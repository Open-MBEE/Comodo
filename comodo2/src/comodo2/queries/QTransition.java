package comodo2.queries;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.FunctionBehavior;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.TimeEvent;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.TransitionKind;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.internal.impl.SignalEventImpl;
import org.eclipse.uml2.uml.internal.impl.TimeEventImpl;

public class QTransition {
	@Inject
	private QRegion mQRegion;

	@Inject
	private QState mQState;

	@Inject
	private QEvent mQEvent;

	public String getEventName(final Transition t) {
		return getFirstEventName(t);
		/*
		if (t.getTriggers().isEmpty()) {
			return "";
		}
		
		if (mQEvent.isSignalEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent())) {
			return mQEvent.getSignalEventName(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
		} else {
			if (mQEvent.isTimeEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent())) {
				mQEvent.getTimeEventName(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
			}
		}
		return "";
		*/
	}

	public String getTimeEventDuration(final Transition t) {
		/*
		if ((this.hasEvent(t) && mQEvent.isTimeEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent()))) {
			return ((TimeEvent) IterableExtensions.<Trigger>head(t.getTriggers()).getEvent()).getWhen().getExpr().stringValue();
		}
		return "";
		*/
		if (hasEvent(t) == false) {
			return "";			
		}
		Trigger trigger = t.getTriggers().get(0);
		if (trigger == null) {
			return "";
		}
		if (trigger.getEvent() == null) {
			return "";
		}
		if ((trigger.getEvent() instanceof TimeEvent) == false) {
			return "";
		}
		TimeEvent e = (TimeEvent)trigger.getEvent();		
		return e.getWhen().getExpr().stringValue();
	}

	public String getFirstEventName(final Transition t) {
		if (t == null) {
			return "";
		}
		if (t.getTriggers().isEmpty()) {
			return "";
		}
		/*
		if (mQEvent.isSignalEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent())) {
			return mQEvent.getSignalEventName(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
		} else {
 			if (mQEvent.isTimeEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent())) {
				return mQEvent.getTimeEventName(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
			}
		}
		*/
		Trigger trigger = t.getTriggers().get(0);
		if (trigger == null) {
			return "";
		}
		Event e = trigger.getEvent();
		if (e == null) {
			return "";
		}
		if (mQEvent.isSignalEvent(e)) {
			return mQEvent.getSignalEventName(e);
		} else if (mQEvent.isTimeEvent(e)) {
			return mQEvent.getTimeEventName(e);
		}	
		return "";
	}

	public Signal getFirstEvent(final Transition t) {
		if (t.getTriggers().isEmpty()) {
			return null;
		}
		Event e = t.getTriggers().get(0).getEvent();
		if (e == null) {
			return null;
		}
		return mQEvent.getSignalEvent(e);
/*		
		if (mQEvent.isSignalEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent())) {
			return mQEvent.getSignalEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
		}
		return null;
*/		
	}

	public String getResolvedGuardName(final Transition t) {
		return this.getGuardName(t);
	}

	// Needed for StringTemplate boolean logic
	public String getGuardNameOrNull(final Transition t) {
		if (this.getGuardName(t)=="") {
			return null;
		} else {
			return this.getGuardName(t);
		}
	}

	public String getGuardName(final Transition t) {
		if (t.getGuard() == null) {
			return "";
		}
		/*
		if (IterableExtensions.isEmpty(Iterables.<OpaqueExpression>filter(t.getGuard().getOwnedElements(), OpaqueExpression.class))) {
			return "";
		}
		return IterableExtensions.<String>head(IterableExtensions.<OpaqueExpression>head(Iterables.<OpaqueExpression>filter(t.getGuard().getOwnedElements(), OpaqueExpression.class)).getBodies()).toString();
		*/
		for (Element e : t.getGuard().getOwnedElements()) {
			if (e instanceof OpaqueExpression) {
				return ((OpaqueExpression)e).getBodies().get(0).toString();
			}
		}
		return "";
	}

	public String getFirstActionName(final Transition t) {
		/*
		if (IterableExtensions.isEmpty(Iterables.<Activity>filter(t.allOwnedElements(), Activity.class))) {
			return "";
		}
		return IterableExtensions.<Activity>head(Iterables.<Activity>filter(t.allOwnedElements(), Activity.class)).getName();
		*/
		for (Element e : t.allOwnedElements()) {
			if (e instanceof Activity) {
				return ((Activity)e).getName();
			}
			if (e instanceof FunctionBehavior) {
				return ((FunctionBehavior)e).getName();
			}
		}
		return "";
	}

