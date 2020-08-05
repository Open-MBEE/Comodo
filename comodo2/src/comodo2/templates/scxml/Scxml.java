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
import java.util.List;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

public class Scxml implements IGenerator {

	private static final Logger mLogger = Logger.getLogger(Main.class);

	@Inject
	@Extension
	private QStateMachine mQStateMachine;

	@Inject
	@Extension
	private QRegion mQRegion;

	@Inject
	@Extension
	private QState mQState;

	@Inject
	@Extension
	private QTransition mQTransition;

	@Inject
	@Extension
	private QClass mQClass;

	@Inject
	@Extension
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
		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			if ((mQClass.isToBeGenerated(e) && mQClass.hasStateMachines(e))) {
				Iterable<StateMachine> _stateMachines = mQClass.getStateMachines(e);
				for (final StateMachine sm : _stateMachines) {
					mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toScxmlFilePath(sm.getName())));
					fsa.generateFile(mFilesHelper.toScxmlFilePath(sm.getName()), this.generate(sm));
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
		return str;
	}

	/**
	 * Transform a simple or composite state.
	 */
	public CharSequence exploreState(final State s) {
		if (s.isSimple()) {
			return exploreSimpleState(s);
		} else {
			if (s.isComposite()) {
				return exploreCompositeState(s);
			}
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
		} else if (!IterableExtensions.isEmpty(mQState.getAllNonFinalSubstates(s))) {
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
		final Function1<Region, Boolean> _function = (Region e) -> {
			Element _owner = e.getOwner();
			return Boolean.valueOf(Objects.equal(_owner, s));
		};
		Iterable<Region> _filter = IterableExtensions.<Region>filter(Iterables.<Region>filter(s.allOwnedElements(), Region.class), _function);
		for(final Region r : _filter) {
			str.append("  ");
			str.append("<state id=\"" + mQRegion.getRegionName(r) + "\">");
			str.newLineIfNotEmpty();
			str.append("    " + printInitial(mQRegion.getInitialStateName(r)), "    ");
			str.newLineIfNotEmpty();
			final Function1<State, Boolean> _function_1 = (State e1) -> {
				Element _owner = e1.getOwner();
				return Boolean.valueOf(Objects.equal(_owner, r));
			};
			Iterable<State> _filter_1 = IterableExtensions.<State>filter(Iterables.<State>filter(r.allOwnedElements(), State.class), _function_1);
			for(final State substate : _filter_1) {
				str.newLine();
				str.append("    " + exploreState(substate), "\t");
				str.newLineIfNotEmpty();
			}
			str.newLine();
			str.append("  " + printStateEnd(), "  ");
			str.newLineIfNotEmpty();
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
		for(final Pseudostate hs : mQState.getHistory(s)) {
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
		StringConcatenation _builder = new StringConcatenation();
		{
			final Function1<Transition, String> _function = (Transition e) -> {
				return e.getName();
			};
			List<Transition> _sortBy = IterableExtensions.<Transition, String>sortBy(s.getOutgoings(), _function);
			for(final Transition t : _sortBy) {
				{
					boolean _isMalformed = mQTransition.isMalformed(t);
					if (_isMalformed) {
						String _stateName = mQState.getStateName(s);
						String _plus = ("Internal transition from state " + _stateName);
						String _plus_1 = (_plus + " has no trigger event and no guard, skipped since could introduce infinite loop!");
						Scxml.mLogger.warn(_plus_1);
						_builder.newLineIfNotEmpty();
					} else {
						CharSequence _printTransitionStart = this.printTransitionStart(mQTransition.getFirstEventName(t), mQTransition.getResolvedGuardName(t), mQTransition.getTargetName(t), mQTransition.hasAction(t));
						_builder.append(_printTransitionStart);
						_builder.newLineIfNotEmpty();
						{
							boolean _hasAction = mQTransition.hasAction(t);
							if (_hasAction) {
								_builder.append("  ");
								CharSequence _printAction = this.printAction(mQTransition.getFirstActionName(t));
								_builder.append(_printAction, "  ");
								_builder.newLineIfNotEmpty();
								CharSequence _printTransitionEnd = this.printTransitionEnd();
								_builder.append(_printTransitionEnd);
								_builder.newLineIfNotEmpty();
							}
						}
					}
				}
			}
		}
		return _builder;
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

		final Function1<Transition, String> _function = (Transition e) -> {
			return e.getName();
		};
		List<Transition> _sortBy = IterableExtensions.<Transition, String>sortBy(s.getOutgoings(), _function);
		for (final Transition t : _sortBy) {
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

		final Function1<Transition, String> _function = (Transition e) -> {
			return e.getName();
		};
		List<Transition> _sortBy = IterableExtensions.<Transition, String>sortBy(s.getOutgoings(), _function);
		for (final Transition t : _sortBy) {
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
		return 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<scxml xmlns=\"http://www.w3.org/2005/07/scxml\" xmlns:customActionDomain=\"http://my.custom-actions.domain/CUSTOM\" version=\"1.0\" initial=\"" +
				mQStateMachine.getInitialStateName(sm) + "\">\n";
	}

	public CharSequence printStateMachineEnd() {
		return "</scxml>\n";
	}
}
