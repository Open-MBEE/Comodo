package comodo2.queries;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import comodo2.queries.QState;
import comodo2.queries.QTransition;
import java.util.List;
import java.util.TreeSet;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Vertex;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class QStateMachine {
  @Inject
  @Extension
  private QState _qState;
  
  @Inject
  @Extension
  private QTransition _qTransition;
  
  /**
   * This function returns the name a state machine.
   * 
   * @param sm State machine.
   * @return The name of the given state machine.
   */
  public String getName(final QStateMachine sm) {
    return this.getName(sm);
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
    Iterable<Pseudostate> _filter = Iterables.<Pseudostate>filter(sm.allOwnedElements(), Pseudostate.class);
    for (final Pseudostate ps : _filter) {
      if ((((Objects.equal(ps.getKind(), PseudostateKind.INITIAL_LITERAL) && 
        Objects.equal(ps.getContainer().getOwner(), sm)) && 
        (ps.getOutgoings().size() == 1)) && 
        (IterableExtensions.<Transition>head(ps.getOutgoings()).getTarget() != null))) {
        Vertex _target = IterableExtensions.<Transition>head(ps.getOutgoings()).getTarget();
        return this._qState.getStateName(((State) _target));
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
    final Function1<State, Boolean> _function = (State s) -> {
      String _name = s.getName();
      return Boolean.valueOf((!Objects.equal(_name, "Unavailable")));
    };
    return IterableExtensions.<State>filter(this.getAllStates(sm), _function);
  }
  
  /**
   * This function returns all available and non final state of
   * a given state machine.
   * 
   * @param sm State machine.
   * @return All available and non-final states of a given state machine.
   */
  public Iterable<Pseudostate> getAllAvailableNonFinalStates(final StateMachine sm) {
    final Function1<Pseudostate, Boolean> _function = (Pseudostate ps) -> {
      PseudostateKind _kind = ps.getKind();
      return Boolean.valueOf((!Objects.equal(_kind, PseudostateKind.TERMINATE_LITERAL)));
    };
    return IterableExtensions.<Pseudostate>filter(Iterables.<Pseudostate>filter(this.getAllAvailableStates(sm), Pseudostate.class), _function);
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
    Iterable<State> _allAvailableStates = this.getAllAvailableStates(sm);
    for (final State state : _allAvailableStates) {
      boolean _hasDoActivities = this._qState.hasDoActivities(state);
      if (_hasDoActivities) {
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
    Iterable<State> _allAvailableStates = this.getAllAvailableStates(sm);
    for (final State state : _allAvailableStates) {
      {
        boolean _hasOnEntryActions = this._qState.hasOnEntryActions(state);
        if (_hasOnEntryActions) {
          names.add(state.getEntry().getName());
        }
        boolean _hasOnExitActions = this._qState.hasOnExitActions(state);
        if (_hasOnExitActions) {
          names.add(state.getExit().getName());
        }
        final Function1<Transition, String> _function = (Transition e) -> {
          return e.getName();
        };
        List<Transition> _sortBy = IterableExtensions.<Transition, String>sortBy(state.getOutgoings(), _function);
        for (final Transition t : _sortBy) {
          boolean _hasAction = this._qTransition.hasAction(t);
          if (_hasAction) {
            names.add(this._qTransition.getFirstActionName(t));
          }
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
    Iterable<State> _allAvailableStates = this.getAllAvailableStates(sm);
    for (final State state : _allAvailableStates) {
      final Function1<Transition, String> _function = (Transition e) -> {
        return e.getName();
      };
      List<Transition> _sortBy = IterableExtensions.<Transition, String>sortBy(state.getOutgoings(), _function);
      for (final Transition t : _sortBy) {
        boolean _hasGuard = this._qTransition.hasGuard(t);
        if (_hasGuard) {
          names.add(this._qTransition.getGuardName(t));
        }
      }
    }
    return names;
  }
}
