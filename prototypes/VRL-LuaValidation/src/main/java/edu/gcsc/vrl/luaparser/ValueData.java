package edu.gcsc.vrl.luaparser;

import javafx.beans.property.StringProperty;

/*
 * Die ValueData Klasse ist dazu da, die einzelnen Daten für die GUI-Generierung
 * strukturiert zu erfassen und zu speichern. Hiermit wird dann die ObservableList
 * befüllt und die cellFactory kann entsprechende Zellen rendern.
 */
public class ValueData {
    private String valName = "";
    private String type = "";
    private String defaultVal = "";
    private String style = "";
    private String tooltip = "";
    private double range_min;
    private double range_max;
    private double[] values;
    private boolean visibility;

    private StringProperty defVal;
    private StringProperty valNameProp;

    public ValueData(String valueName){
        this.valName = valueName;
    }

    public String getValName(){
        return this.valName;
    }

    public StringProperty getDefProp() {return defVal;}
    public StringProperty getValNameProp() {return valNameProp;}

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
    public double[] getValues(){
        return this.values;
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
    public void setRangeMax(double rangemax){
        this.range_max = rangemax;
    }
    public void setVisibility(boolean vis){
        this.visibility = vis;
    }
    public void setValues(double[] vals){
        this.values = vals;
    }
}
