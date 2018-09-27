package edu.gcsc.vrl.luaparser;

/*
* This class should be used as an structure to provide error messages.
* */
public class ErrorMessage {
    private String msg;
    private int errorLevel;
    private String paramName;

    /*
    * Constructormethode. Creates a new ErrorMessage-object
    *
    * @param msg error message
    * @param errorLevel level of error
    * @param paramName parameter which is involved
    * @return ErrorMessage new object
    * */
    public ErrorMessage(String msg, int errorLevel, String paramName){
        this.msg = msg;
        this.errorLevel = errorLevel;
        this.paramName = paramName;
    }


    /*
    * Getting the error level
    *
    * @return errorLevel int
    * */
    public int getErrorLevel() {
        return errorLevel;
    }

    /*
    * Getting the error message
    *
    * @return msg String
    * */
    public String getMsg() {
        return msg;
    }

    /*
    * Getting the name of the parameter,
    * which is involved.
    *
    * @return paramName String
    * */
    public String getParamName() {
        return paramName;
    }
}
