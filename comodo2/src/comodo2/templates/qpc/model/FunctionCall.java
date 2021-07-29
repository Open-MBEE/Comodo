package comodo2.templates.qpc.model;

import org.apache.log4j.Logger;

import comodo2.engine.Main;


/**
 * Implements Comparable to be allowed in TreeSets
 */
public class FunctionCall implements Comparable<FunctionCall> {

	private static final Logger mLogger = Logger.getLogger(Main.class);

    
    private String funcName = null;
    private String args = null;
    private String[] argList = null;
    /** Flag indicates whether this function is being used as a Guard. */
    // WARNING: CURRENTLY NOT USED AND NOT DEFINED
    private boolean isGuard = false;



    public FunctionCall(String functionStr, boolean isGuard){
        this.isGuard = isGuard;

        int firstParIndex = functionStr.indexOf("(");
        int lastParIndex = functionStr.lastIndexOf(")");

        // checks for well formatted functionStr
        if (firstParIndex > 0 && firstParIndex < lastParIndex){
            this.funcName = functionStr.substring(0, firstParIndex).trim();

            // if non-empty set of parentheses
            if (lastParIndex > firstParIndex + 1) {
                this.args = functionStr.substring(firstParIndex+1, lastParIndex).trim();
                if (this.args.equals("")){
                    this.args = null; // for example functionName( ), agrs go back to null instead of empty string
                }
            }
        } else {
            mLogger.warn("FunctionCall object created on wrongly formatted function string: " + functionStr);
            this.funcName = functionStr;
        }

        if (this.args != null && this.args.length() > 0) {  // parse arguments into a list
            argList = args.split(",");
        } else {
            argList = new String[0]; // empty String list
        }
    }


    /**
     * Compares first based on isGuard flag, then by name -- using String.compareTo() by default
     */
    @Override
    public int compareTo(FunctionCall otherObj) {
        // Let a := this, b:= other
        if (this.isGuard() && !otherObj.isGuard()) {
            return -1;  // a < b
        }
        if (!this.isGuard() && otherObj.isGuard()) {
            return 1;  // a > b
        }
        // if guard flag is the same, check name
        if (this.getName() == null && otherObj.getName() == null) {
            return 0;   // a == b
        }
        if (this.getName() != null && otherObj.getName() == null) {
            return 1;   // a > b
        }
        if (this.getName() == null && otherObj.getName() != null) {
            return -1;  // a < b
        }
        // TODO: extend ArgumentedFunctionCall or something, to use in StateMachineSource.
        // if (this.getName().equals(otherObj.getName())) {
        //     // if name flag is the same, check guard
        //     if (this.getArgs() == null && otherObj.getArgs() == null) {
        //         return 0;   // a == b
        //     }
        //     if (this.getArgs() != null && otherObj.getArgs() == null) {
        //         return 1;   // a > b
        //     }
        //     if (this.getArgs() == null && otherObj.getArgs() != null) {
        //         return -1;  // a < b
        //     }
        //     return this.getArgs().compareTo(otherObj.getArgs());
        // }
        return this.getName().compareTo(otherObj.getName());
    }


    public String getName(){
        return this.funcName;
    }

    public Boolean isGuard(){
        return this.isGuard;
    }

    public String getArgs(){
        return this.args;
    }

    public String toString(){
        String str = this.funcName + "(";
        if (!(this.args == null)){
            str += this.args;
        }
        return str + ")";
    }

    public String getImplementationArgsString(){
            String str = "";
            int c = 1;
            for (String arg : argList){
                str += ", int32_t arg" + c;
            }
            return str;
    }
}
