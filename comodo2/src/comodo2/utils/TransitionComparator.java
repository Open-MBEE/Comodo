package comodo2.utils;

import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import java.util.Comparator;

public class TransitionComparator implements Comparator<Transition> {
	
	private String getEventName(final Transition t) {
		if (t == null) {
			return "";
		}
		if (t.getTriggers().isEmpty()) {
			return "";
		}
		Trigger trigger = t.getTriggers().get(0);
		if (trigger == null) {
			return "";
		}
		Event e = trigger.getEvent();
		if (e == null) {
			return "";
		}
		return e.getName();
	}

    public int compare(Transition t1, Transition t2) {
    	if (t1.getName().isEmpty() == false || t2.getName().isEmpty() == false) {
    		return t1.getName().compareTo(t2.getName()); 
    	} else {
    		/*
    		 * Transition names are empty, check events.
    		 */
    		String e1 = getEventName(t1);
    		String e2 = getEventName(t2);
    		if (e1.isEmpty() == false || e2.isEmpty() == false) {
        		return e1.compareTo(e2);    			
    		} 
    	}
    	return 1;
    } 
} 
