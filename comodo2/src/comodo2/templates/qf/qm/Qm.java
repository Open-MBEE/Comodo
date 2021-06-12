package comodo2.templates.qf.qm;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import comodo2.engine.Main;
import comodo2.queries.QBehavior;
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
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

public class Qm implements IGenerator {

	private static final Logger mLogger = Logger.getLogger(Main.class);

	private static final TreeSet<String> timeEventsNameset = new TreeSet<String>();

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

	@Inject
	private QBehavior mQBehavior;

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
						mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toQmFilePath(sm.getName())));
						fsa.generateFile(mFilesHelper.toQmFilePath(sm.getName()), this.generate(sm));
					}
				}				
			}
		}
	}

	public CharSequence generate(final StateMachine sm) {
		StringConcatenation str = new StringConcatenation();
		StringConcatenation temp_str = new StringConcatenation();

		str.append(printDocumentStart());
		str.newLineIfNotEmpty();
		str.append(" <package name=\"" + sm.getName() + "\">\n");
		str.append("  <class name=\"" + sm.getName() + "\" superclass=\"qpc::QActive\">\n");

		// We need a temp string for now because the TimeEvent elements needs to be declared before the statechart
		// in the XLM document. But these only get registered when registering the states in which they are used.
		// Later, the plan will be to make use of the javax.xml.parsers libraries for this process to be more robust and understandable.
		temp_str.append("   <statechart>\n");
		temp_str.append(printInitial(mQStateMachine.getInitialStateName(sm)));
		temp_str.newLineIfNotEmpty();
		temp_str.append("  " + exploreTopStates(sm), "  ");
		temp_str.newLineIfNotEmpty();
		
		str.append(printTimeEvents());
		str.newLineIfNotEmpty();
		str.append(temp_str);
		
		str.append("<state_diagram size=\"80,50\"/>\n</statechart>\n");
		str.append("  </class>\n </package>\n");
		str.append(printDocumentEnd());
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
			str.append(" " + printStateStart(s));
			str.newLineIfNotEmpty();
			str.append("  " + exploreActions(s), "  ");
			str.newLineIfNotEmpty();
			str.append("  " + exploreTransitions(s), "  ");
			str.newLineIfNotEmpty();
			str.append(" " + printStateGlyph());
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
			registerTimeEvent(t);
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

	public void registerTimeEvent(Transition t){
		String eventName  = mQTransition.getFirstEventName(t);

		if (!Objects.equal(eventName, "")) {
			// Removing non alphanumeric characters since this will be the name of a C variable
			timeEventsNameset.add(eventName.replaceAll("[^A-Za-z0-9]", "").toUpperCase());
		}
	}

	public CharSequence printTimeEvents(){
		String str = "";

		for (final String eventName : timeEventsNameset){
			str += "<attribute name=\"" + eventName + "\"  type=\"QTimeEvt\"/>";
		}

		return str;
	}


	public CharSequence printDocumentStart() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
					"<model version=\"5.1.1\" links=\"1\">\n" + 
					" <framework name=\"qpc\"/>";
	}

	public CharSequence printDocumentEnd() {
		return "</model>\n";
	}







	public CharSequence printInitial(final String targetName) {
		String target_relative_path = "../1";
		String init_code = "";

		String str = "    " + "<initial target=\"" + target_relative_path + "\">\n";
		
		if (!Objects.equal(init_code, "")) {	
			str += "     " + "<action>";
			str += init_code + "</action>\n";
		}

		str += printInitialGlyph();
		str += "    " + "</initial>\n";
		return str;
	}

	public CharSequence printInitialGlyph() {
		String conn = "10,10,0,0,5,5"; // placeholder
		String box = "15,15,10,3"; // placeholder

		String str = "     " + "<initial_glyph conn=\"" + conn + "\">\n";
		str += "      " + "<action box=\"" + box + "\"/>\n";
		str += "     " + "</initial_glyph>\n";
		return str;
	}

	public CharSequence printTransition(final Transition t) {
		String eventName  = mQTransition.getFirstEventName(t); 
		String guardName  = mQTransition.getResolvedGuardName(t);
		String targetName = mQTransition.getTargetName(t); 

		String str = "<tran";
		if (!Objects.equal(eventName, "")) {
			str += " trig=\"" + eventName.replaceAll("[^A-Za-z0-9]", "").toUpperCase() + "\"";
		}
		if (!Objects.equal(guardName, "")) {
			str += " cond=\"" + guardName + "\"";
		}
		if (!Objects.equal(targetName, "")) {
			// TODO: QM expects a relative numeric path instead of a name
			targetName = "../0"; // placeholder
			str += " target=\"" + targetName + "\"";
		}
		str += ">\n";

		if (mQTransition.hasAction(t)){
			// printAction expects a code string instead of a name. Look into uml.Transition.getEffect()
			str += printAction(t.getName()); //TODO
		}

		str += printTransitionGlyph();

		str += "</tran>";
		return str;
	}

	public CharSequence printTransitionGlyph() {
		String conn = "10,10,0,0,5,5"; // placeholder
		String box = "15,15,10,3"; // placeholder

		String str = " <tran_glyph conn=\"" + conn + "\">\n";
		str += "  <action box=\"" + box + "\"/>\n";
		str += " </tran_glyph>\n";
		return str;
	}

	public CharSequence printEntryActions(final State s) {
		StringConcatenation str = new StringConcatenation();
		str.append("<entry brief=\"" + s.getEntry().getName() + "\">");

		str.append(mQBehavior.getBehaviorCodeString(s.getEntry()));

		str.append("</entry>\n");
		return str;
	}

	public CharSequence printExitActions(final State s) {
		StringConcatenation str = new StringConcatenation();
		str.append("<exit brief=\"" + s.getEntry().getName() + "\">");

		str.append(mQBehavior.getBehaviorCodeString(s.getExit()));

		str.append("</exit>\n");
		return str;
	}

	public CharSequence printAction(final String codeString) {
		return "<action>" + codeString + "</action>\n";
	}

	// Quantum Modeler does not have Do Activities, only Entry and Exit
	public CharSequence printDoActivities(final State s) {
		return "<entry>" + s.getDoActivity().getName() + "</entry>\n";
	}

	public CharSequence printStateStart(final State s) {
		return "<state name=\"" + mQState.getStateName(s) + "\">\n";
	}

	public CharSequence printStateEnd() {
		return "</state>\n";
	}

	public CharSequence printStateGlyph() {
		String node = "10,10,0,0"; // placeholder
		String box = "15,15,10,3"; // placeholder

		String str = "<state_glyph node=\"" + node + "\">\n";
		str += "  <entry box=\"" + box + "\"/>\n";
		str += "</state_glyph>\n";
		return str;
	}

	public CharSequence printFinalState(final State s) {
		return "<final id=\"" + mQState.getStateName(s) + "\"/>\n";
	}


}
