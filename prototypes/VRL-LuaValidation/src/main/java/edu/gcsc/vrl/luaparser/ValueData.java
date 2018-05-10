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

    public String getValName(){
        return this.valName;
    }
    public String getType(){
        return this.type;
    }
    public String getDefaultVal(){
        return this.defaultVal;
    }
    public String getStyle(){
        return this.style;
    }
    public String getTooltip(){
        return this.tooltip;
    }
    public double getRangeMin(){
        return this.range_min;
    }
    public double getRangeMax(){
        return this.range_max;
    }
    public boolean getVisibility(){
        return this.visibility;
    }

    public void setType(String aType){
        this.type = aType;
    }
    public void setDefaultVal(String defVal){
        this.defaultVal = defVal;
    }
    public void setStyle(String aStyle){
        this.style = aStyle;
    }
    public void setTooltip(String aTooltip){
        this.tooltip = aTooltip;
    }
    public void setRangeMin(double rangemin){
        this.range_min = rangemin;
    }
    public void set(double rangemax){
        this.range_max = rangemax;
    }
    public void set(boolean vis){
        this.visibility = vis;
    }
}
