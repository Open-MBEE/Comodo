package comodo2.templates.qpc.model;

import java.util.ArrayList;
import com.google.common.collect.Iterables;

import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;

import comodo2.queries.QState;
import comodo2.templates.qpc.Utils;


/**
 * This class serves as a wrapper for Othrogonal states.
 * This initializes some variable names and does logic on regions.
 */
public class OrthogonalStateWrapper {

    private Utils mUtils = new Utils();
    private QState mQState = new QState();
    
    private String name;
    private State state;
    private ArrayList<Region> regions;
    private ArrayList<RegionWrapper> wrappedRegions;
    private CurrentGeneration current;
    private String bailEvent;
    private String stateQualifiedName;

    public OrthogonalStateWrapper(State s, CurrentGeneration current){
        this.state = s;
        this.stateQualifiedName = mUtils.formatRegionName(mQState.getFullyQualifiedName(s));
        this.name = s.getName();
        this.current = current;
        this.bailEvent = current.getSmQualifiedName().toUpperCase() + "_BAIL_EVENT_";
        this.setRegions();
        this.setWrappedRegions();
    }

    /* Getter methods */
    public CurrentGeneration getCurrent() {
        return current;
    }

    public String getStateQualifiedName() {
        return stateQualifiedName;
    }

    public String getName() {
        return this.name;
    }

    public State getState() {
        return this.state;
    }

    public String getBailEvent() {
        return this.bailEvent;
    }

    public ArrayList<Region> getRegions() {
        return this.regions;
    }

    public ArrayList<RegionWrapper> getWrappedRegions() {
        return this.wrappedRegions;
    }
    /* END OF Getter methods */

    /**
     * Private setter, we only want to set regions once and for sure do not want
     * them to be manipulated from outside of this class' constructor.
     */
    private void setRegions(){
        this.regions = new ArrayList<Region>();
		for (Region r : Iterables.<Region>filter(this.state.allOwnedElements(), Region.class)) {
            // if the state this region belongs to is the orthogonal state
            if (r.getState()==this.state){
                this.regions.add(r);
            }
        }
    }

    /**
     * Private setter, 
     */
    private void setWrappedRegions(){
        this.wrappedRegions = new ArrayList<RegionWrapper>();
		for (Region r : this.regions) {
            this.wrappedRegions.add(new RegionWrapper(r, this.stateQualifiedName));
        }
    }

    /**
     * This method had to be moved to StateMachineSource because the mQEvent member injection led
     * to some nullPointerException for some reason that I do not fully understand.
     * I will dig into that later.
	 * This method returns a HashMap of the signals that should get passed through to subregions in an orthogonal state.
     */
    // public HashMap<String, ArrayList<String>> getSignalDispatches(){
    //     HashMap<String, ArrayList<String>> signalDispatches = new HashMap<String, ArrayList<String>>();

    //     for (Region r : this.regions){
    //         for (Transition t : Iterables.<Transition>filter(r.allOwnedElements(), Transition.class)) {
    //             String triggerEvent = null;
    //             System.out.println(t.getSource().getName() + " -> "+ t.getTarget().getName() + " ----- " + t.toString());
    //             if (mQTransition.hasSignalEvent(t)){
    //                 triggerEvent = mUtils.formatSignalName(mQTransition.getFirstEventName(t), current.getClassName());
    //             } else if (mQTransition.hasTimeEvent(t)){
    //                 triggerEvent = mUtils.formatTimeEventName(t.getSource().getName());
    //             }
                
    //             if (triggerEvent!=null) {
    //                 if (signalDispatches.containsKey(triggerEvent)) {
    //                     signalDispatches.get(triggerEvent).add(r.getName());
    //                 } else {
    //                     signalDispatches.put(triggerEvent, new ArrayList<String>());
    //                     signalDispatches.get(triggerEvent).add(r.getName());
    //                 }
                    
    //             }
    //         }
    //     }
    //     return signalDispatches;
    // }
}
