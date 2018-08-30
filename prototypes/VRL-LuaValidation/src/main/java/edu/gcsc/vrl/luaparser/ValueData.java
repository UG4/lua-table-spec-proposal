package edu.gcsc.vrl.luaparser;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

/*
 * Die ValueData Klasse ist dazu da, die einzelnen Daten für die GUI-Generierung
 * zu speichern. Hiermit wird dann die ObservableList
 * befüllt und die cellFactory kann entsprechende Zellen rendern.
 */
public class ValueData {
    /*
    * Konstruktor
    * */

    public ValueData(String name){
        this.valName = new SimpleStringProperty(name);
        this.valProp = new SimpleObjectProperty<ValueData>(this);
    }

    /*
    * Klassenvariablen
    * */

    private ObjectProperty<ValueData> valProp;
    private List<ValueData> subParams = new ArrayList<>();

    private StringProperty valName;
    private StringProperty type;
    private String defaultVal = "";
    private String style = "";
    private String tooltip = "";
    private double range_min;
    private double range_max;
    private double[] values;
    private boolean visibility;
    private String actData = "";
    private boolean isNestedGroup;
    // Only for GUI
    private boolean selected = false;
    private boolean disabled = false;
    private ValueData parentNode;


    // GETTER - Methoden
    public ObjectProperty<ValueData> getValProp() { return this.valProp; }

    public List<ValueData> getOptions() { return this.subParams; }

    public void addSubParam(ValueData a){ this.subParams.add(a); }

    public StringProperty getValName() { return this.valName; }

    public StringProperty getType() { return this.type; }

    public String getDefaultVal() { return this.defaultVal; }

    public String getStyle() { return this.style; }

    public String getTooltip() { return this.tooltip; }

    public double getRange_min() { return this.range_min; }

    public double getRange_max() { return this.range_max; }

    public double[] getValues() { return this.values; }

    public boolean isVisible() { return this.visibility; }

    public String getActData() { return this.actData; }

    public boolean isNestedGroup() { return this.isNestedGroup; }

    public boolean isSelected() { return selected; }

    public boolean isDisabled() { return disabled; }

    public ValueData getParentNode() { return parentNode; }

    // SETTER - Methoden
    public void setSubParams(List<ValueData> subParams) { this.subParams = subParams; }

    public void setType(String type) { this.type = new SimpleStringProperty(type); }

    public void setDefaultVal(String defaultVal) { this.defaultVal = defaultVal; }

    public void setStyle(String style) { this.style = style; }

    public void setTooltip(String tooltip) { this.tooltip = tooltip; }

    public void setRange_min(double range_min) { this.range_min = range_min; }

    public void setRange_max(double range_max) { this.range_max = range_max; }

    public void setValues(double[] values) { this.values = values; }

    public void setVisibility(boolean visibility) { this.visibility = visibility; }

    public void setActData(String dat) {this.actData = dat; }

    public void setNestedGroup(boolean isNestedGroup) {this.isNestedGroup = isNestedGroup; }

    public void setDisabled(boolean disabled) { this.disabled = disabled; }

    public void setSelected(boolean selected) { this.selected = selected; }

    public void setParentNode(ValueData parentNode) { this.parentNode = parentNode; }

    // Objektmethoden

    public ValueData getParam(String subParamName){
        if(getOptions() != null){
            for(ValueData v : getOptions()){
                if(subParamName.equals(v.getValName().get())){
                    return v;
                }
            }
        }
        System.out.println("Sub-Param doesn't exist!");
        return null;
    }

    public void setParam(String subParamName, Object val){
        Object value;
        switch(getType().get()) {
            case "Integer":
                try {
                    value = (Integer) val;
                } catch (ClassCastException c){
                    System.out.println("Your given Option-Value doesn't fit with the Values Type!");
                }
                break;

            case "Double":
                try {
                    value = (Double) val;
                } catch (ClassCastException c){
                    System.out.println("Your given Option-Value doesn't fit with the Values Type!");
                }
                break;

            case "String":
                try {
                    value = (String) val;
                } catch (ClassCastException c){
                    System.out.println("Your given Option-Value doesn't fit with the Values Type!");
                }
                break;
        }
        ValueData v = getParam(subParamName);
        if(v != null){
            /*
            * Hier muss der Code implementiert werden, der den Wert an die entsprechende Stelle
            * des ValueData - Objekts setzt.
            * Kann allerdings erst gemacht werden, wenn entschieden wurde, wie man den realen Wert abspeichert!
            * */
        } else {
            System.out.println("The Sub-Parameter: " + subParamName + " doesn't exists!");
        }
    }

    public boolean hasParam(String subParamName){
        if(getOptions() != null){
            for(ValueData v : getOptions()){
                if(subParamName.equals(v.getValName().get())){
                    return true;
                }
            }
        }
        System.out.println("Sub-Param doesn't exist!");
        return false;
    }
}
