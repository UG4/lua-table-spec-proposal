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

    public ValueData(String name){
        this.valName = new SimpleStringProperty(name);
        this.valProp = new SimpleObjectProperty<ValueData>(this);
    }


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

    // GETTER - Methoden
    public ObjectProperty<ValueData> getValProp() { return this.valProp; }

    public List<ValueData> getSubParams() { return this.subParams; }

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


}
