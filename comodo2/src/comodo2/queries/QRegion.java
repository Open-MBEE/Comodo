package comodo2.queries;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import comodo2.engine.Config;
import comodo2.queries.QState;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Vertex;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class QRegion {
  @Inject
  @Extension
  private QState _qState;
  
  public State getParentState(final Region r) {
    return r.getState();
  }
  
  public String getFullyQualifiedName(final Region r) {
    boolean _isTopState = this.isTopState(r);
    if (_isTopState) {
      return r.getName();
    }
    Element _owner = r.getOwner();
    boolean _tripleEquals = (_owner == null);
    if (_tripleEquals) {
      return r.getName();
    }
    String _fullyQualifiedName = this._qState.getFullyQualifiedName(this.getParentState(r));
    String _plus = (_fullyQualifiedName + ":");
    String _name = r.getName();
    return (_plus + _name);
  }
  
  public String getRegionName(final Region r) {
    boolean _generateFullyQualifiedStateNames = Config.getInstance().generateFullyQualifiedStateNames();
    if (_generateFullyQualifiedStateNames) {
      return this.getFullyQualifiedName(r);
    }
    return r.getName();
  }
  
  public String getInitialStateName(final Region r) {
    Iterable<Pseudostate> _filter = Iterables.<Pseudostate>filter(r.allOwnedElements(), Pseudostate.class);
    for (final Pseudostate ps : _filter) {
      if ((((Objects.equal(ps.getKind(), PseudostateKind.INITIAL_LITERAL) && 
        Objects.equal(ps.getContainer().getOwner(), this.getParentState(r))) && 
        (ps.getOutgoings().size() == 1)) && 
        (IterableExtensions.<Transition>head(ps.getOutgoings()).getTarget() != null))) {
        Vertex _target = IterableExtensions.<Transition>head(ps.getOutgoings()).getTarget();
        return this._qState.getStateName(((State) _target));
      }
    }
    return "";
  }
  
  public boolean isTopState(final Region r) {
    Element _owner = r.getOwner();
    StateMachine _containingStateMachine = r.containingStateMachine();
    return Objects.equal(_owner, _containingStateMachine);
  }
}
