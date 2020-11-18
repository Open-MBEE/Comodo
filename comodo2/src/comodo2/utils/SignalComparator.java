package comodo2.utils;

import org.eclipse.uml2.uml.Signal;
import java.util.Comparator;

public class SignalComparator implements Comparator<Signal> { 
    public int compare(Signal s1, Signal s2) { 
        return s1.getName().compareTo(s2.getName()); 
    } 
} 
