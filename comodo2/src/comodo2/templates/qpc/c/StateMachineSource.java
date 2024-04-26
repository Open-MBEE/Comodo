package comodo2.templates.qpc.c;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import comodo2.engine.Main;
import comodo2.queries.QClass;
import comodo2.queries.QRegion;
import comodo2.queries.QState;
import comodo2.queries.QStateMachine;
import comodo2.queries.QTransition;
import comodo2.templates.qpc.Utils;
import comodo2.templates.qpc.model.CurrentGeneration;
import comodo2.templates.qpc.model.OrthogonalStateWrapper;
import comodo2.templates.qpc.model.RegionWrapper;
import comodo2.templates.qpc.traceability.FileDescriptionHeader;
import comodo2.utils.FilesHelper;
import comodo2.utils.StateComparator;
import comodo2.utils.TransitionComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
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
	private QRegion mQRegion;
	
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

	private boolean USER_LOGGING = false;
	
	private static final String Q_HANDLED = "Q_HANDLED()";
	private static final String Q_TRAN = "Q_TRAN";
	private static final String Q_INIT_SIG = "Q_INIT_SIG";
	private static final String Q_ENTRY_SIG = "Q_ENTRY_SIG";
	private static final String Q_EXIT_SIG = "Q_EXIT_SIG";
	private static final String Q_BAIL_SIG = "Q_BAIL_SIG";
	private static final String Q_TOP_STATE = "QHsm_top";

	/* ################################ */


	/**
	 * Transform UML State Machine associated to a class (classifier behavior)
	 * into a Quantum Framework C source file.
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

		str.append(printStateMachineDefinitions(current.getSmQualifiedName(), sm));

		str.append(printInitialTransition(sm));

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
	 * - unnamed orthogonal regions are renamed with a unique name.
	 */
	public void preprocessStateMachine(final StateMachine sm, CurrentGeneration current) {
		// Loop through all states and rename unnamed states (special name for final states).
		for (State s : Iterables.<State>filter(sm.allOwnedElements(), State.class)) {
			// loop through all states and rename the unnamed states with an unique name.
			if (mQState.isFinal(s) && s.getName().equals("")){
				s.setName("finalState" + current.getFinalStateCounter());
			} else if (s.getName().equals("")){
				Integer unamedCounter = current.getUnNamedStateCounter();
				mLogger.info("An unnamed state was found in State Machine: \"" + sm.getName() + "\". Renamed unnamed" + unamedCounter);
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
		// Rename unnamed orthogonal regions
		for (Region r : Iterables.<Region>filter(sm.allOwnedElements(), Region.class)){
			if (mQRegion.getParentState(r)!=null && mQRegion.getParentState(r).isOrthogonal()){
				if (Objects.equal(r.getName(), "")){
					r.setName("region" + current.getRegionCounter());
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
		
		if (s.isComposite()){
			return exploreCompositeState(s);
		} else if (s.isSimple()) {
			return printState(s);
		} else {
			throw new RuntimeException("Error transforming state \"" + s.getName() + "\". Not simple nor composite.");
		}
	}

	/**
	 * Explore a composite state.
	 */
	public CharSequence exploreCompositeState(final State s) {
		if (s.isOrthogonal()){
			return exploreOrthogonalState(s);
		} else if (s.isComposite()){
			StringConcatenation compositeStateString = new StringConcatenation();
			compositeStateString.append(printState(s));
			for (State substate : mQState.getAllDirectSubstates(s)){
				compositeStateString.append(exploreState(substate));
			}
			return compositeStateString;
		} else {
			throw new RuntimeException("Error transforming state \"" + s.getName() + "\". Thought to be composite.");
		}
	}

	/**
	 * Explore all regions of an orthogonal state.
	 */
	public CharSequence exploreOrthogonalState(final State s) {
		StringConcatenation orthogonalStateString = new StringConcatenation();

		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-orthogonalState.stg");

		orthogonalStateString.append(printState(s));

		for (Region r : Iterables.<Region>filter(s.allOwnedElements(), Region.class)) {
			ST st_region = g.getInstanceOf("OrthogonalRegionMethodDefinitions");

			st_region.add("logging", USER_LOGGING);
			st_region.add("regionQualifiedName", mUtils.formatRegionName(mQRegion.getFullyQualifiedName(r)));
			st_region.add("smQualifiedName", current.getSmQualifiedName());
			st_region.add("smQualifiedNameUppercase", current.getSmQualifiedName().toUpperCase());
			st_region.add("initialState", transitionToStateMacro(mQRegion.getInitialState(r)));

			orthogonalStateString.append(st_region.render());

			for(final State substate : Iterables.<State>filter(r.allOwnedElements(), State.class)) {
				if (Objects.equal(substate.getOwner(), r)) {
					orthogonalStateString.append( exploreState(substate) );
				}
			}
		}

		return orthogonalStateString;
	}

	/**
	 * Transforms a single state into QPC C code
	 */
	public CharSequence printState(final State s) {
		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st = g.getInstanceOf("StateMachine_State");

		st.add("stateName", s.getName()); 
		st.add("stateQualifiedName", mUtils.formatStateName(mQState.getFullyQualifiedName(s), current.getSmQualifiedName()));
		st.add("logging", USER_LOGGING);
		st.add("activeObject", getActiveObjectName(s));
		st.add("signalSwitchCase", printSwitchCaseStatements(s));

		// Default case if switch check fails
		if (mQState.isTopState(s)){
			st.add("superState", Q_TOP_STATE);
		} else {
			st.add("superState", mUtils.formatStateName(mQState.getFullyQualifiedName(mQState.getParentState(s)), current.getSmQualifiedName()));
		}

		return st.render();
	}

	/**
	 * Prints all switch/cases of a state.
	 * That includes Entry - Exit actions, transitions, and special cases based on the state type.
	 */
	public CharSequence printSwitchCaseStatements(final State s) {
		StringConcatenation str = new StringConcatenation();
		
		// Orthogonal state. This handles the passing of signals to submachines.
		if (s.isOrthogonal()){
			str.append(printOrthogonalSignalDispatches(s));
		} 
		else if (s.isComposite()) {
			/*  Only for composite states that are not orthogonal. This is because
				orthogonal states have mutiple initial substates, which are dealt with differently. */
			if (mQState.hasInitialSubstate(s)){
				str.append(printInitialSubstateCase(s));
			}
		} 
		// Final state
		if (mQState.isFinal(s)) {
			str.append(printFinalStateCase(s));
		}
		// If s is contained within an orthogonal state
		if (mQState.getParentOrthogonalRegion(s)!=null){
			str.append(printBailingCase(s, mQState.getParentOrthogonalRegion(s)));
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

		st_final.add("triggerEventName", completionSig);
		st_final.add("returnStatement", Q_HANDLED);
		return st_final.render();
	}

	/**
	 * Prints the initial case of a composite state.
	 */
	public CharSequence printInitialSubstateCase(final State s) {

		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st_init = g.getInstanceOf("StateMachine_SwitchStatement");

		st_init.add("triggerEventName", Q_INIT_SIG);
		st_init.add("logging", false);

		try { 
			// If the initial node points to a pseudostate (like a choice node), this will throw a ClassCastException
			st_init.add("returnStatement", transitionToStateMacro(mQState.getInitialSubstate(s))); 
		} catch (ClassCastException e){
			// We know that we need to printChoices on the first transition instead
			st_init.add("action", printChoices(mQState.getInitialSubstateTransition(s)));
			st_init.add("returnStatement", Q_HANDLED + "/*no choice matched*/"); 
		}
		return st_init.render();
	}

	/**
	 * Returns the special case for bailing out of an orthogonal state.
	 * This should only be printed when the state is part of an orthogonal state.
	 */
	public String printBailingCase(final State s, final Region parentOrthogonalRegion){
		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st_final = g.getInstanceOf("StateMachine_SwitchStatement");
		
		String stateHandlerFunction = mUtils.formatStateName(mQRegion.getFullyQualifiedName(parentOrthogonalRegion), current.getSmQualifiedName());
		st_final.add("triggerEventName", Q_BAIL_SIG);
		st_final.add("returnStatement", Q_TRAN + "(&" + stateHandlerFunction + "_final)");
		return st_final.render();
	}

	/**
	 * Prints the entry and exit (actions) cases of a state.
	 * Because of QF architecture, this also implies dealing with history pseudostates,
	 * TimeEvents, and other types of state-specific things.
	 */
	public CharSequence printActions(final State s) {
		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st_entry = g.getInstanceOf("StateMachine_SwitchStatement");
		ST st_exit = g.getInstanceOf("StateMachine_SwitchStatement");
		
		st_entry.add("triggerEventName", Q_ENTRY_SIG);
		st_entry.add("logging", USER_LOGGING);
		st_entry.add("onEntryStateEnum", mUtils.formatStateEnum(mQState.getFullyQualifiedName(s), current.getSmQualifiedName()));
		st_entry.add("returnStatement", Q_HANDLED);
		st_exit.add("triggerEventName", Q_EXIT_SIG);
		st_exit.add("logging", USER_LOGGING);
		st_exit.add("returnStatement", Q_HANDLED);

		// Handling of Entry actions
		if (mQState.hasOnEntryActions(s)) {
			st_entry.add("action", mUtils.formatActionName(s.getEntry().getName(), current.getSmQualifiedName(), current.getClassName()));
		} // Do activities are not supported in QF
		if (mQState.hasDoActivities(s)) {
			mLogger.warn("SKIPPED -- Do activities are not supported in QPC" +
			 				"(found in state " + s.getName() + ")");
		} // Handling of Exit actions
		if (mQState.hasOnExitActions(s)) {
			st_exit.add("action", mUtils.formatActionName(s.getExit().getName(), current.getSmQualifiedName(), current.getClassName()));
		}

		// Handling of History pseudostates
		BasicEList<Pseudostate> historiyList = mQState.getAllParentHistoryNodes(s);
		if (!historiyList.isEmpty()){
			st_entry.add("historyList", historiyList);
			st_entry.add("stateQualifiedName", mUtils.formatStateName(mQState.getFullyQualifiedName(s), current.getSmQualifiedName()));
		}
		// Handling of relative TimeEvents
		String timeEventDuration = mQState.getFirstTimeEventDurationString(s);
		if (timeEventDuration != null){
			st_entry.add("stateName", s.getName());
			st_entry.add("timerDuration", timeEventDuration);
			st_exit.add("stateName", s.getName());
			st_exit.add("disarmTimeEvent", true);
		}
		// Handling of orthogonal states. These need to pass along signals to their regions etc..
		if (s.isOrthogonal()){
			OrthogonalStateWrapper orthogonalStateWrapper = new OrthogonalStateWrapper(s, current);
			st_entry.add("entryOrthogonalStateWrapper", orthogonalStateWrapper);
			st_exit.add("exitOrthogonalStateWrapper", orthogonalStateWrapper);
		}

		return st_entry.render() + st_exit.render();
	}

	/**
	 * Returns the code string of all the outgoing transitions of a state,
	 * as switch-case statements.
	 */
	public CharSequence printTransitions(final State s) {
		StringConcatenation str = new StringConcatenation();

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
				str.append(printTransition(t));
				registerEvent(t);
			}
		}
		return str;
	}
	
	/**
	 * @return Code string for the switch-case of transition t.
	 */
	public CharSequence printTransition(final Transition t){
		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st_tran = g.getInstanceOf("StateMachine_SwitchStatement");
		st_tran.add("logging", USER_LOGGING);
		
		String guardName  = mQTransition.getResolvedGuardName(t);
		String actionName = mQTransition.getFirstActionName(t);
		String eventName  = mQTransition.getFirstEventName(t); 
		
		if (mQTransition.hasSignalEvent(t)){
			String signalName = mUtils.formatSignalName(eventName, current.getClassName());
			st_tran.add("triggerEventName", signalName);
		} else if (mQTransition.hasTimeEvent(t)){
			String timeEventName = mUtils.formatTimeEventName(t.getSource().getName());
			st_tran.add("triggerEventName", timeEventName);
		}
		String actionStr;
		String returnStr;
		
		// if-else: depending on the type of the transition, actionStr and returnStr will be formed differently
		if (mQTransition.isChoiceTransition(t)) {
			// Case where the transition points to a choice node: recursively goes down all paths
			actionStr = printChoices(t);
			returnStr = Q_HANDLED;
		
		} else if (!Objects.equal(guardName, "")) {
			// Case where there is a guard on the transition
			ST st_if = g.getInstanceOf("StateMachine_IfStatement");
			st_if.add("guard", mUtils.formatGuardName(guardName, current.getSmQualifiedName()));
			st_if.add("action", mUtils.formatActionName(actionName, current.getSmQualifiedName(), current.getClassName()));
			st_if.add("returnStatement", getReturnStatement(t));
			
			actionStr = st_if.render();
			returnStr = Q_HANDLED;
			
		} else if (!Objects.equal(eventName, "")) {
			// Case where there is no guard, only a triggerring event.
			actionStr = mUtils.formatActionName(actionName, current.getSmQualifiedName(), current.getClassName());
			returnStr = getReturnStatement(t);
		
		} else {
			mLogger.warn("SKIPPED: Empty transition (no guard, no trigger) was found going out of state: " + t.getSource().getName());
			return "// NOT GENERATED: An empty transition was found on event: " + eventName + "\n";
		} // end of if-else
		
		// handles the cases where the transition points to a final state
		if (t.getTarget() instanceof FinalState){
			actionStr += appendFinalStateAction(t); 
		} // end of final state handling
		
		st_tran.add("action", actionStr);
		st_tran.add("returnStatement", returnStr);

		return st_tran.render();
	}


	/**
	 * Returns the code string of transitions that points to a choice node.
	 * This recursively goes down all the following choice nodes until it reaches a state.
	 * @param t Transition that points to a choice node.
	 */
	public String printChoices(final Transition t) {
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
				String out_guardName  = mQTransition.getGuardNameOrNull(outgoing);
				String out_actionName = mQTransition.getFirstActionName(outgoing);

				String actionStr = mUtils.formatActionName(out_actionName, current.getSmQualifiedName(), current.getClassName());
				
				// handles the cases where the transition points to a final state
				if (outgoing.getTarget() instanceof FinalState){
					actionStr += appendFinalStateAction(outgoing);
				}
				
				ST st_if = g.getInstanceOf("StateMachine_IfStatement");
				st_if.add("isElseStatement", Objects.equal(out_guardName, "else"));
				st_if.add("guard", mUtils.formatGuardName(out_guardName, current.getSmQualifiedName()));
				st_if.add("action", actionStr);
				st_if.add("returnStatement", getReturnStatement(outgoing));
				
				// If the guard is "else", we store it and add it at the very end
				if (Objects.equal(out_guardName, "else")){
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
	 * Returns the special cases for an orthogonal state, that will dispatch the signals it receives
	 * to its children regions that need to handle the signal.
	 */
	public CharSequence printOrthogonalSignalDispatches(final State s){
		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		StringConcatenation str = new StringConcatenation();
		
		OrthogonalStateWrapper orthogonalStateWrapper = new OrthogonalStateWrapper(s, current);
		HashMap<String, ArrayList<String>> signalDispatchMap = getSignalDispatches(orthogonalStateWrapper);

		for (String triggerEventName : signalDispatchMap.keySet()) {
			ST st_final = g.getInstanceOf("StateMachine_SwitchStatement");
			
			st_final.add("triggerEventName", triggerEventName);
			st_final.add("returnStatement", Q_HANDLED);
			st_final.add("regionDispatchList", signalDispatchMap.get(triggerEventName));

			str.append(st_final.render());
		}

		return str.toString();
	}


	/**
	 * IF Transition t points to a FinalState, appends to the "action" field of st_original 
	 * the code string responsible for dealing with final states. ELSE, does nothing.
	 */
	public String appendFinalStateAction(final Transition t){
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
			return st_final_action.render();
		} else {
			return "";
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
				// mLogger.warn("SKIPPED: TimeEvent detected - not supported (outbound from state " + t.getSource().getName() + ")");
			}
		}
	}


	/**
	 * Prints the initial transition of a State machine
	 */
	public CharSequence printInitialTransition(final StateMachine sm) {

		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st = g.getInstanceOf("StateMachine_InitialState");

		st.add("smQualifiedName", current.getSmQualifiedName());
		st.add("returnStatement", transitionToStateMacro(mQStateMachine.getInitialState(sm)));

		return st.render();
	}

	/**
	 * Returns the returnStatement that a transition should have.
	 * This depends on the type of the targeted vertex.
	 */
	public String getReturnStatement(Transition t) {

		// Internal transition
		if (mQTransition.isInternal(t)) {
			return Q_HANDLED + "/*internal transition*/";
		} 
		// History pseudostate
		else if (mQTransition.pointsToHistoryPseudostate(t)){
			// the history pseudostate carries the name of the property
			return transitionToHistoryMacro(mQTransition.getTargetName(t));
		} 
		// Entry/Exit pseudostate
		else if (mQTransition.pointsToEntryOrExitPseudostate(t)) {
			if (mQTransition.getEntryOrExitTargetName(t).equals("")){
				mLogger.error("Entry/Exit pseudostate points to unnamed pseudostate (Outbound from state \"" + t.getSource().getName() + "\"). This is likely caused by an unsupported pattern (e.g. points to choice node).");
				return "NULL; // NOT GENERATED: Malformed Entry/Exit pseudostate";
			}
			// Most likely points to a regular state
			return getReturnStatement(t.getTarget().getOutgoings().get(0));
		}
		// Regular state
		else if (t.getTarget() instanceof State){
			return transitionToStateMacro((State) t.getTarget());
		} 
		// Unsupported pattern?
		else {
			throw new RuntimeException( "Transition outbound from state \"" + t.getSource().getName() + 
						"\" has an unsupporeted target type (target element: " + t.getTarget().toString() + ").");
		}
	}

	/**
	 * Returns the QPC-specific return statement for a transition to stateName
	 */
	public String transitionToStateMacro(State s){
		if (s == null){
			return null;
		}
		if (Objects.equal(s.getName(), "")){
			return null;
		}
		return Q_TRAN + "(&" + mUtils.formatStateName(mQState.getFullyQualifiedName(s), current.getSmQualifiedName()) + ")";
	}

	/**
	 * Returns the QPC-specific return statement for a transition to historyStateName.
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
		// TODO: 
		// - Do NOT subscribe to events if in a submachine??
		// - refactor getSignalEventsNameset() to query the model and not current.
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

	public String printStateMachineDefinitions(final String smName, final StateMachine sm){
        STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-definitions.stg");
		ST st = g.getInstanceOf("StateMachineSourceDefinitions");
		st.add("smName", smName);
		st.add("smNameUpperCase", smName.toUpperCase());
		
		// TimeEvents initialization (constructor call)
		String timeEventInitString = "";
		for (String stateName : mQStateMachine.getAllStatesWithTimeEvents(sm)){
			timeEventInitString += "QTimeEvt_ctor(&(me->" + stateName + "RelativeTimer), " + mUtils.formatTimeEventName(stateName) + ");\n";
		}
		if (!timeEventInitString.equals("")){
			st.add("timeEventInitString", timeEventInitString);
		} // end of TimeEvents

		// Orthogonal regions definitions
		BasicEList<RegionWrapper> orthogonalRegions = new BasicEList<RegionWrapper>();
		for (State s : mQStateMachine.getAllOrthogonalStates(sm)){
			OrthogonalStateWrapper orthogonalStateWrapper = new OrthogonalStateWrapper(s, current);
			orthogonalRegions.addAll(orthogonalStateWrapper.getWrappedRegions());
		}
		if (!orthogonalRegions.isEmpty()){
			st.add("orthogonalRegions", orthogonalRegions);
		}
		// end of orthogonal regions definitions

        return st.render();
    }

	/**
	 * This varies based on whether the state is part of an orthogonal region or not.
	 * If part of an orthogonal region, the ActiveObject will be the one of the region.
	 * If not, the ActiveObject will be the one of the state machine.
	 * @param s State
	 * @return name of the ActiveObject that this state definition takes as an input.
	 */
	public String getActiveObjectName(final State s){
		Region r = mQState.getParentOrthogonalRegion(s);
		if (r!=null){
			return mUtils.formatRegionName(mQRegion.getFullyQualifiedName(r));
		} else {
			return current.getSmQualifiedName();
		}
	}






	/**
	 *    /!\  This method should ideally be in the OrthogonalStateWrapper class, but calling mQTransition.getFirstEventName(t)
	 * from there led to some nullPointerException that I do not fully understand. Will dig into it later,
	 * and for now this is acceptable.
	 * This method returns a HashMap of the signals that should get passed through to subregions in an orthogonal state.
	 */
	public HashMap<String, ArrayList<String>> getSignalDispatches(OrthogonalStateWrapper orthogonalStateWrapper){
        HashMap<String, ArrayList<String>> signalDispatches = new HashMap<String, ArrayList<String>>();

        for (Region r : orthogonalStateWrapper.getRegions()){
            for (Transition t : Iterables.<Transition>filter(r.allOwnedElements(), Transition.class)) {
                String triggerEvent = null;
                if (mQTransition.hasSignalEvent(t)){
                    triggerEvent = mUtils.formatSignalName(mQTransition.getFirstEventName(t), current.getClassName());
                } else if (mQTransition.hasTimeEvent(t)){
                    triggerEvent = mUtils.formatTimeEventName(t.getSource().getName());
                }
                
                if (triggerEvent!=null) {
                    if (signalDispatches.containsKey(triggerEvent)) {
                        signalDispatches.get(triggerEvent).add(r.getName());
                    } else {
                        signalDispatches.put(triggerEvent, new ArrayList<String>());
                        signalDispatches.get(triggerEvent).add(r.getName());
                    }
                    
                }
            }
        }
        return signalDispatches;
    }
}
