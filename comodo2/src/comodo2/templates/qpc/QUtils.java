package comodo2.templates.qpc;

import com.google.common.base.Objects;


public class QUtils {
    
    /**
	 * Takes in a guard name and format it in the appropriate format for QPC use of guards.
	 */
	public String formatGuardName(String guardName, String smQualifiedName) {
		if (Objects.equal(guardName, "") || guardName == null){
			return null;
		}
		return smQualifiedName + "_impl_" + guardName.trim();
	}

    /**
	 * Takes in an event name and format it in the appropriate format for QPC use of signals.
	 * This uses the name of the comodo component (class) containing the state machine.
	 */
	public String formatSignalName(String eventName, String smClassName) {
		return smClassName.toUpperCase() + "_" + eventName + "_SIG";
	}

	/**
	 * Takes in an action name and format it in the appropriate format for QPC use of actions.
	 */
	public String formatActionName(String actionName, String smQualifiedName) {
		if (Objects.equal(actionName, "") || actionName == null){
			return null;
		}
		return checkTrailingSemicolon(smQualifiedName + "_impl_" + actionName.trim());
	}

	public String formatStateName(String stateQualifiedName, String smQualifiedName){
		return smQualifiedName.toUpperCase() + "_" + stateQualifiedName.toUpperCase().replaceAll("::", "_");
	}
    
    public String checkTrailingSemicolon(String str) {
		return str.endsWith(";") ? str : str + ";";
	}

	public String eventNaming(String timeEventName){
		return timeEventName.replaceAll("[^A-Za-z0-9_]", "").toUpperCase();
	}
}
