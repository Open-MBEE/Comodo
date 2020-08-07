package comodo2.queries;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import java.util.TreeSet;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

public class QStateMachine {
	@Inject
	private QState mQState;

	@Inject
	private QTransition mQTransition;

	/**
	 * This function returns the name a state machine.
	 * 
	 * @param sm State machine.
	 * @return The name of the given state machine.
	 */
	public String getName(final QStateMachine sm) {
		return getName(sm);
	}

	/**
	 * This method looks for the initial state of a State Machine.
	 * It takes the "initial" pseudo-state (kind == "initial")
	 * at the level of the SM (container.owner == sm)
	 * and returns the destination state of the associated
	 * transition.
	 * @param sm State Machine
	 * @return The name of initial state if it exists, "" otherwise.
	 */
	public String getInitialStateName(final StateMachine sm) {
		for (final Pseudostate ps : Iterables.<Pseudostate>filter(sm.allOwnedElements(), Pseudostate.class)) {
			if (((((ps.getKind() == PseudostateKind.INITIAL_LITERAL) && 
					Objects.equal(ps.getContainer().getOwner(), sm)) && 
					(ps.getOutgoings().size() == 1)) && 
					(IterableExtensions.<Transition>head(ps.getOutgoings()).getTarget() != null))) {
				return mQState.getStateName(((State) IterableExtensions.<Transition>head(ps.getOutgoings()).getTarget()));
			}
		}
		return "";
	}

	/**
	 * This function returns all states contained in the
	 * given state machine.
	 * 
	 * @param sm State machine.
	 * @return All states contained in the given state machine.
	 */
	public Iterable<State> getAllStates(final StateMachine sm) {
		return Iterables.<State>filter(sm.allOwnedElements(), State.class);
	}

	/**
	 * This function returns all available states on a given
	 * state machine.
	 * 
	 * @param sm State machine.
	 * @return All available state of a given state machine.
	 */
	public Iterable<State> getAllAvailableStates(final StateMachine sm) {
		/*
		final Function1<State, Boolean> _function = (State s) -> {
			return Boolean.valueOf((!Objects.equal(s.getName(), "Unavailable")));
		};
		return IterableExtensions.<State>filter(this.getAllStates(sm), _function);
		 */
		BasicEList<State> res = new BasicEList<State>();
		for (State s : getAllStates(sm)) {
			if (!Objects.equal(s.getName(), "Unavailable")) {
				res.add(s);
			}
		}
		return res;

	}

	/**
	 * This function returns all available and non final state of
	 * a given state machine.
	 * 
	 * @param sm State machine.
	 * @return All available and non-final states of a given state machine.
	 */
	public Iterable<Pseudostate> getAllAvailableNonFinalStates(final StateMachine sm) {
		/*
		final Function1<Pseudostate, Boolean> _function = (Pseudostate ps) -> {
			return Boolean.valueOf((!Objects.equal(ps.getKind(), PseudostateKind.TERMINATE_LITERAL)));
		};
		return IterableExtensions.<Pseudostate>filter(Iterables.<Pseudostate>filter(this.getAllAvailableStates(sm), Pseudostate.class), _function);
		 */
		BasicEList<Pseudostate> res = new BasicEList<Pseudostate>();
		for (Pseudostate ps : Iterables.<Pseudostate>filter(getAllAvailableStates(sm), Pseudostate.class)) {
			if (ps.getKind() == PseudostateKind.TERMINATE_LITERAL) {
				res.add(ps);
			}
		}
		return res;

	}

	/**
	 * This function returns all regions within a given state machine.
	 * 
	 * @param sm State machine.
	 * @return All region of a given state machine.
	 */
	public Iterable<Region> getAllRegions(final StateMachine sm) {
		return Iterables.<Region>filter(sm.allOwnedElements(), Region.class);
	}

	/**
	 * This function returns all do-activities names,
	 * within a given state machine, sorted in alphabetical
	 * order.
	 * 
	 * @param sm State machine.
	 * @return All do-activities names of a given state machine.
	 */
	public TreeSet<String> getAllActivityNames(final StateMachine sm) {
		TreeSet<String> names = new TreeSet<String>();
		for (final State state : getAllAvailableStates(sm)) {
			if (mQState.hasDoActivities(state)) {
				names.add(state.getDoActivity().getName());
			}
		}
		return names;
	}

	/**
	 * This function returns all action names,
	 * within a given state machine, sorted in alphabetical
	 * order.
	 * 
	 * @param sm State machine.
	 * @return All action names of a given state machine.
	 */
	public TreeSet<String> getAllActionNames(final StateMachine sm) {
		TreeSet<String> names = new TreeSet<String>();
		for (final State state : getAllAvailableStates(sm)) {
			if (mQState.hasOnEntryActions(state)) {
				names.add(state.getEntry().getName());
			}
			if (mQState.hasOnExitActions(state)) {
				names.add(state.getExit().getName());
			}

			final Function1<Transition, String> _function = (Transition e) -> {
				return e.getName();
			};
			for (final Transition t : IterableExtensions.<Transition, String>sortBy(state.getOutgoings(), _function)) {
				if (mQTransition.hasAction(t)) {
					names.add(mQTransition.getFirstActionName(t));
				}
			}
		}
		return names;
	}

	/**
	 * This function returns all guard names,
	 * within a given state machine, sorted in alphabetical
	 * order.
	 * 
	 * @param sm State machine.
	 * @return All guard names of a given state machine.
	 */
	public TreeSet<String> getAllGuardNames(final StateMachine sm) {
		TreeSet<String> names = new TreeSet<String>();
		for (final State state : getAllAvailableStates(sm)) {
			final Function1<Transition, String> _function = (Transition e) -> {
				return e.getName();
			};
			for (final Transition t : IterableExtensions.<Transition, String>sortBy(state.getOutgoings(), _function)) {
				if (mQTransition.hasGuard(t)) {
					names.add(mQTransition.getGuardName(t));
				}
			}
		}
		return names;
	}
}
