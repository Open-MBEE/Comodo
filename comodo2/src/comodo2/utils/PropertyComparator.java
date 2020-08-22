package comodo2.utils;

import org.eclipse.uml2.uml.Property;
import java.util.Comparator;

public class PropertyComparator implements Comparator<Property> { 
    public int compare(Property p1, Property p2) { 
        return p1.getName().compareTo(p2.getName()); 
    } 
} 
