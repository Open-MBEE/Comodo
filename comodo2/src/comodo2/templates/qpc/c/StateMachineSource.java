package comodo2.templates.qpc.c;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import comodo2.engine.Main;
import comodo2.queries.QClass;
import comodo2.queries.QState;
import comodo2.queries.QStateMachine;
import comodo2.queries.QTransition;
import comodo2.utils.FilesHelper;
import comodo2.utils.StateComparator;
import comodo2.utils.TransitionComparator;

import java.util.UUID;
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
	private FilesHelper mFilesHelper;
	

	/* #########  QPC-specific  ######## */
	// private final TreeSet<String>   timeEventsNameset = new TreeSet<String>();
	private final TreeSet<String> signalEventsNameset = new TreeSet<String>();

	private String smQualifiedName;
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
						smQualifiedName = c.getName() + "_" + sm.getName();
						mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toQmFilePath(sm.getName())));
						fsa.generateFile(mFilesHelper.toCFilePath(smQualifiedName), this.generate(sm));						
					}
				}				
			}
		}
	}


	public CharSequence generate(final StateMachine sm) {
		StringConcatenation str = new StringConcatenation();

		

		str.append(printStateMachineIncludes(smQualifiedName));
		
		str.append(printNewlines(3));

		str.append(printStateMachineDefinitions(smQualifiedName));


		str.append(printInitialState(mQStateMachine.getInitialStateName(sm), sm.getName()));

		str.append(exploreTopStates(sm));
		




		// str.append(printTimeEvents());
		// str.newLineIfNotEmpty();



		// str.append(printFileTemplates(sm.getName()));

		str.newLineIfNotEmpty();


		String result = str.toString();
		result = result.replace("_INITIAL_STATE_PLACEHOLDER_FOR_SIGNAL_SUBSCRIPTION_", this.printInitialSignalSubscription(sm.getName()));


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
	 * Start transformation from top level states.
	 */
	public CharSequence exploreTopStates(final StateMachine sm) {
		StringConcatenation str = new StringConcatenation();

		TreeSet<State> sortedTopStates = new TreeSet<State>(new StateComparator());
		for (final State s : Iterables.<State>filter(sm.allOwnedElements(), State.class)) {
			if (mQState.isTopState(s)) {
				sortedTopStates.add(s);
			}
		}
		for (final State s : sortedTopStates) {
			str.append(exploreState(s));
			str.newLineIfNotEmpty();			
		}
		return str;
	}

	/**
	 * Transform a simple or composite state.
	 */
	public CharSequence exploreState(final State s) {
		if (s.isSimple()) {
			return exploreSimpleState(s);
		} else if (s.isComposite()) {
			return exploreCompositeState(s);
		}
		return "COMODO2 ERROR transforming " + s.getName() + " state!";
	}

	/**
	 * Transform a simple state.
	 */
	public CharSequence exploreSimpleState(final State s) {
		StringConcatenation str = new StringConcatenation();

		
		if (mQState.isFinal(s)) {
			// Final nodes do not exist in QM. Is there a need to handle them?
			// str.append(printFinalState(s));
			str.newLineIfNotEmpty();
		} else {

			str.append(" " + printStateStart(s));
			str.newLineIfNotEmpty();
			str.append("  " + exploreActions(s), "  ");
			str.newLineIfNotEmpty();
			str.append("  " + exploreTransitions(s), "  ");
			str.newLineIfNotEmpty();
			str.append(" " + printStateEnd());
			str.newLineIfNotEmpty();

		}


		return str;
	}

	/**
	 * Transform a composite state.
	 * A composite state can:
	 * - have orthogonal regions
	 * - have substates (including final pseudo-states)
	 * - have no substates
	 */
	public CharSequence exploreCompositeState(final State s) {
		StringConcatenation str = new StringConcatenation();
		
		
		/*if (s.isOrthogonal()) {
			str.append(exploreOrthogonalState(s));
			str.newLineIfNotEmpty();
		} else */
		if (!Iterables.isEmpty(mQState.getAllNonFinalSubstates(s))) {
			
			
			str.append(printStateStart(s));
			str.newLineIfNotEmpty();
			
			if (!Objects.equal(mQState.getInitialSubstateName(s), "")){
				str.append("  " + printInitial(mQState.getInitialSubstateName(s), "", s.getName()), "  ");
			}

			str.newLineIfNotEmpty();
			str.append("  " + exploreActions(s), "  ");
			str.newLineIfNotEmpty();
			str.append("  " + exploreTransitions(s), "  ");
			str.newLineIfNotEmpty();

			for(final State ss : mQState.getCompositeSubstates(s)) {
				str.newLine();
				str.append("  " + exploreCompositeState(ss), "  ");
				str.newLineIfNotEmpty();
			}
			for(final State ss_1 : mQState.getSimpleSubstates(s)) {
				str.newLine();
				str.append("  " + exploreSimpleState(ss_1), "  ");
				str.newLineIfNotEmpty();
			}
			for(final State ss_2 : mQState.getFinalSubstates(s)) {
				str.newLine();
				str.append("  " + exploreSimpleState(ss_2), "  ");
				str.newLineIfNotEmpty();
			}

			str.append(printStateEnd());
			str.newLineIfNotEmpty();
			
		
		} else {
			str.append(exploreSimpleState(s));
			str.newLineIfNotEmpty();
		}


		return str;
	}

	public CharSequence exploreActions(final State s) {
		String str = "";
		if ((mQState.hasOnEntryActions(s) || mQState.hasTimerTransition(s))) {
			str += printEntryActions(s);
		}		
		if (mQState.hasDoActivities(s)) {
			mLogger.warn("SKIPPED -- Do Activities are not supported in Quantum Modeler " +
			 				"(found in state " + s.getName() + ")");
		}
		if (mQState.hasOnExitActions(s) || mQState.hasTimerTransition(s)) {
			str += printExitActions(s);
		}
		return str;
	}

	public CharSequence exploreTransitions(final State s) {
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
				// String sourceName = mQTransition.getSourceName(t);
				String comodoId = UUID.randomUUID().toString();
				
				registerEvent(t);

				str.append(printTransition(t, comodoId));
				str.newLineIfNotEmpty();
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
				// signalEventsNameset.add(eventName);
			} else if (mQTransition.hasTimeEvent(t)) {
				// timeEventsNameset.add(eventName);
			}
		}
	}

	public String eventNaming(String timeEventName){
		return timeEventName.replaceAll("[^A-Za-z0-9_]", "").toUpperCase();
	}

	public CharSequence printTimeEvents(){
		String str = "";

		// for (final String eventName : timeEventsNameset){
			str += "<attribute name=\"" + "timeEvent"  + "\"  type=\"QTimeEvt\"/>";
		// }

		return str;
	}



	/**
	 * Prints the initial transition of a State machine
	 * @param targetName
	 * @return CharSequence
	 */
	public CharSequence printInitialState(final String targetName, String smName) {

		STGroup g = new STGroupFile("resources/qpc_tpl/StateMachineSource-state.stg");
		ST st = g.getInstanceOf("StateMachine_InitialState");

		st.add("smQualifiedName", smQualifiedName);

        return st.render();
	}
	








	/**
	 * Prints the initial transition of a State machine
	 * @param targetName
	 * @return CharSequence
	 */
	public CharSequence printInitial(final String targetName, String initCode, String initId) {

		String str = "    " + "<initial target=\"" + targetName + "\" comodoId=\"init" + initId + "\">\n";
		
		if (!Objects.equal(initCode, "")) {	
			str += "     " + "<action>";
			str += initCode + "</action>\n";
		}


		str += "    " + "</initial>\n";
		return str;
	}


	/**
	 * Prints the <tran> QM elements. Multiple possibilities from UML
	 *  -- Transition is simple (no guards)
	 *  -- Transition has a guard
	 *  -- Transition points to a UML choice node
	 * On top of that, we need to print the actions
	 */
	public CharSequence printTransition(final Transition t, final String transitionComodoId) {
		String eventName  = mQTransition.getFirstEventName(t); 
		String guard  = mQTransition.getResolvedGuardName(t);
		String targetName = mQTransition.getTargetName(t); 
		
		String str = "<tran";

		if (!Objects.equal(eventName, "")) {
			str += " trig=\"" + eventName + "\">\n";
		}


		if (mQTransition.isChoiceTransition(t)) {
			str += printChoices(t, transitionComodoId);

		} else if (!Objects.equal(guard, "")) {
			// A simple guard has to be translated into a choice node with a single option in QM.
			// If so, the target is inside the choice node and not in the <tran ...> tag
			String guardComodoId = UUID.randomUUID().toString();
			

			str += printChoiceNode(targetName, guard, "", guardComodoId);

		} else if (!Objects.equal(targetName, "")) {
			str = str.substring(0, str.length() - 2);
			str += " target=\"" + targetName + "\"";
			str += " comodoId=\"" + transitionComodoId + "\"";
			str += ">\n";
		}

		if (mQTransition.hasAction(t)){
			// Prints the name of the behavior as the code string
			str += printTransitionAction(checkTrailingSemicolon(mQTransition.getFirstActionName(t)));
		}

		str += str;

		
		return str;
	}

	/**
	 * Returns CharSequence of Choice transitions in QM format.
	 * In QM, choice nodes are special types of transitions, not pseudoStates like in UML.
	 */
	public CharSequence printChoices(final Transition t, final String transitionComodoId) {
		
		String str = "";
		Pseudostate choicePseudoState = (Pseudostate) t.getTarget();

		for (Transition choiceTransition : choicePseudoState.getOutgoings()){
			String guardComodoId = UUID.randomUUID().toString();
			
			
			String targetName = mQTransition.getTargetName(choiceTransition); 
			String guard  = mQTransition.getResolvedGuardName(choiceTransition);
			String action = mQTransition.getFirstActionName(choiceTransition);

			str += printChoiceNode(targetName, guard, action, guardComodoId);
		}

		return str;
	}
	
	public String printChoiceNode(String targetName, String guard, String action, String guardComodoId){
		String str = "";
		str += "<choice target=\"" + targetName + "\" comodoId=\"" + guardComodoId + "\">\n";
		str += " <guard>" + guard + "</guard>\n";
		if (!Objects.equal(action, "")) {
			str += "  <action>" + action + "</action>\n";
		}

		return str;
	}


	public CharSequence printEntryActions(final State s) {
		StringConcatenation str = new StringConcatenation();
		str.append("<entry brief=\"" + s.getEntry().getName() + "\">");

		// str.append(mQBehavior.getBehaviorCodeString(s.getEntry()));
		str.append(checkTrailingSemicolon(s.getEntry().getName()));

		str.append("</entry>\n");
		return str;
	}

	public CharSequence printExitActions(final State s) {
		StringConcatenation str = new StringConcatenation();
		str.append("<exit brief=\"" + s.getExit().getName() + "\">");

		//str.append(mQBehavior.getBehaviorCodeString(s.getExit()));
		str.append(checkTrailingSemicolon(s.getExit().getName()));

		str.append("</exit>\n");
		return str;
	}

	public CharSequence printTransitionAction(final String codeString) {
		return "<action>" + codeString + "</action>\n";
	}


	public CharSequence printStateStart(final State s) {
		return "<state name=\"" + mQState.getStateName(s) + "\">\n";
	}

	public CharSequence printStateEnd() {
		return  "</state>\n";
	}

	public CharSequence printFinalState(final State s) {
		// There's no Final State in QM
		// return "<final id=\"" + mQState.getStateName(s) + "\"/>\n";
		return "";
	}

	private String checkTrailingSemicolon(String str) {
		return str.endsWith(";") ? str : str + ";";
	}

	public CharSequence printFileTemplates(final String smName) {
		String str = " <directory name=\".\">\n";
		str += "  <file name=\"" + smName + ".h\">\n";
		str += "   <text>";
		str += printStateMachineHeaderTemplate(smName);
		str += "\n\n$declare${" + smName + "}</text>\n";
		str += "  </file>";
		
		str += "  <file name=\"" + smName + ".c\">\n";
		str += "   <text>";
		str += printStateMachineSourceTemplate(smName);
		str += "\n\n$define${" + smName + "}</text>\n";
		str += "  </file>\n";

		str += " </directory>\n";

		return str;
	}

	public String printInitialSignalSubscription(String smName){
		String str = "// Subscribe to all the signals to which this state machine needs to respond.\n";
		str += "if (me->active == (QActive *)me) {";

		// for (String signalName : this.signalEventsNameset){
		// 	str += "	QActive_subscribe(me->active, " + smName.toUpperCase() + "_" + signalName + "_SIG);\n";
		// }
		str += "}";
		return str;
	}


	public String printStateMachineSourceTemplate(final String smName){
        STGroup g = new STGroupFile("resources/qm_tpl/QMStateMachineSourceTpl.stg");
		ST st = g.getInstanceOf("StateMachineSource");
		st.add("smName", smName);
		st.add("smNameUpperCase", smName.toUpperCase());

        return st.render();
    }

	public String printStateMachineHeaderTemplate(final String smName){
        STGroup g = new STGroupFile("resources/qm_tpl/QMStateMachineHeaderTpl.stg");
		ST st = g.getInstanceOf("StateMachineHeader");
		st.add("smName", smName);
		st.add("smNameUpperCase", smName.toUpperCase());

        return st.render();
    }


}
