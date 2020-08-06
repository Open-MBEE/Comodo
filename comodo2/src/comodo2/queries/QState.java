package comodo2.queries;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import comodo2.engine.Config;
import comodo2.queries.QRegion;
import comodo2.queries.QTransition;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

public class QState {
	@Inject
	@Extension
	private QRegion mQRegion;

	@Inject
	@Extension
	private QTransition mQTransition;

	public Region getParentRegion(final State s) {
		return s.getContainer();
	}

	public String getFullyQualifiedName(final State s) {
		if ((s == null)) {
			return "";
		}
		if (isTopState(s)) {
			return s.getName();
		}
		if (s.getOwner() == null) {
			return s.getName();
		}
		return (mQRegion.getFullyQualifiedName(this.getParentRegion(s)) + ":" + s.getName());
	}

	/**
	 * This function returns the [fully qualified] name of a given state.
	 * 
	 * @param s State to check.
	 * @return Name of the state.
	 */
	public String getStateName(final State s) {
		if (Config.getInstance().generateFullyQualifiedStateNames()) {
			return this.getFullyQualifiedName(s);
		}
		return s.getName();
	}

	public String getStateName(final Pseudostate ps) {
		if (Config.getInstance().generateFullyQualifiedStateNames()) {
			return (mQRegion.getFullyQualifiedName(ps.getContainer()) + ":" + ps.getName());
		}
		return ps.getName();
	}

	public String getHistoryTypeName(final Pseudostate ps) {
		if (Objects.equal(ps.getKind(), PseudostateKind.SHALLOW_HISTORY_LITERAL)) {
			return "shallow";
		}
		return "deep";
	}

	public State getParentState(final Pseudostate s) {
		return s.getContainer().getState();
	}

	public State getParentState(final State s) {
		return s.getContainer().getState();
	}

	public Iterable<State> getAllNonFinalSubstates(final State s) {
		final Function1<State, Boolean> _function = (State e) -> {
			return Boolean.valueOf((!isFinal(e)));
		};
		return IterableExtensions.<State>filter(Iterables.<State>filter(s.allOwnedElements(), State.class), _function);
	}

	public Iterable<State> getAllFinalSubstates(final State s) {
		final Function1<State, Boolean> _function = (State e) -> {
			return Boolean.valueOf(this.isFinal(e));
		};
		return IterableExtensions.<State>filter(Iterables.<State>filter(s.allOwnedElements(), State.class), _function);
	}

	public String getInitialSubstateName(final State s) {
		Iterable<Pseudostate> _filter = Iterables.<Pseudostate>filter(s.allOwnedElements(), Pseudostate.class);
		for (final Pseudostate ps : _filter) {
			if ((((Objects.equal(ps.getKind(), PseudostateKind.INITIAL_LITERAL) && 
					Objects.equal(ps.getContainer().getOwner(), s)) && 
					(ps.getOutgoings().size() == 1)) && 
					(IterableExtensions.<Transition>head(ps.getOutgoings()).getTarget() != null))) {
				return getStateName(((State) IterableExtensions.<Transition>head(ps.getOutgoings()).getTarget()));
			}
		}
		return "";
	}

	public Iterable<State> getCompositeSubstates(final State s) {
		final Function1<State, Boolean> _function = (State e) -> {
			return Boolean.valueOf((e.isComposite() && Objects.equal(this.getParentState(e), s)));
		};
		return IterableExtensions.<State>filter(Iterables.<State>filter(s.allOwnedElements(), State.class), _function);
	}

	public Iterable<State> getSimpleSubstates(final State s) {
		final Function1<State, Boolean> _function = (State e) -> {
			return Boolean.valueOf(((!e.isComposite()) && Objects.equal(this.getParentState(e), s)));
		};
		return IterableExtensions.<State>filter(Iterables.<State>filter(this.getAllNonFinalSubstates(s), State.class), _function);
	}

	public Iterable<State> getFinalSubstates(final State s) {
		final Function1<State, Boolean> _function = (State e) -> {
			return Boolean.valueOf((((!e.isComposite()) && Objects.equal(this.getParentState(e), s)) && this.isFinal(e)));
		};
		return IterableExtensions.<State>filter(Iterables.<State>filter(s.allOwnedElements(), State.class), _function);
	}

	public Iterable<Pseudostate> getHistory(final State s) {
		if (s.isComposite() == false) {
			return null;
		}
		final Function1<Pseudostate, Boolean> _function = (Pseudostate e) -> {
			return Boolean.valueOf(this.isHistoryState(e));
		};
		final Iterable<Pseudostate> hs = IterableExtensions.<Pseudostate>filter(Iterables.<Pseudostate>filter(s.allOwnedElements(), Pseudostate.class), _function);
		if (IterableExtensions.isEmpty(hs)) {
			return null;
		}
		final Function1<Pseudostate, Boolean> _function_1 = (Pseudostate e) -> {
			return Boolean.valueOf(Objects.equal(getParentState(e), s));
		};
		return IterableExtensions.<Pseudostate>filter(hs, _function_1);
	}

	public boolean isInitalState(final Pseudostate ps) {
		return Objects.equal(ps.getKind(), PseudostateKind.INITIAL_LITERAL);
	}

	public boolean isFinalState(final Pseudostate ps) {
		return Objects.equal(ps.getKind(), PseudostateKind.TERMINATE_LITERAL);
	}

	public boolean isHistoryState(final Pseudostate ps) {
		return (Objects.equal(ps.getKind(), PseudostateKind.DEEP_HISTORY_LITERAL) || 
				Objects.equal(ps.getKind(), PseudostateKind.SHALLOW_HISTORY_LITERAL));
	}

	/**
	 * This function checks whether a given state is at the top level
	 * in the hierarchy of a states (the state has no parent state).
	 * 
	 * @param s State to check.
	 * @return True if the state is at the top level, false otherwise.
	 */
	public boolean isTopState(final State s) {
		if ((s == null)) {
			return true;
		}
		return Objects.equal(s.getContainer().getOwner(), s.containingStateMachine());
	}

	/**
	 * This function checks whether the state is a final pseudo-state.
	 * Note that a composite state should not be final.
	 * 
	 * @param s State to check.
	 * @return True if the state is a final pseudo-state, false otherwise.
	 */
	public boolean isFinal(final State s) {
		if (s.isComposite()) {
			return false;
		}
		return (s instanceof FinalState);
	}

	public boolean hasOnEntryActions(final State s) {
		return (s.getEntry() != null);
	}

	public boolean hasOnExitActions(final State s) {
		return (s.getExit() != null);
	}

	public boolean hasDoActivities(final State s) {
		return (s.getDoActivity() != null);
	}

	public boolean hasTimerTransition(final State s) {
		if (getParentState(s) == null) {
			return false;
		}
		final Function1<Transition, Boolean> _function = (Transition e) -> {
			return Boolean.valueOf((mQTransition.isTimerTransition(e) && e.getSource().getName().matches(s.getName())));
		};
		return !IterableExtensions.isEmpty(IterableExtensions.<Transition>filter(Iterables.<Transition>filter(this.getParentState(s).allOwnedElements(), Transition.class), _function));
	}

	public boolean hasHistory(final State s) {
		if (s.isComposite() == false) {
			return false;
		}
		final Function1<Pseudostate, Boolean> _function = (Pseudostate ps) -> {
			return Boolean.valueOf((isHistoryState(ps) == true));
		};
		return !IterableExtensions.isEmpty(IterableExtensions.<Pseudostate>filter(Iterables.<Pseudostate>filter(s.allOwnedElements(), Pseudostate.class), _function));
	}
}
