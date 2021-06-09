package comodo2.templates.qf.qm;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import comodo2.engine.Main;
import comodo2.queries.QClass;
import comodo2.queries.QRegion;
import comodo2.queries.QState;
import comodo2.queries.QStateMachine;
import comodo2.queries.QTransition;
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
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

public class Qm implements IGenerator {

	private static final Logger mLogger = Logger.getLogger(Main.class);

	@Inject
	private QStateMachine mQStateMachine;

	@Inject
	private QRegion mQRegion;

	@Inject
	private QState mQState;

	@Inject
	private QTransition mQTransition;

	@Inject
	private QClass mQClass;

	@Inject
	private FilesHelper mFilesHelper;

	/**
	 * Transform UML State Machine associated to a class (classifier behavior)
	 * into a Quantum Framework XML file for the Quantum Modeler.
	 * This file was based off of ../scxml/Scxml.java and modified to fit QM files
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
						mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toQmFilePath(sm.getName())));
						fsa.generateFile(mFilesHelper.toQmFilePath(sm.getName()), this.generate(sm));
					}
				}				
			}
		}
	}

	public CharSequence generate(final StateMachine sm) {
		StringConcatenation str = new StringConcatenation();
		str.append(printStateMachineStart(sm));
		str.newLineIfNotEmpty();
		str.append("  " + exploreTopStates(sm), "  ");
		str.newLineIfNotEmpty();
		str.append(printStateMachineEnd());
		str.newLineIfNotEmpty();
		return str;
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
			str.append(printFinalState(s));
			str.newLineIfNotEmpty();
		} else {
			str.append(printStateStart(s));
			str.newLineIfNotEmpty();
			str.append("  " + exploreActions(s), "  ");
			str.newLineIfNotEmpty();
			str.append("  " + exploreTransitions(s), "  ");
			str.newLineIfNotEmpty();
			str.append(printStateEnd());
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
			str.append("  " + printInitial(mQState.getInitialSubstateName(s)), "  ");
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
			/*if (mQState.hasHistory(s)) {
				str.newLine();
				str.append("  " + exploreHistoryState(s), "  ");
				str.newLineIfNotEmpty();
			}*/
			str.append("  " + exploreActions(s), "  ");
			str.newLineIfNotEmpty();
			str.append("  " + exploreTransitions(s), "  ");
			str.newLineIfNotEmpty();
			str.append(printStateEnd());
			str.newLineIfNotEmpty();
		} else {
			str.append(exploreSimpleState(s));
			str.newLineIfNotEmpty();
		}
		return str;
	}

/*	public CharSequence exploreOrthogonalState(final State s) {
		StringConcatenation str = new StringConcatenation();
		str.append("<parallel id=\"" + mQState.getStateName(s) + "\">");
		str.newLineIfNotEmpty();

		Iterable<Region> regions = Iterables.<Region>filter(s.allOwnedElements(), Region.class);
		for(final Region r : regions) {
			if (r.getOwner() == s) {
				str.append("  ");
				str.append("<state id=\"" + mQRegion.getRegionName(r) + "\">");
				str.newLineIfNotEmpty();
				str.append("    " + printInitial(mQRegion.getInitialStateName(r)), "    ");
				str.newLineIfNotEmpty();

				Iterable<State> substates = Iterables.<State>filter(r.allOwnedElements(), State.class);			
				for(final State substate : substates) {
					if (Objects.equal(substate.getOwner(), r)) {
						str.newLine();
						str.append("    " + exploreState(substate), "\t");
						str.newLineIfNotEmpty();
					}
				}
				str.newLine();
				str.append("  " + printStateEnd(), "  ");
				str.newLineIfNotEmpty();
			}
		}
				
		if (mQState.hasHistory(s)) {
			str.append(exploreHistoryState(s));
			str.newLineIfNotEmpty();
		}
		str.append("  " + exploreActions(s), "  ");
		str.newLineIfNotEmpty();
		str.append("  " + exploreTransitions(s), "  ");
		str.newLineIfNotEmpty();
		str.append("</parallel>");
		str.newLine();
		return str;
	}*/

