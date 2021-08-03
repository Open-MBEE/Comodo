package comodo2.templates.qpc.model;

import java.util.TreeSet;

/**
 * This class will just serve as a container structure for multiple variables
 * that describe the current generation of a statemachine.
 */
public class CurrentGeneration {
    
    private String smName;
	private String className;
	private String smQualifiedName;
	private Integer finalStateCounter;
	private Integer unNamedStateCounter;
    
    private TreeSet<String> signalEventsNameset;

    public CurrentGeneration(String className, String smName){
        this.smQualifiedName = sanitizeName(className + "_" + smName);
        this.className = sanitizeName(className);
        this.smName = sanitizeName(smName);
        this.finalStateCounter = 0;
        this.unNamedStateCounter = 0;
        this.signalEventsNameset = new TreeSet<String>();
    }

    public String sanitizeName(String name){
        return (name.substring(0, 1).toLowerCase() + name.substring(1)).replaceAll("-", "_");
    }

    public String getSmName() {
        return smName;
    }

    public Integer getFinalStateCounter() {
        return finalStateCounter;
    }

    public void incrementFinalStateCounter() {
        finalStateCounter++;
    }

    public Integer getUnNamedStateCounter() {
        return unNamedStateCounter;
    }

    public void incrementUnNamedStateCounter() {
        unNamedStateCounter++;
    }

    public String getClassName() {
        return className;
    }

    public String getSmQualifiedName() {
        return smQualifiedName;
    }

    public void setSmQualifiedName(String smQualifiedName) {
        this.smQualifiedName = smQualifiedName;
    }

    public void addSignalEventsNameset(String signalName) {
        this.signalEventsNameset.add(signalName);
    }

    public TreeSet<String> getSignalEventsNameset(){
        return this.signalEventsNameset;
    }
}
