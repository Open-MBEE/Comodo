package comodo2.queries;

import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.TimeEvent;

@SuppressWarnings("all")
public class QEvent {
  public String getSignalEventName(final Event e) {
    Signal _signal = ((SignalEvent) e).getSignal();
    boolean _tripleEquals = (_signal == null);
    if (_tripleEquals) {
      return "";
    }
    return ((SignalEvent) e).getSignal().getName();
  }
  
  public Signal getSignalEvent(final Event e) {
    return ((SignalEvent) e).getSignal();
  }
  
  public String getTimeEventName(final Event e) {
    boolean _isRelative = ((TimeEvent) e).isRelative();
    if (_isRelative) {
      String _stringValue = ((TimeEvent) e).getWhen().getExpr().stringValue();
      return ("after_" + _stringValue);
    } else {
      String _stringValue_1 = ((TimeEvent) e).getWhen().getExpr().stringValue();
      return ("at_" + _stringValue_1);
    }
  }
  
  public boolean isTimeEvent(final Event e) {
    return (e instanceof TimeEvent);
  }
  
  public boolean isSignalEvent(final Event e) {
    return (e instanceof SignalEvent);
  }
}
