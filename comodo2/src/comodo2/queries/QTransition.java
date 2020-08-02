package comodo2.queries;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import comodo2.queries.QEvent;
import comodo2.queries.QRegion;
import comodo2.queries.QState;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.TimeEvent;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.TransitionKind;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.Vertex;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class QTransition {
  @Inject
  @Extension
  private QRegion _qRegion;
  
  @Inject
  @Extension
  private QState _qState;
  
  @Inject
  @Extension
  private QEvent _qEvent;
  
  public String getEventName(final Transition t) {
    boolean _isEmpty = t.getTriggers().isEmpty();
    if (_isEmpty) {
      return "";
    }
    boolean _isSignalEvent = this._qEvent.isSignalEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
    if (_isSignalEvent) {
      return this._qEvent.getSignalEventName(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
    } else {
      boolean _isTimeEvent = this._qEvent.isTimeEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
      if (_isTimeEvent) {
        this._qEvent.getTimeEventName(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
      }
    }
    return "";
  }
  
  public String getTimeEventDuration(final Transition t) {
    if ((this.hasEvent(t) && this._qEvent.isTimeEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent()))) {
      Event _event = IterableExtensions.<Trigger>head(t.getTriggers()).getEvent();
      ((TimeEvent) _event).getWhen().getExpr().stringValue();
    }
    return "";
  }
  
  public String getFirstEventName(final Transition t) {
    boolean _isEmpty = t.getTriggers().isEmpty();
    if (_isEmpty) {
      return "";
    }
    boolean _isSignalEvent = this._qEvent.isSignalEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
    if (_isSignalEvent) {
      return this._qEvent.getSignalEventName(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
    } else {
      boolean _isTimeEvent = this._qEvent.isTimeEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
      if (_isTimeEvent) {
        return this._qEvent.getTimeEventName(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
      }
    }
    return "";
  }
  
  public Signal getFirstEvent(final Transition t) {
    boolean _isEmpty = t.getTriggers().isEmpty();
    if (_isEmpty) {
      return null;
    }
    boolean _isSignalEvent = this._qEvent.isSignalEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
    if (_isSignalEvent) {
      return this._qEvent.getSignalEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
    }
    return null;
  }
  
  public String getResolvedGuardName(final Transition t) {
    return this.getGuardName(t);
  }
  
  public String getGuardName(final Transition t) {
    Constraint _guard = t.getGuard();
    boolean _tripleEquals = (_guard == null);
    if (_tripleEquals) {
      return "";
    }
    boolean _isEmpty = IterableExtensions.isEmpty(Iterables.<OpaqueExpression>filter(t.getGuard().getOwnedElements(), OpaqueExpression.class));
    if (_isEmpty) {
      return "";
    }
    return IterableExtensions.<String>head(IterableExtensions.<OpaqueExpression>head(Iterables.<OpaqueExpression>filter(t.getGuard().getOwnedElements(), OpaqueExpression.class)).getBodies()).toString();
  }
  
  public String getFirstActionName(final Transition t) {
    boolean _isEmpty = IterableExtensions.isEmpty(Iterables.<Activity>filter(t.allOwnedElements(), Activity.class));
    if (_isEmpty) {
      return "";
    }
    return IterableExtensions.<Activity>head(Iterables.<Activity>filter(t.allOwnedElements(), Activity.class)).getName();
  }
  
  public String getSourceName(final Transition t) {
    Vertex _source = t.getSource();
    boolean _tripleEquals = (_source == null);
    if (_tripleEquals) {
      return "";
    }
    Vertex _source_1 = t.getSource();
    if ((_source_1 instanceof State)) {
      Vertex _source_2 = t.getSource();
      return this._qState.getStateName(((State) _source_2));
    }
    Vertex _source_3 = t.getSource();
    if ((_source_3 instanceof Pseudostate)) {
      Vertex _target = t.getTarget();
      return this._qState.getStateName(((Pseudostate) _target));
    }
    return "";
  }
  
  public String getTargetName(final Transition t) {
    if (((t.getTarget() == null) || (this.isInternal(t) == true))) {
      return "";
    }
    Vertex _target = t.getTarget();
    if ((_target instanceof State)) {
      Vertex _target_1 = t.getTarget();
      return this._qState.getStateName(((State) _target_1));
    }
    Vertex _target_2 = t.getTarget();
    if ((_target_2 instanceof Pseudostate)) {
      Vertex _target_3 = t.getTarget();
      return this._qState.getStateName(((Pseudostate) _target_3));
    }
    return "";
  }
  
  public boolean isTimerTransition(final Transition t) {
    boolean _hasEvent = this.hasEvent(t);
    if (_hasEvent) {
      return false;
    }
    boolean _isEmpty = t.getTriggers().isEmpty();
    boolean _equals = (_isEmpty == true);
    if (_equals) {
      return false;
    }
    return this._qEvent.isTimeEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
  }
  
  public boolean isInternal(final Transition t) {
    TransitionKind _kind = t.getKind();
    return Objects.equal(_kind, TransitionKind.INTERNAL_LITERAL);
  }
  
  public boolean isTargetTopState(final Transition t) {
    return this._qRegion.isTopState(t.getTarget().getContainer());
  }
  
  /**
   * An internal transition (no target state) with no triggering event
   * and without guard can be dangerous since it is basically an infinite loop.
   * 
   * Also a self-transition with no triggering event and without guard can be
   * dangerous since it is basically an infinite loop.
   * 
   * Therefore in these cases the transition is considered malformed and should
   * be skipped!
   */
  public boolean isMalformed(final Transition t) {
    if ((((this.hasEvent(t) == false) && 
      (this.hasGuard(t) == false)) && 
      (this.hasTarget(t) == false))) {
      return true;
    }
    if (((((this.hasEvent(t) == false) && 
      (this.hasGuard(t) == false)) && 
      (this.hasTarget(t) == true)) && 
      Objects.equal(this.getTargetName(t), this.getSourceName(t)))) {
      return true;
    }
    return false;
  }
  
  public boolean hasEvent(final Transition t) {
    String _firstEventName = this.getFirstEventName(t);
    boolean _equals = Objects.equal(_firstEventName, "");
    if (_equals) {
      return false;
    }
    return true;
  }
  
  public boolean hasGuard(final Transition t) {
    String _guardName = this.getGuardName(t);
    boolean _equals = Objects.equal(_guardName, "");
    if (_equals) {
      return false;
    }
    return true;
  }
  
  public boolean hasTarget(final Transition t) {
    String _targetName = this.getTargetName(t);
    boolean _equals = Objects.equal(_targetName, "");
    if (_equals) {
      return false;
    }
    return true;
  }
  
  public boolean hasAction(final Transition t) {
    String _firstActionName = this.getFirstActionName(t);
    boolean _equals = Objects.equal(_firstActionName, "");
    if (_equals) {
      return false;
    }
    return true;
  }
}
