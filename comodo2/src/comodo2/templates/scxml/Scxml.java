package comodo2.templates.scxml;

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

public class Scxml implements IGenerator {

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
	 * into an SCXML document.
	 * 
	 * The UML Class should:
	 * - be inside a UML Package with stereotype cmdoModule
	 * - the cmdoModule name should have been provided in the configuration
	 * - have stereotype cmdoComponent
	 * - have an associated UML State Machine
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
/*
		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			if ((mQClass.isToBeGenerated(e) && mQClass.hasStateMachines(e))) {
				for (final StateMachine sm : mQClass.getStateMachines(e)) {
					mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toScxmlFilePath(sm.getName())));
					fsa.generateFile(mFilesHelper.toScxmlFilePath(sm.getName()), this.generate(sm));
				}
			}
		}
*/		
		
		final TreeIterator<EObject> allContents = input.getAllContents();
		while (allContents.hasNext()) {
			EObject e = allContents.next();
			if (e instanceof org.eclipse.uml2.uml.Class) {
				org.eclipse.uml2.uml.Class c = (org.eclipse.uml2.uml.Class)e; 
				if ((mQClass.isToBeGenerated(c) && mQClass.hasStateMachines(c))) {
					for (final StateMachine sm : mQClass.getStateMachines(c)) {
						mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toScxmlFilePath(sm.getName())));
						fsa.generateFile(mFilesHelper.toScxmlFilePath(sm.getName()), this.generate(sm));
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
		/*
		final Function1<State, Boolean> _function = (State e) -> {
			return Boolean.valueOf(mQState.isTopState(e));
		};
		final Function1<State, String> _function_1 = (State e) -> {
			return e.getName();
		};
		List<State> _sortBy = IterableExtensions.<State, String>sortBy(IterableExtensions.<State>filter(mQStateMachine.getAllStates(sm), _function), _function_1);
		for(final State s : _sortBy) {
			str.append(exploreState(s));
			str.newLineIfNotEmpty();
		}
		 */
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
		if (s.isOrthogonal()) {
			str.append(exploreOrthogonalState(s));
			str.newLineIfNotEmpty();
		} else if (!Iterables.isEmpty(mQState.getAllNonFinalSubstates(s))) {
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
			if (mQState.hasHistory(s)) {
				str.newLine();
				str.append("  " + exploreHistoryState(s), "  ");
				str.newLineIfNotEmpty();
			}
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

	public CharSequence exploreOrthogonalState(final State s) {
		StringConcatenation str = new StringConcatenation();
		str.append("<parallel id=\"" + mQState.getStateName(s) + "\">");
		str.newLineIfNotEmpty();
	/*	
		final Function1<Region, Boolean> _function = (Region e) -> {
			Element _owner = e.getOwner();
			return Boolean.valueOf(Objects.equal(_owner, s));
		};
		Iterable<Region> _filter = IterableExtensions.<Region>filter(Iterables.<Region>filter(s.allOwnedElements(), Region.class), _function);
	*/
		Iterable<Region> regions = Iterables.<Region>filter(s.allOwnedElements(), Region.class);
		for(final Region r : regions) {
			if (r.getOwner() == s) {
				str.append("  ");
				str.append("<state id=\"" + mQRegion.getRegionName(r) + "\">");
				str.newLineIfNotEmpty();
				str.append("    " + printInitial(mQRegion.getInitialStateName(r)), "    ");
				str.newLineIfNotEmpty();
				/*
				final Function1<State, Boolean> _function_1 = (State e1) -> {
					Element _owner = e1.getOwner();
					return Boolean.valueOf(Objects.equal(_owner, r));
				};
				Iterable<State> _filter_1 = IterableExtensions.<State>filter(Iterables.<State>filter(r.allOwnedElements(), State.class), _function_1);
				*/
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
	}

	public CharSequence exploreHistoryState(final State s) {
		String str = "";
		for (final Pseudostate hs : mQState.getHistory(s)) {
			str += "<history id=\"" + mQRegion.getRegionName(hs.getContainer()) + ":" +  hs.getName() + "\" type=\"" + mQState.getHistoryTypeName(hs) + "\">\n";
			str += "  <transition target=\"" + mQRegion.getRegionName(hs.getOutgoings().get(0).getTarget().getContainer()) + ":" + 
					hs.getOutgoings().get(0).getTarget().getName() + "\"/>\n";
			str += "</history>\n";
		}
		return str;
	}

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
		/*
		final Function1<Transition, String> _function = (Transition e) -> {
			return e.getName();
		};
		List<Transition> _sortBy = IterableExtensions.<Transition, String>sortBy(s.getOutgoings(), _function);
		*/
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
				str.append(printTransitionStart(mQTransition.getFirstEventName(t), mQTransition.getResolvedGuardName(t), mQTransition.getTargetName(t), mQTransition.hasAction(t)));
				str.newLineIfNotEmpty();
				if (mQTransition.hasAction(t)) {
					str.append("  " + printAction(mQTransition.getFirstActionName(t)), "  ");
					str.newLineIfNotEmpty();
					str.append(printTransitionEnd());
					str.newLineIfNotEmpty();
				}
			}
		}
		return str;
	}

	public CharSequence printInitial(final String name) {
		String str = "<initial>\n";
		str += "  <transition target=\"" + name + "\"/>\n";
		str += "</initial>\n";
		return str;
	}

	public CharSequence printTransitionStart(final String eventName, final String guardName, final String targetName, final boolean hasAction) {
		String str = "<transition";
		if (!Objects.equal(eventName, "")) {
			str += " event=\"" + eventName + "\"";
		}
		if (!Objects.equal(guardName, "")) {
			str += " cond=\"" + guardName + "\"";
		}
		if (!Objects.equal(targetName, "")) {
			str += " target=\"" + targetName + "\"";
		}
		if (hasAction) {
			str += ">";
		} else {
			str += "/>";
		}
		return str;
	}

	public CharSequence printTransitionEnd() {
		return "</transition>\n";
	}

	public CharSequence printEntryActions(final State s) {
		StringConcatenation str = new StringConcatenation();
		str.append("<onentry>");
		str.newLine();
		str.append("  ");
		str.append(printAction(s.getEntry().getName()), "  ");
		str.newLineIfNotEmpty();
		/*
		final Function1<Transition, String> _function = (Transition e) -> {
			return e.getName();
		};
		List<Transition> _sortBy = IterableExtensions.<Transition, String>sortBy(s.getOutgoings(), _function);
		for (final Transition t : _sortBy) {
			if (mQTransition.isTimerTransitionWithEvent(t)) {
				str.append("<send target=\"\" type=\"scxml\" sendid=\"\'" + mQState.getStateName(s) + "_" + mQTransition.getEventName(t));
				str.append("\'\" event=\"\'" + mQTransition.getEventName(t) + "\'\" delay=\"\'" + mQTransition.getTimeEventDuration(t) + "\'\"/>");
				str.newLineIfNotEmpty();
			}
		}
		 */
		for (final Transition t : s.getOutgoings()) {
			if (mQTransition.isTimerTransitionWithEvent(t)) {
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

		/*
		final Function1<Transition, String> _function = (Transition e) -> {
			return e.getName();
		};
		List<Transition> _sortBy = IterableExtensions.<Transition, String>sortBy(s.getOutgoings(), _function);
		for (final Transition t : _sortBy) {
			if (mQTransition.isTimerTransitionWithEvent(t)) {
				str.append("<cancel sendid=\"\'");
				str.append(mQState.getStateName(s));
				str.append("_");
				str.append(mQTransition.getEventName(t));
				str.append("\'\"/>");
				str.newLineIfNotEmpty();
			}
		}
		 */
		for (final Transition t : s.getOutgoings()) {
			if (mQTransition.isTimerTransitionWithEvent(t)) {
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

	public CharSequence printAction(final String name) {
		return "<customActionDomain:" + name + " name=\"" + name + "\"/>\n";
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

	public CharSequence printStateMachineStart(final StateMachine sm) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<scxml xmlns=\"http://www.w3.org/2005/07/scxml\" xmlns:customActionDomain=\"http://my.custom-actions.domain/CUSTOM\" version=\"1.0\" initial=\"" +
				mQStateMachine.getInitialStateName(sm) + "\">\n";
	}

	public CharSequence printStateMachineEnd() {
		return "</scxml>\n";
	}
}
