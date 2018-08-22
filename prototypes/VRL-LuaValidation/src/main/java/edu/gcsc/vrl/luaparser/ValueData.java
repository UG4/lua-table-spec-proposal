package edu.gcsc.vrl.luaparser;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

/*
 * Die ValueData Klasse ist dazu da, die einzelnen Daten für die GUI-Generierung
 * zu speichern(über ValProperty). Hiermit wird dann die ObservableList
 * befüllt und die cellFactory kann entsprechende Zellen rendern.
 */
public class ValueData {
    private String valName = "";
    private ObjectProperty<ValProperty> valprop;
    private StringProperty valNameProp;
    private List<ValueData> subParams;

    public ValueData(String valueName){
        this.valName = valueName;
        this.valNameProp = new SimpleStringProperty(valueName);
    }

    // Getter/Setter für das gewrappte ValProperty-Objekt
    public ObjectProperty<ValProperty> getValprop(){ return this.valprop; }

    public void setValprop(ValProperty aProp){ this.valprop = new SimpleObjectProperty<ValProperty>(aProp); }

    // Getter für die Klassenvariablen
    public String getValName(){
        return this.valName;
    }

    public StringProperty getValNameProp() { return this.valNameProp;}

    public List<ValueData> getSubParams() { return subParams; }

    public void setSubParams(List<ValueData> subParams) { this.subParams = subParams; }

    public void addSubParam(ValueData a){ this.subParams.add(a); }

}