	public String getSourceName(final Transition t) {
		if (t.getSource() == null) {
			return "";
		}
		if ((t.getSource() instanceof State)) {
			return mQState.getStateName(((State) t.getSource()));
		}
		if ((t.getSource() instanceof Pseudostate)) {
			return mQState.getStateName(((Pseudostate) t.getTarget()));
		}
		return "";
	}

	public String getTargetName(final Transition t) {
		if (((t.getTarget() == null) || (this.isInternal(t) == true))) {
			return "";
		}
		if ((t.getTarget() instanceof State)) {
			return mQState.getStateName(((State) t.getTarget()));
		}
		if ((t.getTarget() instanceof Pseudostate)) {
			return mQState.getStateName(((Pseudostate) t.getTarget()));
		}
		return "";
	}

	public boolean isTimerTransition(final Transition t) {
		if (hasEvent(t)) {
			return false;
		}
		if (t.getTriggers().isEmpty() == true) {
			return false;
		}
		return mQEvent.isTimeEvent(t.getTriggers().get(0).getEvent());
	}

	public boolean isInternal(final Transition t) {
		return t.getKind() == TransitionKind.INTERNAL_LITERAL;
	}

	public boolean isTargetTopState(final Transition t) {
		return mQRegion.isTopState(t.getTarget().getContainer());
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

	/**
	 * Returns true if the transition points to a Choice pseudoState
	 * This is needed for QF Target because QM has a different choice node structure.
	 */
	public boolean isChoiceTransition(final Transition t){
		if (t.getTarget() instanceof Pseudostate) {
			Pseudostate ps = (Pseudostate) t.getTarget();
			if (ps.getKind() == PseudostateKind.CHOICE_LITERAL){
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the transition points to a entry or exit pseudoState
	 */
	public boolean pointsToEntryOrExitPseudostate(final Transition t){
		if (t.getTarget() instanceof Pseudostate) {
			Pseudostate ps = (Pseudostate) t.getTarget();
			if (ps.getKind() == PseudostateKind.ENTRY_POINT_LITERAL || 
				ps.getKind() == PseudostateKind.EXIT_POINT_LITERAL) {
					return true;
				}
		}
		return false;
	}

	/**
	 * Returns true if the transition points to a history pseudoState
	 */
	public boolean pointsToHistoryPseudostate(final Transition t){
		if (t.getTarget() instanceof Pseudostate) {
			return mQState.isHistoryState((Pseudostate) t.getTarget());
		}
		return false;
	}

	/**
	 * Returns the target name of the Entry/exit node pointed by t
	 */
	public String getEntryOrExitTargetName(final Transition t){
		if (t.getTarget().getOutgoings().isEmpty()){
			throw new RuntimeException("Malformed Entry/Exit pseudostate found (no state targeted by pseudostate) outbound from state: " + t.getSource().getName());
		}
		return t.getTarget().getOutgoings().get(0).getTarget().getName();
		
	}

	public boolean hasEvent(final Transition t) {
		if (Objects.equal(getFirstEventName(t), "")) {
			return false;
		}
		return true;
	}

	public boolean hasGuard(final Transition t) {
		if (Objects.equal(getGuardName(t), "")) {
			return false;
		}
		return true;
	}

	public boolean hasTarget(final Transition t) {
		if (Objects.equal(getTargetName(t), "")) {
			return false;
		}
		return true;
	}

	public boolean hasAction(final Transition t) {
		if (Objects.equal(getFirstActionName(t), "")) {
			return false;
		}
		return true;
	}

	public boolean hasSignalEvent(final Transition t) {
		if (t.getTriggers().isEmpty()) {
			return false;
		} else if (t.getTriggers().get(0).getEvent()==null) {
			return false;
		} else {
			return (t.getTriggers().get(0).getEvent().getClass() == SignalEventImpl.class);
		}
	}

	public boolean hasTimeEvent(final Transition t) {
		if (t.getTriggers().isEmpty()) {
			return false;
		} else if (t.getTriggers().get(0).getEvent()==null) {
			return false;
		} else {
			return (t.getTriggers().get(0).getEvent().getClass() == TimeEventImpl.class);
		}
	}
	
}
