package comodo2.templates.qpc;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.google.common.base.Objects;

import comodo2.templates.qpc.model.FunctionCall;


public class Utils {
    
    /**
	 * Takes in a guard name and format it in the appropriate format for QPC use of guards.
	 */
	public String formatGuardName(String guardName, String smQualifiedName) {
		if (Objects.equal(guardName, "") || guardName == null){
			return null;
		}
		return smQualifiedName + "_impl_" + insertImplArg(removeTrailingSemicolon(guardName.trim()));
	}

    /**
	 * Takes in an event name and format it in the appropriate format for QPC use of signals.
	 * This uses the name of the comodo component (class) containing the state machine.
	 */
	public String formatSignalName(String eventName, String smClassName) {
		return smClassName.toUpperCase() + "_" + eventName + "_SIG";
	}

	public String formatTimeEventName(String stateName) {
		return stateName.toUpperCase() + "_TIMER_SIG";
	}

	public String formatStateName(String stateQualifiedName, String smQualifiedName){
		return (smQualifiedName + "_" + stateQualifiedName).replaceAll("::", "_").replaceAll(":", "_");
	}
    
	public String formatStateEnum(String stateQualifiedName, String smQualifiedName){
		return formatStateName(stateQualifiedName, smQualifiedName).toUpperCase();
	}
    
    public String checkTrailingSemicolon(String str) {
		return str.endsWith(";") ? str : str + ";";
	}

	public String removeTrailingSemicolon(String str) {
		return str.endsWith(";") ? str.substring(0, str.length()-1) : str;
	}

	/**
	 * Takes in an action name and format it in the appropriate format for QPC use of actions.
	 * That is: smQualifiedName_impl_functionName(me->impl, args) FOR EACH function call in actionName
	 */
	public String formatActionName(String actionName, String smQualifiedName, String smClassName) {
		if (Objects.equal(actionName, "") || actionName == null){
			return "";
		}
		String str = "";
		for (FunctionCall function : getAllFunctionCallsFromFunctionString(actionName)) {
			str += checkTrailingSemicolon(smQualifiedName + "_impl_" + insertImplArg(function.toString().trim())) + "\n";
		}
		Integer counter = 0;
		for (String signalName : getAllSentSignalsFromAction(actionName)) {
			str += "QEvt *newEv" + counter + " = Q_NEW(QEvt, " + formatSignalName(signalName, smClassName) + ");\n";
			str += "QF_publish_(newEv" + counter + ");\n";
			counter++;
		}
		return str.trim();
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


	/**
	 * Takes in a TreeSet of functionStrings and return a TreeSet of all the functions
	 * that the actions use, in the form of FunctionCall objects. 
	 * This is needed because one action can call multiple functions.
	 */
	public TreeSet<FunctionCall> getAllFunctionCalls(TreeSet<String> functionStrings) {
		TreeSet<FunctionCall> functionNames = new TreeSet<FunctionCall>();
		for (String funcStr : functionStrings){
			functionNames.addAll(getAllFunctionCallsFromFunctionString(funcStr));
		}
		return functionNames;
	}

	/**
	 * Returns list of all functions used in an function string, as FunctionCall objects.
	 */
	public List<FunctionCall> getAllFunctionCallsFromFunctionString(String funcStr) {
		String tmp_str = funcStr;
		List<FunctionCall> functionCallList = new ArrayList<FunctionCall>();

		Pattern r = Pattern.compile(".*\\(.*\\)");
		Matcher m = r.matcher(tmp_str);

		while(m.find()){
			functionCallList.add(new FunctionCall(m.group().trim(), false));
		}

		return functionCallList;
	}

}
