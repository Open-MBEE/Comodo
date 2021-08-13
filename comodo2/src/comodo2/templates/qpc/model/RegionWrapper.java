package comodo2.templates.qpc.model;

import org.eclipse.uml2.uml.Region;


/**
 * This class is a mere wrapper for regions so that it's easier to retrieve their qualified names.
 * StringTemplate can then access those fields via %region.qualifiedName%
 */
public class RegionWrapper {

    private String name;
    private String qualifiedName;

    public RegionWrapper(Region r, String stateQualifiedName){
        this.name = r.getName();
        this.qualifiedName = stateQualifiedName + "_" + this.name;
    }

    /* Getter methods */
    public String getName() {
        return this.name;
    }

    public String getQualifiedName() {
        return this.qualifiedName;
    }
}
