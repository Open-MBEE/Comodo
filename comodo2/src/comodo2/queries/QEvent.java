package comodo2.queries;

import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.TimeEvent;

public class QEvent {
	public String getSignalEventName(final Event e) {
		if (((SignalEvent) e).getSignal() == null) {
			return "";
		}
		return ((SignalEvent) e).getSignal().getName();
	}

	public Signal getSignalEvent(final Event e) {
		return ((SignalEvent) e).getSignal();
	}

	public String getTimeEventName(final Event e) {
		if (((TimeEvent) e).isRelative()) {
			return ("after_" + ((TimeEvent) e).getWhen().getExpr().stringValue());
		} else {
			return ("at_" + ((TimeEvent) e).getWhen().getExpr().stringValue());
		}
	}

	public boolean isTimeEvent(final Event e) {
		return (e instanceof TimeEvent);
	}

	public boolean isSignalEvent(final Event e) {
		return (e instanceof SignalEvent);
	}
}
