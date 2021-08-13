package comodo2.queries;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import comodo2.engine.Config;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Element;

public class QState {
	@Inject
	private QRegion mQRegion;

	@Inject
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
		if (ps.getKind() == PseudostateKind.SHALLOW_HISTORY_LITERAL) {
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
		BasicEList<State> res = new BasicEList<State>();
		for (State e : Iterables.<State>filter(s.allOwnedElements(), State.class)) {
			if (isFinal(e) == false) {
				res.add(e);
			}
		}
		return res;
	}

	public Iterable<State> getAllFinalSubstates(final State s) {
		BasicEList<State> res = new BasicEList<State>();
		for (State e : Iterables.<State>filter(s.allOwnedElements(), State.class)) {
			if (isFinal(e)) {
				res.add(e);
			}
		}
		return res;		
	}

	public Boolean hasInitialSubstate(final State s) {
		Iterable<Pseudostate> _filter = Iterables.<Pseudostate>filter(s.allOwnedElements(), Pseudostate.class);		
		for (final Pseudostate ps : _filter) {
			if (((((ps.getKind() == PseudostateKind.INITIAL_LITERAL) && 
					Objects.equal(ps.getContainer().getOwner(), s)) && 
					(ps.getOutgoings().size() == 1)) && 
					(Iterables.<Transition>getFirst(ps.getOutgoings(), null).getTarget() != null))) {
				return true;
			}
		}
		
		return false;
	}

	public String getInitialSubstateName(final State s) {
		Iterable<Pseudostate> _filter = Iterables.<Pseudostate>filter(s.allOwnedElements(), Pseudostate.class);		
		for (final Pseudostate ps : _filter) {
			if (((((ps.getKind() == PseudostateKind.INITIAL_LITERAL) && 
					Objects.equal(ps.getContainer().getOwner(), s)) && 
					(ps.getOutgoings().size() == 1)) && 
					(Iterables.<Transition>getFirst(ps.getOutgoings(), null).getTarget() != null))) {
				return getStateName( ((State) Iterables.<Transition>getFirst(ps.getOutgoings(), null).getTarget()) );
			}
		}
		
		return "";
	}

	/**
	 * @return The substate of a composite state that is pointed at by the initial node.
	 */
	public State getInitialSubstate(final State s) {
		Iterable<Pseudostate> _filter = Iterables.<Pseudostate>filter(s.allOwnedElements(), Pseudostate.class);		
		for (final Pseudostate ps : _filter) {
			if (((((ps.getKind() == PseudostateKind.INITIAL_LITERAL) && 
					Objects.equal(ps.getContainer().getOwner(), s)) && 
					(ps.getOutgoings().size() == 1)) && 
					(Iterables.<Transition>getFirst(ps.getOutgoings(), null).getTarget() != null))) {
						return ( ((State) Iterables.<Transition>getFirst(ps.getOutgoings(), null).getTarget()) );
			}
		}
		
		return null;
	}

	/**
	 * Returns the initial transition of a composite state.
	 * This is used when the initial transition of a composite state points to a pseudostate (like a choice node)
	 * which makes getInitialSubstateName(s) throws a ClassCastException.
	 */
	public Transition getInitialSubstateTransition(final State s) {
		Iterable<Pseudostate> _filter = Iterables.<Pseudostate>filter(s.allOwnedElements(), Pseudostate.class);		
		for (final Pseudostate ps : _filter) {
			if (((((ps.getKind() == PseudostateKind.INITIAL_LITERAL) && 
					Objects.equal(ps.getContainer().getOwner(), s)) && 
					(ps.getOutgoings().size() == 1)) && 
					(Iterables.<Transition>getFirst(ps.getOutgoings(), null).getTarget() != null))) {
				return ps.getOutgoings().get(0);
			}
		}
		return null;
	}

	/**
	 * @return all the composite substates of 's'.
	 */
	public Iterable<State> getCompositeSubstates(final State s) {
		BasicEList<State> res = new BasicEList<State>();
		Iterable<State> states = Iterables.<State>filter(s.allOwnedElements(), State.class);
		for (State e : states) {
			if (e.isComposite() && Objects.equal(this.getParentState(e), s)) {
				res.add(e);
			}
		}
		return res;
	}

	public Iterable<State> getSimpleSubstates(final State s) {
		BasicEList<State> res = new BasicEList<State>();
		Iterable<State> states = Iterables.<State>filter(s.allOwnedElements(), State.class);
		for (State e : states) {
			if ((e.isComposite() == false) && Objects.equal(this.getParentState(e), s)) {
				res.add(e);
			}
		}
		return res;		
	}

	public Iterable<State> getFinalSubstates(final State s) {
		BasicEList<State> res = new BasicEList<State>();
		Iterable<State> states = Iterables.<State>filter(s.allOwnedElements(), State.class);
		for (State e : states) {
			if ((e.isComposite() == false) && Objects.equal(this.getParentState(e), s) && isFinal(e)) {
				res.add(e);
			}
		}
		return res;		
		
	}