/*	public CharSequence exploreHistoryState(final State s) {
		String str = "";
		for (final Pseudostate hs : mQState.getHistory(s)) {
			str += "<history id=\"" + mQRegion.getRegionName(hs.getContainer()) + ":" +  hs.getName() + "\" type=\"" + mQState.getHistoryTypeName(hs) + "\">\n";
			str += "  <transition target=\"" + mQRegion.getRegionName(hs.getOutgoings().get(0).getTarget().getContainer()) + ":" + 
					hs.getOutgoings().get(0).getTarget().getName() + "\"/>\n";
			str += "</history>\n";
		}
		return str;
	}*/

	public CharSequence exploreActions(final State s) {
		String str = "";
		if ((mQState.hasOnEntryActions(s) || mQState.hasTimerTransition(s))) {
			str += printEntryActions(s);
		}		
		if (mQState.hasDoActivities(s)) {
			str += printDoActivities(s);
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
				str.append(printTransition(t));
				str.newLineIfNotEmpty();
			}
		}
		return str;
	}

	public CharSequence printStateMachineStart(final StateMachine sm) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
					"<model version=\"5.1.1\" links=\"1\">\n" + 
					" <framework name=\"qpc\"/>";
	}

	public CharSequence printStateMachineEnd() {
		return "</model>\n";
	}







	public CharSequence printInitial(final String name) {
		// TODO 
		// this does not go through the very first initial node because it is a pseudostate
		// need to have a function that returns top states AND initial pseudo state, not just top states.
		String target_relative_path = "../1";
		String init_c_code = "";

		String str = "<initial target =" + target_relative_path + ">\n";
		str += " <action brief=\"" + name + "\">";
		str += init_c_code + "</action>\n";
		str += printInitialGlyph();
		str += "</initial>\n";
		return str;
	}

	public CharSequence printInitialGlyph() {
		String conn = "conn_placeholder";
		String box = "box_placeholder";

		String str = "<initial_glyph conn=\"" + conn + "\">\n";
		str += "  <action box=\"" + box + "\"/>\n";
		str += "</initial_glyph>\n";
		return str;
	}

	public CharSequence printTransition(final Transition t) {
		String eventName  = mQTransition.getFirstEventName(t); 
		String guardName  = mQTransition.getResolvedGuardName(t);
		String targetName = mQTransition.getTargetName(t); 

		String str = "<tran";
		if (!Objects.equal(eventName, "")) {
			str += " trig=\"" + eventName + "\"";
		}
		if (!Objects.equal(guardName, "")) {
			str += " cond=\"" + guardName + "\"";
		}
		if (!Objects.equal(targetName, "")) {
			str += " target=\"" + targetName + "\"";
		}
		str += ">";

		if (mQTransition.hasAction(t)){
			// printAction expects a code string instead of a name. Look into uml.Transition.getEffect()
			str += printAction(t.getName()); //TODO
		}

		str += printTransitionGlyph();

		str += "</tran>";
		return str;
	}

	public CharSequence printTransitionGlyph() {
		String conn = "conn_placeholder";
		String box = "box_placeholder";

		String str = "<initial_glyph conn=" + conn + ">\n";
		str += "  <action box=\"" + box + "\"/>\n";
		str += "</initial_glyph>\n";
		return str;
	}

	public CharSequence printEntryActions(final State s) {
		StringConcatenation str = new StringConcatenation();
		str.append("<onentry>");
		str.newLine();
		str.append("  ");
		str.append(printAction(s.getEntry().getName()), "  ");
		str.newLineIfNotEmpty();

		for (final Transition t : s.getOutgoings()) {
			if (mQTransition.isTimerTransition(t)) {
				str.append("<send target=\"\" type=\"scxml\" sendid=\"\'" + mQState.getStateName(s) + "_" + mQTransition.getEventName(t));
				str.append("\'\" event=\"\'" + mQTransition.getEventName(t) + "\'\" delay=\"\'" + mQTransition.getTimeEventDuration(t) + "\'\"/>");
				str.newLineIfNotEmpty();
			}
		}

		str.append("</onentry>\t");
		str.newLine();
		return str;
	}

	public CharSequence printExitActions(final State s) {
		StringConcatenation str = new StringConcatenation();
		str.append("<onexit>");
		str.newLine();
		str.append("  " + printAction(s.getExit().getName()), "  ");

		for (final Transition t : s.getOutgoings()) {
			if (mQTransition.isTimerTransition(t)) {
				str.append("<cancel sendid=\"\'");
				str.append(mQState.getStateName(s));
				str.append("_");
				str.append(mQTransition.getEventName(t));
				str.append("\'\"/>");
				str.newLineIfNotEmpty();
			}
		}

		str.append("</onexit>");
		str.newLine();
		return str;
	}

	public CharSequence printAction(final String codeString) {
		return "<action>" + codeString + "</action>\n";
	}

	public CharSequence printDoActivities(final State s) {
		return "<invoke id=\"" + s.getDoActivity().getName() + "\"/>\n";
	}

	public CharSequence printStateStart(final State s) {
		return "<state id=\"" + mQState.getStateName(s) + "\">\n";
	}

	public CharSequence printStateEnd() {
		return "</state>\n";
	}

	public CharSequence printFinalState(final State s) {
		return "<final id=\"" + mQState.getStateName(s) + "\"/>\n";
	}

}
