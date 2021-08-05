package comodo2.templates.qpc.c;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import comodo2.engine.Main;
import comodo2.queries.QClass;
import comodo2.queries.QState;
import comodo2.queries.QStateMachine;
import comodo2.queries.QTransition;
import comodo2.templates.qpc.Utils;
import comodo2.templates.qpc.model.CurrentGeneration;
import comodo2.templates.qpc.traceability.FileDescriptionHeader;
import comodo2.utils.FilesHelper;
import comodo2.utils.StateComparator;
import comodo2.utils.TransitionComparator;

import java.util.TreeSet;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class StateMachineSource implements IGenerator {

	private static final Logger mLogger = Logger.getLogger(Main.class);
	
	@Inject
	private QStateMachine mQStateMachine;
	
	@Inject
	private QState mQState;
	
	@Inject
	private QTransition mQTransition;
	
	@Inject
	private QClass mQClass;
	
	@Inject
	private Utils mUtils;

	@Inject
	private FileDescriptionHeader mFileDescHeader;

	@Inject
	private FilesHelper mFilesHelper;
	

	/* #########  QPC-specific  ######## */

	// current serves as a container for variables corresponding to the current state machine in generation.
	private CurrentGeneration current;

	private boolean USER_LOGGING = true;
	
	private final static String Q_HANDLED = "Q_HANDLED()";
	private final static String Q_TRAN = "Q_TRAN";
	private final static String Q_INIT_SIG = "Q_INIT_SIG";
	private final static String Q_ENTRY_SIG = "Q_ENTRY_SIG";
	private final static String Q_EXIT_SIG = "Q_EXIT_SIG";
	private final static String Q_TOP_STATE = "QHsm_top";

	/* ################################ */


	/**
	 * Transform UML State Machine associated to a class (classifier behavior)
	 * into a Quantum Framework XML file for the Quantum Modeler.
	 * 
	 * The UML Class should:
	 * - be inside a UML Package with stereotype cmdoModule
	 * - the cmdoModule name should have been provided in the configuration
	 * - have stereotype cmdoComponent
	 * - have an associated UML State Machine
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		
		final TreeIterator<EObject> allContents = input.getAllContents();
		while (allContents.hasNext()) {
			EObject e = allContents.next();
			if (e instanceof org.eclipse.uml2.uml.Class) {
				org.eclipse.uml2.uml.Class c = (org.eclipse.uml2.uml.Class)e; 
				if ((mQClass.isToBeGenerated(c) && mQClass.hasStateMachines(c))) {
					for (final StateMachine sm : mQClass.getStateMachines(c)) {
						
						// Sets current generation context
						current = new CurrentGeneration(c.getName(), sm.getName());
						
						preprocessStateMachine(sm, current);

						mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toQmFilePath(current.getSmQualifiedName())));
						fsa.generateFile(mFilesHelper.toCFilePath(current.getSmQualifiedName()), this.generate(sm));						
					}
				}				
			}
		}
	}


	public CharSequence generate(final StateMachine sm) {
		StringConcatenation str = new StringConcatenation();

		str.append(mFileDescHeader.generateFileDescriptionHeader(current.getClassName(), sm.getName(), true));
		str.append(printStateMachineIncludes(current.getSmQualifiedName()));
		
		str.append(printNewlines(3));

		str.append(printStateMachineDefinitions(current.getSmQualifiedName()));

		str.append(printInitialState(mQStateMachine.getInitialStateName(sm), sm.getName()));

		str.append(exploreAllStates(sm));
		


		String result = str.toString();
		result = result.replace("_INITIAL_STATE_PLACEHOLDER_FOR_SIGNAL_SUBSCRIPTION_", this.printInitialSignalSubscription());


		return result;
	}

	/**
	 * Before actually traversing the state machine, we do some minimal transformation
	 * on unnamed elements. 
	 * - unnamed states are renamed with a unique name
	 * - unnamed history pseudostates are renamed with a unique name.
	 */
	public void preprocessStateMachine(final StateMachine sm, CurrentGeneration current) {
		// Loop through all states and rename unnamed states (special name for final states).
		for (State s : Iterables.<State>filter(sm.allOwnedElements(), State.class)) {
			// loop through all states and rename the unnamed states with an unique name.
			if (mQState.isFinal(s) && s.getName().equals("")){
				s.setName("finalState" + current.getFinalStateCounter());
			} else if (s.getName().equals("")){
				Integer unamedCounter = current.getUnNamedStateCounter();
				mLogger.warn("An unnamed state was found in State Machine: \"" + sm.getName() + "\". Renamed unnamed" + unamedCounter);
				s.setName("unnamedState" + unamedCounter);
			}
		}
		// Loop through all pseudostates and rename unnamed history pseudostates.
		for (Pseudostate ps : Iterables.<Pseudostate>filter(sm.allOwnedElements(), Pseudostate.class)) {
			if (mQState.isHistoryState(ps)){
				if (ps.getName().equals("")){
					String psName = mQState.getParentState(ps).getName() + mQState.getHistoryTypeName(ps) + "History" + current.getHistoryCounter();
					ps.setName(psName); // we could remove the counter if we knew states had a unique name.
				}
			}
		}
	}

	/**
	 * Start transformation of all states.
	 */
	public CharSequence exploreAllStates(final StateMachine sm) {
		StringConcatenation str = new StringConcatenation();

		TreeSet<State> sortedTopStates = new TreeSet<State>(new StateComparator());

		for (State s : Iterables.<State>filter(sm.allOwnedElements(), State.class)) {
			if (mQState.isTopState(s)){
				sortedTopStates.add(s);
			}
		}

		for (final State s : sortedTopStates) {
			str.append(exploreState(s));
		}
		return str;
	}

	/**
	 * Transforms a state and all its sub-states into QPC C code
	 */
	public CharSequence exploreState(final State s) {
		String full_state_string = "";

		full_state_string += printState(s);

		if (s.isComposite()){
			for (State substate : mQState.getAllDirectSubstates(s)){
				full_state_string += exploreState(substate);
			}
		}

		return full_state_string;
	}

	/**
	 * Transforms a single state into QPC C code
	 */
	public CharSequence printState(final State s) {
		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st = g.getInstanceOf("StateMachine_State");
		
		st.add("stateName", s.getName()); 
		st.add("stateQualifiedName", mUtils.formatStateName(s.getName(), current.getSmQualifiedName()));
		st.add("smQualifiedName", current.getSmQualifiedName());
		st.add("logging", USER_LOGGING);
		st.add("signalSwitchCase", printSwitchCaseStatements(s));

		if (mQState.isTopState(s)){
			st.add("superState", Q_TOP_STATE);
		} else {
			st.add("superState", current.getSmQualifiedName() + "_" + mQState.getParentState(s).getName());
		}

		return st.render();
	}

	/**
	 * Prints all switch/cases of a state.
	 * That includes Entry - Exit actions, transitions, ...
	 */
	public CharSequence printSwitchCaseStatements(final State s) {
		StringConcatenation str = new StringConcatenation();
		
		if (s.isComposite()) {
			if (mQState.hasInitialSubstate(s)){
				str.append(printInitialSubstateCase(s));
			}
		} else if (mQState.isFinal(s)) {
			str.append(printFinalStateCase(s));
		}

		str.append(printActions(s));

		str.append(printTransitions(s));
		
		return str;
	}

	/**
	 * Returns the special case corresponding to a final state.
	 */
	public String printFinalStateCase(final State s){
		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st_final = g.getInstanceOf("StateMachine_SwitchStatement");
		String completionSig;

		if (mQState.isTopState(s)){ // state machine wide final node
			completionSig = "_SIG_" + current.getSmQualifiedName().toUpperCase() + "_COMPLETE_";
		} else { // if final state is within a composite state
			completionSig = "_SIG_" + mUtils.formatStateEnum(mQState.getFullyQualifiedName(mQState.getParentState(s)), current.getSmQualifiedName().toUpperCase()) + "_COMPLETE_";	
		}

		current.addSignalEventsNameset(completionSig);

		st_final.add("signalName", completionSig);
		st_final.add("returnStatement", Q_HANDLED);
		return st_final.render();
	}

	/**
	 * Prints the initial case of a composite state.
	 */
	public CharSequence printInitialSubstateCase(final State s) {

		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st_init = g.getInstanceOf("StateMachine_SwitchStatement");

		st_init.add("signalName", Q_INIT_SIG);
		st_init.add("logging", false);

		try { 
			// If the initial node points to a pseudostate (like a choice node), this will throw a ClassCastException
			st_init.add("returnStatement", transitionToStateMacro(mQState.getInitialSubstateName(s))); 
		} catch (ClassCastException e){
			// We know that we need to printChoices on the first transition instead
			st_init.add("action", printChoices(mQState.getInitialSubstateTransition(s)));
			st_init.add("returnStatement", Q_HANDLED + "/*no choice matched*/"); 
		}
		
		return st_init.render();
	}

	/**
	 * Prints the entry and exit (actions) cases of a state.
	 */
	public CharSequence printActions(final State s) {
		String str = "";

		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st_entry = g.getInstanceOf("StateMachine_SwitchStatement");
		ST st_exit = g.getInstanceOf("StateMachine_SwitchStatement");
		
		st_entry.add("signalName", Q_ENTRY_SIG);
		st_entry.add("logging", USER_LOGGING);
		st_entry.add("onEntryStateEnum", mUtils.formatStateEnum(s.getName(), current.getSmQualifiedName()));
		st_entry.add("returnStatement", Q_HANDLED);
		st_exit.add("signalName", Q_EXIT_SIG);
		st_exit.add("logging", USER_LOGGING);
		st_exit.add("returnStatement", Q_HANDLED);

		// Handling of History pseudostates
		BasicEList<Pseudostate> historiyList = mQState.getAllParentHistoryNodes(s);
		if (!historiyList.isEmpty()){
			st_entry.add("historyList", historiyList);
			st_entry.add("stateQualifiedName", mUtils.formatStateName(s.getName(), current.getSmQualifiedName()));
		} // Handling of Entry actions
		if (mQState.hasOnEntryActions(s) || mQState.hasTimerTransition(s)) {
			st_entry.add("action", mUtils.formatActionName(s.getEntry().getName(), current.getSmQualifiedName(), current.getClassName()));
		} // Do activities are not supported in QF
		if (mQState.hasDoActivities(s)) {
			mLogger.warn("SKIPPED -- Do activities are not supported in QPC" +
			 				"(found in state " + s.getName() + ")");
		} // Handling of Exit actions
		if (mQState.hasOnExitActions(s) || mQState.hasTimerTransition(s)) {
			st_exit.add("action", mUtils.formatActionName(s.getExit().getName(), current.getSmQualifiedName(), current.getClassName()));
		}

		str += st_entry.render();
		str += st_exit.render();

		return str;
	}

	/**
	 * Returns the code string of all the outgoing transitions of a state,
	 * as switch-case statements.
	 */
	public CharSequence printTransitions(final State s) {
		String str = "";

		TreeSet<Transition> sortedTrans = new TreeSet<Transition>(new TransitionComparator());
		for (final Transition t : s.getOutgoings()) {
			sortedTrans.add(t);
		}
		for(final Transition t : sortedTrans) {
			if (mQTransition.isMalformed(t)) {
				mLogger.warn("Internal transition from state " + 
				mQState.getStateName(s) + 
				" has no trigger event and no guard, skipped since could introduce infinite loop!");

			} else {
				str += printTransition(t);
				registerEvent(t);
			}
		}
		return str;
	}
	
	/**
	 * @return Code string for the switch-case of transition t.
	 */
	public String printTransition(final Transition t){
		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st_tran = g.getInstanceOf("StateMachine_SwitchStatement");
		st_tran.add("logging", USER_LOGGING);
		
		String guard  = mQTransition.getResolvedGuardName(t);
		String action = mQTransition.getFirstActionName(t);
		String eventName  = mQTransition.getFirstEventName(t); 
		String signalName = mUtils.formatSignalName(eventName, current.getClassName());
		st_tran.add("signalName", signalName);

		// if-else
		if (mQTransition.isChoiceTransition(t)) {
			// Case where the transition points to a choice node: recursively goes down all paths

			st_tran.add("action", printChoices(t));
			st_tran.add("returnStatement", Q_HANDLED);
		
		} else if (!Objects.equal(guard, "")) {
			// Case where there is a guard on the transition

			ST st_if = g.getInstanceOf("StateMachine_IfStatement");
			st_if.add("guard", mUtils.formatGuardName(guard, current.getSmQualifiedName()));
			st_if.add("action", mUtils.formatActionName(action, current.getSmQualifiedName(), current.getClassName()));
			st_if.add("returnStatement", getReturnStatement(t));
			appendFinalStateAction(t, st_if); // handles the cases where the transition points to a final state
			
			st_tran.add("action", st_if.render());
			st_tran.add("returnStatement", Q_HANDLED);
			
		} else if (!Objects.equal(eventName, "")) {
			// Case where there is no guard, only a triggerring event.

			st_tran.add("action", mUtils.formatActionName(action, current.getSmQualifiedName(), current.getClassName()));
			st_tran.add("returnStatement", getReturnStatement(t));
			appendFinalStateAction(t, st_tran); // handles the cases where the transition points to a final state
		
		} else {
			mLogger.warn("SKIPPED: Empty transition (no guard, no trigger) was found going out of state: " + t.getSource().getName());
		} // end of if-else
		
		return st_tran.render();
	}


	/**
	 * Returns the code string of transitions that points to a choice node.
	 * This recursively goes down all the following choice nodes until it reaches a state.
	 * @param t Transition that points to a choice node.
	 */
	public CharSequence printChoices(final Transition t) {
		if (!(t.getTarget() instanceof Pseudostate)) {
			mLogger.warn("An error occured while traversing the model. Transition " + t.toString() + " does not point to a choice node.");
			return "";
		}

		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st_if_root = g.getInstanceOf("StateMachine_IfStatement");

		// We need an else_tmp_str so that the else cases can be printed last.
		String str = "";
		String else_tmp_str = "";

		Pseudostate choicePseudoState = (Pseudostate) t.getTarget();

		String guard  = mQTransition.getGuardNameOrNull(t);
		String root_action = mQTransition.getFirstActionName(t);

		// isElseStatement is a boolean to indicate whether to use an "if(guard)" or an "else" statement
		st_if_root.add("isElseStatement", Objects.equal(guard, "else"));
		st_if_root.add("guard", mUtils.formatGuardName(guard, current.getSmQualifiedName()));


		for (Transition outgoing : choicePseudoState.getOutgoings()){
			// If the transition points to a choice node, recursively re-iterate
			if (mQTransition.isChoiceTransition(outgoing)){
				// If the guard is "else", we store it and add it at the very end
				if (Objects.equal(mQTransition.getGuardNameOrNull(outgoing), "else")){
					else_tmp_str += printChoices(outgoing);
				} else{
					str += printChoices(outgoing);
				}
			} else {
				// String out_targetName = mQTransition.getTargetName(outgoing); 
				String out_guard  = mQTransition.getGuardNameOrNull(outgoing);
				String out_action = mQTransition.getFirstActionName(outgoing);

				ST st_if = g.getInstanceOf("StateMachine_IfStatement");
				st_if.add("isElseStatement", Objects.equal(out_guard, "else"));
				st_if.add("guard", mUtils.formatGuardName(out_guard, current.getSmQualifiedName()));
				st_if.add("action", mUtils.formatActionName(out_action, current.getSmQualifiedName(), current.getClassName()));
				st_if.add("returnStatement", getReturnStatement(outgoing));
				appendFinalStateAction(outgoing, st_if); // handles the cases where the transition points to a final state

				// If the guard is "else", we store it and add it at the very end
				if (Objects.equal(out_guard, "else")){
					else_tmp_str += st_if.render();
				} else {
					str += st_if.render();
				}

			}
		}
		// add the else case at the end
		str += else_tmp_str;
		// add root_action at the beginning of the choice if-else string.
		st_if_root.add("action", (mUtils.formatActionName(root_action, current.getSmQualifiedName(), current.getClassName()) + "\n" + str).trim());

		return st_if_root.render();
	}

	/**
	 * IF Transition t points to a FinalState, appends to the "action" field of st_original 
	 * the code string responsible for dealing with final states. ELSE, does nothing.
	 */
	public void appendFinalStateAction(final Transition t, ST st_original){
		if (t.getTarget() instanceof FinalState){
			STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
			ST st_final_action = g.getInstanceOf("FinalTransitionAction");
			st_final_action.add("smQualifiedNameUpperCase", current.getSmQualifiedName().toUpperCase());

			String completionSig;
			State finalState = (State) t.getTarget();
			if (mQState.isTopState(finalState)){
				completionSig = "_SIG_" + current.getSmQualifiedName().toUpperCase() + "_COMPLETE_";
			} else { // need to retrieve the parent state name for completion event.
				completionSig = "_SIG_" + mUtils.formatStateEnum(mQState.getFullyQualifiedName(mQState.getParentState(finalState)), current.getSmQualifiedName().toUpperCase()) + "_COMPLETE_";	
			}
			st_final_action.add("completionSig", completionSig);
			st_original.add("action", st_final_action.render());
		}
	}

	/**
	 * Registers any Event within a transition so that it can later be added to the class declaration.
	 */
	public void registerEvent(Transition t){
		String eventName  = mQTransition.getFirstEventName(t);

		if (!Objects.equal(eventName, "")) {
			// Removing non alphanumeric characters since this will be the name of a C variable
			
			if (mQTransition.hasSignalEvent(t)) {
				current.addSignalEventsNameset(mUtils.formatSignalName(eventName, current.getClassName()));
			} else if (mQTransition.hasTimeEvent(t)) {
				mLogger.warn("SKIPPED: TimeEvent detected - not supported (outbound from state " + t.getSource().getName() + ")");
			}
		}
	}


	/**
	 * Prints the initial transition of a State machine
	 */
	public CharSequence printInitialState(final String targetName, String smName) {

		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st = g.getInstanceOf("StateMachine_InitialState");

		st.add("smQualifiedName", current.getSmQualifiedName());
		st.add("returnStatement", transitionToStateMacro(targetName));

		return st.render();
	}

	/**
	 * Returns the returnStatement that a transition should have.
	 * This depends on the type of the targeted vertex.
	 */
	public String getReturnStatement(Transition t) {
		String targetName = mQTransition.getTargetName(t);
		
		if (mQTransition.isInternal(t)) {
			return Q_HANDLED + "/*internal transition*/";
		} else if (mQTransition.pointsToHistoryPseudostate(t)){
			return transitionToHistoryMacro(targetName);
		} else if (t.getTarget() instanceof State){
			return transitionToStateMacro(targetName);
		} else {
			throw new RuntimeException( "Transition outbound from state \"" + t.getSource().getName() + 
						"\" has an unsupporeted target type (target element: " + t.getTarget().toString() + ").");
		}
	}

	/**
	 * Returns the QPC-specific return statement for a transition to stateName
	 */
	public String transitionToStateMacro(String stateName){
		if (Objects.equal(stateName, "") || stateName == null){
			
			return null;
		}
		return Q_TRAN + "(&" + current.getSmQualifiedName() + "_" + stateName + ")";
	}

	/**
	 * Returns the QPC-specific return statement for a transition to stateName
	 */
	public String transitionToHistoryMacro(String historyStateName){
		if (Objects.equal(historyStateName, "") || historyStateName == null){
			
			return null;
		}
		return Q_TRAN + "(me->" + historyStateName + ")";
	}

	/**
	 * Returns the code string that needs to be injected in the Initial state of the State Machine.
	 * This calls QPC-specific functions to subscribe to all events.
	 */
	public String printInitialSignalSubscription(){
		String str = "// Subscribe to all the signals to which this state machine needs to respond.\n";
		str += "	if (me->active == (QActive *)me) {\n";

		for (String signalName : current.getSignalEventsNameset()){
			str += "		QActive_subscribe(me->active, " + signalName + ");\n";
		}
		str += "	}";
		// TODO: // Do NOT subscribe to events if in a submachine
		return str;
	}

	public CharSequence printNewlines(Integer n){
		return new String(new char[n]).replace("\0", "\n");
	}


	public String printStateMachineIncludes(final String smName){
        STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-includes.stg");
		ST st = g.getInstanceOf("StateMachineSourceIncludes");
		st.add("smName", smName);

        return st.render();
    }

	public String printStateMachineDefinitions(final String smName){
        STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-definitions.stg");
		ST st = g.getInstanceOf("StateMachineSourceDefinitions");
		st.add("smName", smName);
		st.add("smNameUpperCase", smName.toUpperCase());

        return st.render();
    }
}