	public Iterable<Pseudostate> getHistory(final State s) {
		if (s.isComposite() == false) {
			return null;
		}
		/*
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
		*/
		BasicEList<Pseudostate> res = new BasicEList<Pseudostate>();		
		State p = getParentState(s);
		if (p != null) {
			for (Element ps : p.allOwnedElements()) {
				if (ps instanceof Pseudostate) {
					if (isHistoryState((Pseudostate)ps)) {
						res.add((Pseudostate)ps);
					}
				}
			}
		}
		return res;
	}

	public boolean isInitalState(final Pseudostate ps) {
		return ps.getKind() == PseudostateKind.INITIAL_LITERAL;
	}

	public boolean isFinalState(final Pseudostate ps) {
		return ps.getKind() == PseudostateKind.TERMINATE_LITERAL;
	}

	public boolean isHistoryState(final Pseudostate ps) {
		return (ps.getKind() == PseudostateKind.DEEP_HISTORY_LITERAL) || 
			   (ps.getKind() == PseudostateKind.SHALLOW_HISTORY_LITERAL);
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
		/*
		final Function1<Transition, Boolean> _function = (Transition e) -> {
			return Boolean.valueOf((mQTransition.isTimerTransition(e) && e.getSource().getName().matches(s.getName())));
		};
		return !IterableExtensions.isEmpty(IterableExtensions.<Transition>filter(Iterables.<Transition>filter(this.getParentState(s).allOwnedElements(), Transition.class), _function));
		*/
		for (Transition e : Iterables.<Transition>filter(getParentState(s).allOwnedElements(), Transition.class)) {
			if (mQTransition.isTimerTransition(e) && e.getSource().getName().matches(s.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return True if State s has at least one outgoing transition that carries a TimeEvent. False otherwise.
	 */
	public boolean hasOutgoingTimerTransition(final State s) {
		for (Transition t : s.getOutgoings()){
			if (mQTransition.isTimerTransition_v2(t)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasHistory(final State s) {
		if (s.isComposite() == false) {
			return false;
		}
		State p = getParentState(s);
		if (p == null) {
			return false;
		}
		for (Element ps : p.allOwnedElements()) {
			if (ps instanceof Pseudostate) {
				if (isHistoryState((Pseudostate)ps)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns all the pseudostates that are directly owned by State s.
	 */
	public Iterable<Pseudostate> getAllDirectlyOwnedPseudostates(final State s) {

		BasicEList<Pseudostate> res = new BasicEList<Pseudostate>();		
		for (Pseudostate ps : Iterables.<Pseudostate>filter(s.allOwnedElements(), Pseudostate.class)) {
			if (ps.getKind() == PseudostateKind.ENTRY_POINT_LITERAL || 
				ps.getKind() == PseudostateKind.EXIT_POINT_LITERAL){
				// Entry and exit points are directly owned by the composite state
					if (((State)ps.getOwner()).equals(s)) {
					res.add(ps);
				}
			// other pseudostates are owned by the state's region
			} else if (ps.getContainer().getState().equals(s)){
				res.add(ps);
			}
		}
		return res;
	}

	/**
	 * Returns all the substates that are directly owned by State s.
	 */
	public Iterable<State> getAllDirectSubstates(final State s) {
		BasicEList<State> res = new BasicEList<State>();
		for (State e : Iterables.<State>filter(s.allOwnedElements(), State.class)) {
			if (Objects.equal(getParentState(e), s)) {
				res.add(e);
			}
		}
		return res;		
	}

	/**
	 * Returns all the History nodes that this state is tracked by.
	 * That is, shallowHistory at depth +1 and all deepHistory that are owned by
	 * a composite state that also owns this state.
	 */
	public BasicEList<Pseudostate> getAllParentHistoryNodes(final State s) {
		BasicEList<Pseudostate> res = new BasicEList<Pseudostate>();		
		State p = getParentState(s);
		Integer depth = 1;

		while (p!=null){
			for (Pseudostate ps : getAllDirectlyOwnedPseudostates(p)) {
				// We care about shallow history only at depth 0
				if ((isHistoryState(ps) && depth <= 1) || ps.getKind() == PseudostateKind.DEEP_HISTORY_LITERAL) {
					res.add(ps);
				}
			}
			p = getParentState(p);
			depth++;
		}
		return res;
	}	
	
	/**
	 * Returns the duration string of the first TimeEvent that the transitions of the state carry.
	 * A state should only have one TimeEvent transition.
	 */
	public String getFirstTimeEventDurationString(final State s) {		
		for (Transition t : s.getOutgoings()){
			if (mQTransition.hasTimeEvent(t)){
				return mQTransition.getFirstTimeEventDurationString(t);
			}
		}
		return null;
	}

	/**
	 * Returns the orthogonal region (belonging to an orthogonal state) that State s
	 * is in, if any. If s in not within any orthogonal region, returns null.
	 */
	public Region getParentOrthogonalRegion(final State s) {

		Region parent_region = getParentRegion(s);
		State parent_state = null;

		if (parent_region == null) {
			return null;
		}

		while (parent_region!=null){
			parent_state = mQRegion.getParentState(parent_region);
			if (parent_state==null){
				return null;
			}
			if (parent_state.isOrthogonal()){
				return parent_region;
			}
			parent_region = getParentRegion(parent_state);
		}
		return null;
	}
}
