package edu.gcsc.vrl.luaparser;

import javafx.beans.property.*;

public class ValueDataFX extends ValueData {

    private ObjectProperty<ValueData> valProperty;

    private StringProperty valNameProperty = new SimpleStringProperty();
    private StringProperty typeProperty = new SimpleStringProperty();

    // Only for GUI
    private SimpleBooleanProperty selected = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty disabled = new SimpleBooleanProperty(false);


    public ValueDataFX(String name) {
        super(name);
        setValName(name);

    }

    public StringProperty valNameProperty() {
        return valNameProperty;
    }

    @Override
    public String getValName() {
        return valNameProperty().get();
    }

    @Override
    public void setValName(String valName) {
        valNameProperty().set(valName);
    }

    @Override
    public void setType(String type) {
        typeProperty().set(type);
    }

    public StringProperty typeProperty() {
        return typeProperty;
    }

    @Override
    public String getType() {
        return typeProperty().get();
    }

    public ObjectProperty<ValueData> valProperty() {

        if(valProperty==null) {
            valProperty = new SimpleObjectProperty<>(super.getVal());
        }

        return this.valProperty;
    }

    @Override
    public void setVal(ValueData vd) {
        valProperty().set(vd);
    }

    @Override
    public ValueData getVal() {
        return valProperty().get();
    }


    public void setSelected(boolean selected) {
        this.selectedProperty().set(selected);
    }


    public BooleanProperty selectedProperty() {
        return this.selected;
    }

    public boolean isSelected() {
        return selectedProperty().get();
    }

    public BooleanProperty disabledProperty() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabledProperty().set(disabled);
    }

    public boolean isDisabled() {
        return disabledProperty().get();
    }
}