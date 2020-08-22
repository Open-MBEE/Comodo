package comodo2.utils;

import org.eclipse.uml2.uml.Reception;
import java.util.Comparator;

public class ReceptionComparator implements Comparator<Reception> { 
    public int compare(Reception r1, Reception r2) {
    	if (r1.getSignal() != null && r2.getSignal() != null) {
    		return r1.getSignal().getName().compareTo(r2.getSignal().getName()); 
    	} else {
    		return 1;
    	}
    } 
} 
