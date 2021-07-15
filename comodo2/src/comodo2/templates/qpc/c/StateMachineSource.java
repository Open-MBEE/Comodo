package comodo2.templates.qpc.c;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import comodo2.engine.Main;
import comodo2.queries.QClass;
import comodo2.queries.QState;
import comodo2.queries.QStateMachine;
import comodo2.queries.QTransition;
import comodo2.templates.qpc.QUtils;
import comodo2.utils.FilesHelper;
import comodo2.utils.StateComparator;
import comodo2.utils.TransitionComparator;

import java.util.TreeSet;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
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
	private QUtils mQUtils;

	@Inject
	private FilesHelper mFilesHelper;
	

	/* #########  QPC-specific  ######## */
	private final TreeSet<String>   timeEventsNameset = new TreeSet<String>();
	private final TreeSet<String> signalEventsNameset = new TreeSet<String>();

	private String smQualifiedName;
	private String smClassName;

	private boolean USER_LOGGING = true;
	
	private final String Q_HANDLED = "Q_HANDLED()";
	private final String Q_TRAN = "Q_TRAN";
	private final String Q_INIT_SIG = "Q_INIT_SIG";
	private final String Q_ENTRY_SIG = "Q_ENTRY_SIG";
	private final String Q_EXIT_SIG = "Q_EXIT_SIG";
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
						this.smClassName = c.getName();
						this.smQualifiedName = this.smClassName + "_" + sm.getName();
						mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toQmFilePath(sm.getName())));
						fsa.generateFile(mFilesHelper.toCFilePath(this.smQualifiedName), this.generate(sm));						
					}
				}				
			}
		}
	}


	public CharSequence generate(final StateMachine sm) {
		StringConcatenation str = new StringConcatenation();

		str.append(printStateMachineIncludes(this.smQualifiedName));
		
		str.append(printNewlines(3));

		str.append(printStateMachineDefinitions(this.smQualifiedName));

		str.append(printInitialState(mQStateMachine.getInitialStateName(sm), sm.getName()));

		str.append(exploreAllStates(sm));
		


		String result = str.toString();
		result = result.replace("_INITIAL_STATE_PLACEHOLDER_FOR_SIGNAL_SUBSCRIPTION_", this.printInitialSignalSubscription());


		return result;
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





	/**
	 * Start transformation of all states.
	 */
	public CharSequence exploreAllStates(final StateMachine sm) {
		StringConcatenation str = new StringConcatenation();

		TreeSet<State> sortedStates = new TreeSet<State>(new StateComparator());
		for (final State s : Iterables.<State>filter(sm.allOwnedElements(), State.class)) {
			sortedStates.add(s);
		}
		for (final State s : sortedStates) {
			str.append(exploreState(s));		
		}
		return str;
	}


	/**
	 * Transforms a state into QPC C code
	 */
	public CharSequence exploreState(final State s) {
		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st = g.getInstanceOf("StateMachine_State");

		if (mQState.isFinal(s)) {
			// Final nodes do not exist in QM. Is there a need to handle them?
			mLogger.warn("/!\\ Final state was found");
		} else {

			st.add("smQualifiedName", this.smQualifiedName);
			st.add("stateName", s.getName());
			st.add("logging", USER_LOGGING);

			st.add("signalSwitchCase", printSwitchCaseStatements(s));
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
			str.append(printInitialSubstateCase(s));
		}

		str.append(printActions(s));

		str.append(printTransitions(s));
		

		return str;
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
		st_entry.add("onEntryStateEnum", mQUtils.formatStateName(mQState.getFullyQualifiedName(s), this.smQualifiedName));
		st_entry.add("returnStatement", Q_HANDLED);
		st_exit.add("signalName", Q_EXIT_SIG);
		st_exit.add("logging", USER_LOGGING);
		st_exit.add("returnStatement", Q_HANDLED);

		if (mQState.hasOnEntryActions(s) || mQState.hasTimerTransition(s)) {
			st_entry.add("action", mQUtils.formatActionName(s.getEntry().getName(), this.smQualifiedName));
		}		
		if (mQState.hasDoActivities(s)) {
			mLogger.warn("SKIPPED -- Do activities are not supported in QPC" +
			 				"(found in state " + s.getName() + ")");
		}
		if (mQState.hasOnExitActions(s) || mQState.hasTimerTransition(s)) {
			st_exit.add("action", mQUtils.formatActionName(s.getExit().getName(), this.smQualifiedName));
		}

		str += st_entry.render();
		str += st_exit.render();

		return str;
	}

	/**
	 * 
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
				STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
				ST st_tran = g.getInstanceOf("StateMachine_SwitchStatement");
				st_tran.add("logging", USER_LOGGING);
				
				registerEvent(t);
				
				String eventName  = mQTransition.getFirstEventName(t); 
				String guard  = mQTransition.getResolvedGuardName(t);
				String action = mQTransition.getFirstActionName(t);
				// target is handled in getReturnStatement
				
				String signalName = mQUtils.formatSignalName(eventName, this.smClassName);

				st_tran.add("signalName", signalName);

				if (mQTransition.isChoiceTransition(t)) {
					
					st_tran.add("action", printChoices(t));
					st_tran.add("returnStatement", Q_HANDLED);

				} else if (!Objects.equal(guard, "")) {

					ST st_if = g.getInstanceOf("StateMachine_IfStatement");
					st_if.add("guard", mQUtils.formatGuardName(guard, this.smQualifiedName));
					st_if.add("action", mQUtils.formatActionName(action, this.smQualifiedName));
					st_if.add("returnStatement", getReturnStatement(t));

					st_tran.add("action", st_if.render());
					st_tran.add("returnStatement", Q_HANDLED);

				} else if (!Objects.equal(eventName, "")) {

					st_tran.add("action", mQUtils.formatActionName(action, this.smQualifiedName));
					st_tran.add("returnStatement", getReturnStatement(t));

				} else {
					System.out.println("DEBUGGING: Something is missing...");
				}

				str += st_tran.render();
			}
		}
		return str;
	}

	/**
	 * Registers any Event within a transition so that it can later be added to the class declaration (QM needs it)
	 */
	public void registerEvent(Transition t){
		String eventName  = mQTransition.getFirstEventName(t);

		if (!Objects.equal(eventName, "")) {
			// Removing non alphanumeric characters since this will be the name of a C variable
			
			if (mQTransition.hasSignalEvent(t)) {
				signalEventsNameset.add(mQUtils.formatSignalName(eventName, this.smClassName));
			} else if (mQTransition.hasTimeEvent(t)) {
				timeEventsNameset.add(eventName);
			}
		}
	}


	/**
	 * Prints the initial transition of a State machine
	 */
	public CharSequence printInitialState(final String targetName, String smName) {

		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st = g.getInstanceOf("StateMachine_InitialState");

		st.add("smQualifiedName", this.smQualifiedName);

        return st.render();
	}
	




	/**
	 * Returns the code string of transitions that points to a choice node.
	 * This recursively goes down all the following choice nodes until it reaches a state.
	 * @param Transition t that points to a choice node.
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
		String action = mQTransition.getFirstActionName(t);

		// isElseStatement is a boolean to indicate whether to use an "if(guard)" or an "else" statement
		st_if_root.add("isElseStatement", Objects.equal(guard, "else"));
		st_if_root.add("guard", mQUtils.formatGuardName(guard, this.smQualifiedName));


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
				String out_targetName = mQTransition.getTargetName(outgoing); 
				String out_guard  = mQTransition.getGuardNameOrNull(outgoing);
				String out_action = mQTransition.getFirstActionName(outgoing);

				ST st_if = g.getInstanceOf("StateMachine_IfStatement");
				st_if.add("isElseStatement", Objects.equal(out_guard, "else"));
				st_if.add("guard", mQUtils.formatGuardName(out_guard, this.smQualifiedName));
				st_if.add("action", mQUtils.formatActionName(out_action, this.smQualifiedName));
				st_if.add("returnStatement", transitionToStateMacro(out_targetName));

				// If the guard is "else", we store it and add it at the very end
				if (Objects.equal(out_guard, "else")){
					else_tmp_str += st_if.render();
				} else {
					str += st_if.render();
				}

			}
		}
		str += else_tmp_str;
		st_if_root.add("action", action + "\n" + str);


		return st_if_root.render();
	}

	/**
	 * Returns the returnStatement that a transition should have.
	 * Q_HANDLED for internal transitions, Q_TRAN(< targetState >) for others
	 */
	public String getReturnStatement(Transition t) {
		if (mQTransition.isInternal(t)) {
			return Q_HANDLED + "/*internal transition*/";
		} else {
			return transitionToStateMacro(mQTransition.getTargetName(t));
		}
	}

	/**
	 * Returns the QPC-specific return statement for a transition to stateName
	 */
	public String transitionToStateMacro(String stateName){
		if (Objects.equal(stateName, "") || stateName == null){
			return null;
		}
		return Q_TRAN + "(&" + this.smQualifiedName + "_" + stateName + ")";
	}


	/**
	 * Returns the code string that needs to be injected in the Initial state of the State Machine.
	 * This calls QPC-specific functions to subscribe to all events.
	 */
	public String printInitialSignalSubscription(){
		String str = "// Subscribe to all the signals to which this state machine needs to respond.\n";
		str += "	if (me->active == (QActive *)me) {\n";

		for (String signalName : this.signalEventsNameset){
			str += "		QActive_subscribe(me->active, " + signalName + ");\n";
		}
		str += "	}";
		return str;
	}

}
