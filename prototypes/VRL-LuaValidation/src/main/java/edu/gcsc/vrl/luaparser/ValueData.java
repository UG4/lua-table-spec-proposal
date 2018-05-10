package edu.gcsc.vrl.luaparser;

public class ValueData {
    private String valName = "";
    private String type = "";
    private String defaultVal = "";
    private String style = "";
    private String tooltip = "";
    private double range_min;
    private double range_max;
    private boolean visibility;

    public ValueData(String valueName){
        this.valName = valueName;
    }

    private String getValName(){
        return this.valName;
    }
    private String getType(){
        return this.type;
    }
    private String getDefaultVal(){
        return this.defaultVal;
    }
    private String getStyle(){
        return this.style;
    }
    private String getTooltip(){
        return this.tooltip;
    }
    private double getRangeMin(){
        return this.range_min;
    }
    private double getRangeMax(){
        return this.range_max;
    }
    private boolean getVisibility(){
        return this.visibility;
    }

    private void setType(String aType){
        this.type = aType;
    }
    private void setDefaultVal(String defVal){
        this.defaultVal = defVal;
    }
    private void setStyle(String aStyle){
        this.style = aStyle;
    }
    private void setTooltip(String aTooltip){
        this.tooltip = aTooltip;
    }
    private void setRangeMin(double rangemin){
        this.range_min = rangemin;
    }
    private void set(double rangemax){
        this.range_max = rangemax;
    }
    private void set(boolean vis){
        this.visibility = vis;
    }
}
