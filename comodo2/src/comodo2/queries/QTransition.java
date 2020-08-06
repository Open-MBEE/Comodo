package comodo2.queries;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import comodo2.queries.QEvent;
import comodo2.queries.QRegion;
import comodo2.queries.QState;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.TimeEvent;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.TransitionKind;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

public class QTransition {
	@Inject
	@Extension
	private QRegion mQRegion;

	@Inject
	@Extension
	private QState mQState;

	@Inject
	@Extension
	private QEvent mQEvent;

	public String getEventName(final Transition t) {
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
	}

	public String getTimeEventDuration(final Transition t) {
		if ((this.hasEvent(t) && mQEvent.isTimeEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent()))) {
			return ((TimeEvent) IterableExtensions.<Trigger>head(t.getTriggers()).getEvent()).getWhen().getExpr().stringValue();
		}
		return "";
	}

	public String getFirstEventName(final Transition t) {
		if (t.getTriggers().isEmpty()) {
			return "";
		}
		if (mQEvent.isSignalEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent())) {
			return mQEvent.getSignalEventName(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
		} else {
			if (mQEvent.isTimeEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent())) {
				return mQEvent.getTimeEventName(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
			}
		}
		return "";
	}

	public Signal getFirstEvent(final Transition t) {
		if (t.getTriggers().isEmpty()) {
			return null;
		}
		if (mQEvent.isSignalEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent())) {
			return mQEvent.getSignalEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
		}
		return null;
	}

	public String getResolvedGuardName(final Transition t) {
		return this.getGuardName(t);
	}

	public String getGuardName(final Transition t) {
		if (t.getGuard() == null) {
			return "";
		}
		if (IterableExtensions.isEmpty(Iterables.<OpaqueExpression>filter(t.getGuard().getOwnedElements(), OpaqueExpression.class))) {
			return "";
		}
		return IterableExtensions.<String>head(IterableExtensions.<OpaqueExpression>head(Iterables.<OpaqueExpression>filter(t.getGuard().getOwnedElements(), OpaqueExpression.class)).getBodies()).toString();
	}

	public String getFirstActionName(final Transition t) {
		if (IterableExtensions.isEmpty(Iterables.<Activity>filter(t.allOwnedElements(), Activity.class))) {
			return "";
		}
		return IterableExtensions.<Activity>head(Iterables.<Activity>filter(t.allOwnedElements(), Activity.class)).getName();
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
		return mQEvent.isTimeEvent(IterableExtensions.<Trigger>head(t.getTriggers()).getEvent());
	}

	public boolean isInternal(final Transition t) {
		return Objects.equal(t.getKind(), TransitionKind.INTERNAL_LITERAL);
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
}
