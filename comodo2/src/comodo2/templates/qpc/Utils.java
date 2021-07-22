package comodo2.templates.qpc;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.google.common.base.Objects;


public class Utils {
    
    /**
	 * Takes in a guard name and format it in the appropriate format for QPC use of guards.
	 */
	public String formatGuardName(String guardName, String smQualifiedName) {
		if (Objects.equal(guardName, "") || guardName == null){
			return null;
		}
		return smQualifiedName + "_impl_" + insertImplArg(guardName.trim());
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
	 * That is: smQualifiedName _impl_ functionName (me->impl) FOR EACH functionName in actionName
	 */
	public String formatActionName(String actionName, String smQualifiedName, String smClassName) {
		if (Objects.equal(actionName, "") || actionName == null){
			return "";
		}
		String str = "";
		for (String function : getAllFunctionsFromAction(actionName, false)) {
			str += checkTrailingSemicolon(smQualifiedName + "_impl_" + insertImplArg(function.trim())) + "\n";
		}
		for (String signalName : getAllSentSignalsFromAction(actionName)) {
			str += "QEvt *newEv = Q_NEW(QEvt, " + formatSignalName(signalName, smClassName) + ");\n";
			str += "QF_publish(newEv);\n";
		}
		return str;
	}

	public String formatStateName(String stateQualifiedName, String smQualifiedName){
		return smQualifiedName.toUpperCase() + "_" + stateQualifiedName.toUpperCase().replaceAll("::", "_");
	}
    
    public String checkTrailingSemicolon(String str) {
		return str.endsWith(";") ? str : str + ";";
	}

	/**
	 * Takes in a TreeSet of all action names and return a TreeSet of all the functions
	 * that the actions use. This is needed because one action can call multiple functions,
	 * and the same function can be re-used between actions.
	 */
	public TreeSet<String> getAllActionFunctionNames(TreeSet<String> actionNames) {
		TreeSet<String> functionNames = new TreeSet<String>();
		for (String action : actionNames){
			functionNames.addAll(getAllFunctionsFromAction(action, true));
		}
		return functionNames;
	}

	/**
	 * Returns list of all functions used in an action string.
	 */
	public List<String> getAllFunctionsFromAction(String str, Boolean removeArgs) {
		String tmp_str = str;
		List<String> functionList = new ArrayList<String>();

		// remove arguments between parenthesis
		if (removeArgs){
			tmp_str = tmp_str.replaceAll("\\(.*\\)","()");
		}

		Pattern r = Pattern.compile(".*\\(.*\\)");
		Matcher m = r.matcher(tmp_str);

		while(m.find()){
			functionList.add(m.group().trim());
		}

		return functionList;
	}

	/**
	 * Returns list of all signals sent in an action string.
	 * This regexes everything that is not followed by parentheses, in all caps
	 */
	public List<String> getAllSentSignalsFromAction(String actionString) {
		List<String> functionList = new ArrayList<String>();

		Pattern r = Pattern.compile("(?<=\\b)[A-Z1-9_]+(?=\\b(?![\\(\\), ]))");
		Matcher m = r.matcher(actionString);

		while(m.find()){
			functionList.add(m.group().trim());
		}

		return functionList;
	}

	/**
	 * Non regex-heavy implementation, so might be edge cases where it fails...
	 * @param functionStr string representing a function call
	 * @return same function call with the me->impl parameter inserted in first place
	 */
	public String insertImplArg(String functionStr){
		int firstParenthesis = functionStr.indexOf("(");
		// Matches empty parentheses
		Matcher m = Pattern.compile(".*\\(\\s*\\)").matcher(functionStr);
		if (m.find()){
			return functionStr.substring(0, firstParenthesis + 1) + "me->impl" + functionStr.substring(firstParenthesis + 1, functionStr.length());
		} else {
			return functionStr.substring(0, firstParenthesis + 1) + "me->impl, " + functionStr.substring(firstParenthesis + 1, functionStr.length());
		}
	}
}
