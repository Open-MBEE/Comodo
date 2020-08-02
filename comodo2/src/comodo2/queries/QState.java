package comodo2.queries;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import comodo2.engine.Config;
import comodo2.queries.QRegion;
import comodo2.queries.QTransition;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.FinalState;
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
public class QState {
  @Inject
  @Extension
  private QRegion _qRegion;
  
  @Inject
  @Extension
  private QTransition _qTransition;
  
  public Region getParentRegion(final State s) {
    return s.getContainer();
  }
  
  public String getFullyQualifiedName(final State s) {
    if ((s == null)) {
      return "";
    }
    boolean _isTopState = this.isTopState(s);
    if (_isTopState) {
      return s.getName();
    }
    Element _owner = s.getOwner();
    boolean _tripleEquals = (_owner == null);
    if (_tripleEquals) {
      return s.getName();
    }
    String _fullyQualifiedName = this._qRegion.getFullyQualifiedName(this.getParentRegion(s));
    String _plus = (_fullyQualifiedName + ":");
    String _name = s.getName();
    return (_plus + _name);
  }
  
  /**
   * This function returns the [fully qualified] name of a given state.
   * 
   * @param s State to check.
   * @return Name of the state.
   */
  public String getStateName(final State s) {
    boolean _generateFullyQualifiedStateNames = Config.getInstance().generateFullyQualifiedStateNames();
    if (_generateFullyQualifiedStateNames) {
      return this.getFullyQualifiedName(s);
    }
    return s.getName();
  }
  
  public String getStateName(final Pseudostate ps) {
    boolean _generateFullyQualifiedStateNames = Config.getInstance().generateFullyQualifiedStateNames();
    if (_generateFullyQualifiedStateNames) {
      String _fullyQualifiedName = this._qRegion.getFullyQualifiedName(ps.getContainer());
      String _plus = (_fullyQualifiedName + ":");
      String _name = ps.getName();
      return (_plus + _name);
    }
    return ps.getName();
  }
  
  public String getHistoryTypeName(final Pseudostate ps) {
    PseudostateKind _kind = ps.getKind();
    boolean _equals = Objects.equal(_kind, PseudostateKind.SHALLOW_HISTORY_LITERAL);
    if (_equals) {
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
      boolean _isFinal = this.isFinal(e);
      return Boolean.valueOf((!_isFinal));
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
        Vertex _target = IterableExtensions.<Transition>head(ps.getOutgoings()).getTarget();
        return this.getStateName(((State) _target));
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
    boolean _isComposite = s.isComposite();
    boolean _equals = (_isComposite == false);
    if (_equals) {
      return null;
    }
    final Function1<Pseudostate, Boolean> _function = (Pseudostate e) -> {
      return Boolean.valueOf(this.isHistoryState(e));
    };
    final Iterable<Pseudostate> hs = IterableExtensions.<Pseudostate>filter(Iterables.<Pseudostate>filter(s.allOwnedElements(), Pseudostate.class), _function);
    boolean _isEmpty = IterableExtensions.isEmpty(hs);
    if (_isEmpty) {
      return null;
    }
    final Function1<Pseudostate, Boolean> _function_1 = (Pseudostate e) -> {
      State _parentState = this.getParentState(e);
      return Boolean.valueOf(Objects.equal(_parentState, s));
    };
    return IterableExtensions.<Pseudostate>filter(hs, _function_1);
  }
  
  public boolean isInitalState(final Pseudostate ps) {
    PseudostateKind _kind = ps.getKind();
    return Objects.equal(_kind, PseudostateKind.INITIAL_LITERAL);
  }
  
  public boolean isFinalState(final Pseudostate ps) {
    PseudostateKind _kind = ps.getKind();
    return Objects.equal(_kind, PseudostateKind.TERMINATE_LITERAL);
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
    Element _owner = s.getContainer().getOwner();
    StateMachine _containingStateMachine = s.containingStateMachine();
    return Objects.equal(_owner, _containingStateMachine);
  }
  
  /**
   * This function checks whether the state is a final pseudo-state.
   * Note that a composite state should not be final.
   * 
   * @param s State to check.
   * @return True if the state is a final pseudo-state, false otherwise.
   */
  public boolean isFinal(final State s) {
    boolean _isComposite = s.isComposite();
    if (_isComposite) {
      return false;
    }
    return (s instanceof FinalState);
  }
  
  public boolean hasOnEntryActions(final State s) {
    Behavior _entry = s.getEntry();
    return (_entry != null);
  }
  
  public boolean hasOnExitActions(final State s) {
    Behavior _exit = s.getExit();
    return (_exit != null);
  }
  
  public boolean hasDoActivities(final State s) {
    Behavior _doActivity = s.getDoActivity();
    return (_doActivity != null);
  }
  
  public boolean hasTimerTransition(final State s) {
    State _parentState = this.getParentState(s);
    boolean _tripleEquals = (_parentState == null);
    if (_tripleEquals) {
      return false;
    }
    final Function1<Transition, Boolean> _function = (Transition e) -> {
      return Boolean.valueOf((this._qTransition.isTimerTransition(e) && e.getSource().getName().matches(s.getName())));
    };
    boolean _isEmpty = IterableExtensions.isEmpty(IterableExtensions.<Transition>filter(Iterables.<Transition>filter(this.getParentState(s).allOwnedElements(), Transition.class), _function));
    return (!_isEmpty);
  }
  
  public boolean hasHistory(final State s) {
    boolean _isComposite = s.isComposite();
    boolean _equals = (_isComposite == false);
    if (_equals) {
      return false;
    }
    final Function1<Pseudostate, Boolean> _function = (Pseudostate ps) -> {
      boolean _isHistoryState = this.isHistoryState(ps);
      return Boolean.valueOf((_isHistoryState == true));
    };
    boolean _isEmpty = IterableExtensions.isEmpty(IterableExtensions.<Pseudostate>filter(Iterables.<Pseudostate>filter(s.allOwnedElements(), Pseudostate.class), _function));
    return (!_isEmpty);
  }
}
