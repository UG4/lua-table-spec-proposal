package edu.gcsc.vrl.luaparser;

public class ValProperty {
    private String valName = "";
    private String type = "";
    private String defaultVal = "";

    public ValProperty(String valueName, String atype){
        this.valName = valueName;
        this.type = atype;
    }

    public ValProperty(String valueName){
        this.valName = valueName;
    }

    public String getValName() {
        return valName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValName(String valName) {
        this.valName = valName;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }
}
