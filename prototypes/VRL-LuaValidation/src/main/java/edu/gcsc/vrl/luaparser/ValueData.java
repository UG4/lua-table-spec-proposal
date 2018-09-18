package edu.gcsc.vrl.luaparser;

import javafx.beans.property.*;
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

    public ValueData(String name) {
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
    private String defaultVal;
    private String style = "";
    private String tooltip = "";
    private double range_min;
    private double range_max;
    private double[] values;
    private ActualDataValue actData = null;
    private boolean optGroup;
    private boolean notOptGroup;
    private boolean isOptVal;
    private boolean isAValue = false;
    private boolean dependsOnValidate;
    private boolean dependsOnVisible;
    /* Alle Parameter, die zu visibility gehören*/
    private boolean visibility;
    private String[] vis_dependsOn;
    private Value vis_eval;
    /* Alle Parameter, die zu validation gehören*/
    private boolean validationIsValid;
    private String[] valid_dependsOn;
    private Value valid_eval;

    // Only for GUI
    private SimpleBooleanProperty selected = new SimpleBooleanProperty(false);
    //private boolean selected = false;
    //private boolean disabled = false;
    private SimpleBooleanProperty disabled = new SimpleBooleanProperty(false);
    private ValueData parentNode;


    // GETTER - Methoden
    public ObjectProperty<ValueData> getValProp() {
        return this.valProp;
    }

    public List<ValueData> getOptions() { return this.subParams; }

    public void addSubParam(ValueData a) { this.subParams.add(a); }

    public StringProperty getValName() { return this.valName; }

    public StringProperty getType() { return this.type; }

    public String getDefaultVal() { return this.defaultVal; }

    public String getStyle() { return this.style; }

    public String getTooltip() { return this.tooltip; }

    public double getRange_min() { return this.range_min; }

    public double getRange_max() { return this.range_max; }

    public double[] getValues() { return this.values; }

    public boolean isVisible() { return this.visibility; }

    public ActualDataValue getActData() { return this.actData; }

    public boolean isSelected() { return this.selected.get(); }

    public boolean isDisabled() { return this.disabled.get(); }

    public ValueData getParentNode() { return this.parentNode; }

    public boolean isAValue() { return this.isAValue; }

    public String[] getValid_dependsOn() { return this.valid_dependsOn; }

    public String[] getVis_dependsOn() { return this.vis_dependsOn; }

    public boolean isValidationValid() { return this.validationIsValid; }

    public Value getValid_eval() { return this.valid_eval; }

    public Value getVis_eval() { return this.vis_eval; }

    public boolean isOption() { return this.optGroup; }

    public boolean isOptValue() { return this.isOptVal; }

    public boolean isNotOptGroup() { return this.notOptGroup; }

    public boolean dependsOnValidate(){ return this.dependsOnValidate; }

    public boolean dependsOnVisible() { return this.dependsOnVisible; }

    public SimpleBooleanProperty getSelectedProp(){ return this.selected; }

    public SimpleBooleanProperty disabledProperty() { return this.disabled; }

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

    public void setActData(ActualDataValue dat) { this.actData = dat; }

    public void setDisabled(boolean disabled) { this.disabled.set(disabled); }

    public void setSelection(boolean selected) { this.selected.set(selected); }

    public void setParentNode(ValueData parentNode) { this.parentNode = parentNode; }

    public void setOptional(boolean isOpt){ this.optGroup = isOpt; }

    public void isValue(boolean AValue) { this.isAValue = AValue; }

    public void setOptVal(boolean opt){ this.isOptVal = opt; }

    public void setNotOptGroup(boolean notOpt) { this.notOptGroup = notOpt; }

    public void setValid_dependsOn(String[] valid_dependsOn) { this.valid_dependsOn = valid_dependsOn; }

    public void setValid_eval(Value valid_eval) { this.valid_eval = valid_eval; }

    public void setVis_dependsOn(String[] vis_dependsOn) { this.vis_dependsOn = vis_dependsOn; }

    public void setVis_eval(Value vis_eval) { this.vis_eval = vis_eval; }

    public void setValidationIsValid(boolean validationIsValid) { this.validationIsValid = validationIsValid; }

    public void setDependsOnValidate(boolean dependsOn){ this.dependsOnValidate = dependsOn; }

    public void setDependsOnVisible(boolean dependsOn) { this.dependsOnVisible = dependsOn; }

    // Objektmethoden

    public ValueData getParam(String subParamName) {
        if (getOptions() != null) {
            for (ValueData v : getOptions()) {
                if (subParamName.equals(v.getValName().get())) {
                    return v;
                }
            }
        }
        //System.out.println("Sub-Param doesn't exist!");
        return null;
    }

    public void setParam(String subParamName, Object val) {
        ValueData v = getParam(subParamName);
        if (v != null) {
            v.getActData().setValue(val);
        } else {
            System.out.println("The Sub-Parameter: " + subParamName + " doesn't exists!");
        }

    }

    public boolean hasParam(String subParamName) {
        if (getOptions() != null) {
            for (ValueData v : getOptions()) {
                if (subParamName.equals(v.getValName().get())) {
                    return true;
                }
            }
        }
        //System.out.println("Sub-Param doesn't exist!");
        return false;
    }

    public ValueData xpath(String path) {
        char[] charsOfPath = path.toCharArray();
        ValueData currentNode = this;
        StringBuilder currentNameSb = new StringBuilder();

        for (char character : charsOfPath) {
            if (!"\\".equals(String.valueOf(character))) {
                currentNameSb.append(character);
            } else if("\\".equals(String.valueOf(character))){
                String currentName = currentNameSb.toString();

                if(currentName.equals(".")){
                    currentNode = currentNode;
                } else if(currentName.equals("..")){
                    if(this.getParentNode() != null){
                        currentNode = currentNode.getParentNode();
                    } else {
                        System.out.println(currentNode.getValName().get() + " hasn't a Parent-Node!");
                    }
                } else {
                    if(currentNode.hasParam(currentName)){
                        currentNode = currentNode.getParam(currentName);
                    }
                }
                currentNameSb = new StringBuilder();
            }
        }
        return currentNode;
    }

    public ValueData getRootNode() {
        if(this.getParentNode() != null){
            return loopRootNode(this);
        } else {
            return this;
        }
    }

    private ValueData loopRootNode(ValueData v){
        ValueData rootNode;

        if(v.getParentNode() != null){
            rootNode = loopRootNode(v.getParentNode());
            return rootNode;
        } else {
            return v;
        }
    }

    public void setSelectedNew(boolean sel){
        ValueData act = this;
        if(!this.isDisabled()) {
            if (sel) {
                if (isOption() || isOptValue()) {
                    setSelection(true);
                    if (getParentNode() != null && getParentNode().getOptions() != null) {
                        GenUtil.selectParents(this);
                        for (ValueData v : getParentNode().getOptions()) {
                            if ((v.isOption()||v.isOptValue()) && !v.equals(act)) {
                                GenUtil.disableWithAllChildNodes(v);
                            } else if(v.isAValue()){
                                v.setDisabled(true);
                            }
                        }
                    }
                }
            } else if(!sel){
                if (isOption() || isOptValue()) {
                    setSelection(false);
                    if (getParentNode() != null && getParentNode().getOptions() != null) {
                        for (ValueData v : getParentNode().getOptions()) {
                            if ((v.isOption()||v.isOptValue())) {
                                GenUtil.enableWithAllChildNodes(v);
                            } else if(v.isAValue()) {
                                v.setDisabled(false);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean hasOptValue(){
        if(getOptions() != null){
            for(ValueData v : getOptions()){
                if(v.isOptValue()){
                    return true;
                }
            }
        }
        return false;
    }
}
