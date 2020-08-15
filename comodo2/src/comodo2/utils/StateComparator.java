package comodo2.utils;

import org.eclipse.uml2.uml.State;
import java.util.Comparator;

public class StateComparator implements Comparator<State> { 
    public int compare(State s1, State s2) { 
        return s1.getName().compareTo(s2.getName()); 
    } 
} 
