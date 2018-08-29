package edu.gcsc.vrl.luaparser;

public class ErrorMessage {
    private String msg;
    private int errorLevel;
    private String paramName;

    public ErrorMessage(String msg, int errorLevel, String paramName){
        this.msg = msg;
        this.errorLevel = errorLevel;
        this.paramName = paramName;
    }

    /*
    * GETTER/SETTER - Methoden
    * */

    public int getErrorLevel() {
        return errorLevel;
    }

    public String getMsg() {
        return msg;
    }

    public String getParamName() {
        return paramName;
    }
}
