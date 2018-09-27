package edu.gcsc.vrl.luaparser;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;

/*
* This class represents the data model based on the MVC-concept.
* It saves all kind of information about a specific element from a
* validation-spec or a lua-file with explicit parameters.
* It provides various methods to access and manipulate the data
* of the object.
* */
public class ValueData {
    /*
    * Constructor
    *
    * @param name name of object
    * @return <code>ValueData</code>
    * */

    public ValueData(String name) {
        this.valName = new SimpleStringProperty(name);
        this.valProp = new SimpleObjectProperty<ValueData>(this);
    }

    private ObjectProperty<ValueData> valProp;
    private List<ValueData> subParams = new ArrayList<>();

    private StringProperty valName;
    private StringProperty type;
    private Object defaultVal;
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
    // All parameters, which depend on the visibility-property
    private boolean visibility = true;
    private String[] vis_dependsOn;
    private Value vis_eval;
    // All parameters, which depend on the validation-property
    private boolean validationIsValid = true;
    private String[] valid_dependsOn;
    private Value valid_eval;

    // Only for GUI
    private SimpleBooleanProperty selected = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty disabled = new SimpleBooleanProperty(false);

    private ValueData parentNode;


    // GETTER - methods

    /*
    * Returns the wrapped <code>ObjectProperty</code> of the object
    *
    * @return valProp wrapped object
    * */
    public ObjectProperty<ValueData> getValProp() {
        return this.valProp;
    }

    /*
    * Returns all sub-parameters
    *
    * @return subParams subparams
    * */
    public List<ValueData> getOptions() {
        return this.subParams;
    }

    /*
    * Adds a sub-param to the object list of sub-params
    *
    * @param a object to add
    * */
    public void addSubParam(ValueData a) {
        this.subParams.add(a);
    }

    /*
    * Returns the wrapped name of the object
    *
    * @return valName name
    * */
    public StringProperty getValName() {
        return this.valName;
    }

    /*
    * Returns the wrapped type of the object
    *
    * return type datatype
    * */
    public StringProperty getType() {
        return this.type;
    }

    /*
    * Returns the default-value of the object
    *
    * @return defaultVal default value
    * */
    public Object getDefaultVal() {
        return this.defaultVal;
    }

    /*
    * Returns the style-property
    *
    * @return style style-property
    * */
    public String getStyle() {
        return this.style;
    }

    /*
    * Returns the tooltip-message
    *
    * @return tooltip messsage
    * */
    public String getTooltip() {
        return this.tooltip;
    }

    /*
    * Returns the minimum value of the range
    *
    * @return range_min minimum value
    * */
    public double getRange_min() {
        return this.range_min;
    }

    /*
    * Returns the maximum value of the range
    *
    * @return range_max maximum value
    * */
    public double getRange_max() {
        return this.range_max;
    }

    /*
    * Returns all values from the value range
    *
    * @return values array with values
    * */
    public double[] getValues() {
        return this.values;
    }

    /*
    * If the visibility-property validated succesfully,
    * it returns <code>true</code>, otherwise <code>false</code>
    *
    * @return visibility visibility-property
    * */
    public boolean isVisible() {
        return this.visibility;
    }

    /*
    * Returns the ActualDataValue-oject, that depends on the
    * current object.
    *
    * @return actData object with type and value
    * */
    public ActualDataValue getActData() {
        return this.actData;
    }

    /**
    * Returns whether the object is selected
    * from the user or not.
    *
    * @return selected.get() unwrapped selected-property
    * */
    public boolean isSelected() {
        return this.selected.get();
    }

    /**
    * Returns whether the object is disabled or not.
     *
     * @return disabled.get() unwrapped disabled-property
    * */
    public boolean isDisabled() {
        return this.disabled.get();
    }

    /**
     * Returns the parent node of the object
     *
     * @return parentNode parent node
     */
    public ValueData getParentNode() {
        return this.parentNode;
    }

    /**
     * Returns whether the object is a parameter or not
     *
     * @return isAValue is param or not
     * */
    public boolean isAValue() {
        return this.isAValue;
    }

    /**
     * Returns the params the object depends on
     * regarding its validation-property
     *
     * @return valid_dependsOn array with depending params
     * */
    public String[] getValid_dependsOn() {
        return this.valid_dependsOn;
    }

    /**
     * Returns the params the object depends on
     * regarding its visibility-property
     *
     * @return vis_dependsOn array with depending params
     * */
    public String[] getVis_dependsOn() {
        return this.vis_dependsOn;
    }

    /**
     * Returns whether the validation-property is valid.
     *
     * @return validationValid boolean
     * */
    public boolean isValidationValid() {
        return this.validationIsValid;
    }

    /**
     * Returns the validation evaluation function
     * as an <code>Value</code>-object
     *
     * @return valid_eval <code>Value</code>-object
     * */
    public Value getValid_eval() {
        return this.valid_eval;
    }

    /**
     * Returns the visibility evaluation function
     * as an <code>Value</code>-object
     *
     * @return vis_eval <code>Value</code>-object
     * */
    public Value getVis_eval() {
        return this.vis_eval;
    }

    /**
    * Returns whether the object is a option or not
    *
    * @return optGroup is option
    * */
    public boolean isOption() {
        return this.optGroup;
    }

    /**
     * Returns whether the object is a optional parameter or not
     *
     * @return isOptVal is optional param
     * */
    public boolean isOptValue() {
        return this.isOptVal;
    }

    /**
     * Returns whether the object is a not-optional group or not
     *
     * @return notOptGroup is not-opt
     * */
    public boolean isNotOptGroup() {
        return this.notOptGroup;
    }

    /**
    * Returns whether the object depending on other params
     * regarding its validate-property
     *
     * @return dependsOnValidate boolean is depending
    * */
    public boolean dependsOnValidate() {
        return this.dependsOnValidate;
    }

    /**
     * Returns whether the object depending on other params
     * regarding its visibility-property
     *
     * @return dependsOnVisibile boolean is depending
     * */
    public boolean dependsOnVisible() {
        return this.dependsOnVisible;
    }

    /**
     * Returns the wrapped selected-property
     *
     * @return selected selected SimpleBooleanProperty
     * */
    public SimpleBooleanProperty getSelectedProp() {
        return this.selected;
    }

    /**
     * Returns the wrapped disabled-property
     *
     * @return disabled disabled SimpleBooleanProperty
     * */
    public SimpleBooleanProperty disabledProperty() {
        return this.disabled;
    }

    // SETTER - Methoden
    public void setSubParams(List<ValueData> subParams) {
        this.subParams = subParams;
    }

    public void setType(String type) {
        this.type = new SimpleStringProperty(type);
    }

    public void setDefaultVal(Object defaultVal) {
        this.defaultVal = defaultVal;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public void setRange_min(double range_min) {
        this.range_min = range_min;
    }

    public void setRange_max(double range_max) {
        this.range_max = range_max;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void setActData(ActualDataValue dat) {
        this.actData = dat;
    }

    public void setDisabled(boolean disabled) {
        this.disabled.set(disabled);
    }

    public void setSelection(boolean selected) {
        this.selected.set(selected);
    }

    public void setParentNode(ValueData parentNode) {
        this.parentNode = parentNode;
    }

    public void setOptional(boolean isOpt) {
        this.optGroup = isOpt;
    }

    public void isValue(boolean AValue) {
        this.isAValue = AValue;
    }

    public void setOptVal(boolean opt) {
        this.isOptVal = opt;
    }

    public void setNotOptGroup(boolean notOpt) {
        this.notOptGroup = notOpt;
    }

    public void setValid_dependsOn(String[] valid_dependsOn) {
        this.valid_dependsOn = valid_dependsOn;
    }

    public void setValid_eval(Value valid_eval) {
        this.valid_eval = valid_eval;
    }

    public void setVis_dependsOn(String[] vis_dependsOn) {
        this.vis_dependsOn = vis_dependsOn;
    }

    public void setVis_eval(Value vis_eval) {
        this.vis_eval = vis_eval;
    }

    public void setValidationIsValid(boolean validationIsValid) {
        this.validationIsValid = validationIsValid;
    }

    public void setDependsOnValidate(boolean dependsOn) {
        this.dependsOnValidate = dependsOn;
    }

    public void setDependsOnVisible(boolean dependsOn) {
        this.dependsOnVisible = dependsOn;
    }

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
            } else if ("\\".equals(String.valueOf(character))) {
                String currentName = currentNameSb.toString();

                if (currentName.equals(".")) {
                    currentNode = currentNode;
                } else if (currentName.equals("..")) {
                    if (this.getParentNode() != null) {
                        currentNode = currentNode.getParentNode();
                    } else {
                        System.out.println(currentNode.getValName().get() + " hasn't a Parent-Node!");
                    }
                } else {
                    if (currentNode.hasParam(currentName)) {
                        currentNode = currentNode.getParam(currentName);
                    }
                }
                currentNameSb = new StringBuilder();
            }
        }
        return currentNode;
    }

    public ValueData getRootNode() {
        if (this.getParentNode() != null) {
            return loopRootNode(this);
        } else {
            return this;
        }
    }

    private ValueData loopRootNode(ValueData v) {
        ValueData rootNode;

        if (v.getParentNode() != null) {
            rootNode = loopRootNode(v.getParentNode());
            return rootNode;
        } else {
            return v;
        }
    }

    public void setSelectedNew(boolean sel) {
        if (!this.isDisabled() && (this.isOption() || this.isOptValue())) {
            if (sel) {
                this.setSelection(true);
                if (this.isOption() || this.isOptValue()) {

                    if (this.getParentNode() != null) {
                        // ParentNodes selecten
                        selectParentNodes(this);
                        // Alle Siblings disablen, die auch optional sind
                        for (ValueData sib : this.getParentNode().getOptions()) {
                            if (!sib.equals(this)) {
                                disableWithChild(sib);
                            }
                        }


                    }
                }
            } else if (!sel) {
                this.setSelection(false);
                if (this.isOption() || this.isOptValue()) {
                    if (this.getParentNode() != null) {
                        if (this.getParentNode().getOptions() != null) {
                            for (ValueData sib : this.getParentNode().getOptions()) {

                                enableWithChild(sib);

                            }
                        }
                    }
                }

            }
        } else if(!this.isDisabled() && (this.isNotOptGroup() || this.isAValue())){
            if(this.getParentNode() != null){
                selectParentNodes(this);
            }
        }
    }

    private void enableWithChild(ValueData v) {
        if (v.isOption() || v.isOptValue()) {
            v.setSelection(false);
            v.setDisabled(false);

            if (v.getOptions() != null) {
                for (ValueData x : v.getOptions()) {
                    enableWithChild(x);
                }
            }
        } else {
            v.setDisabled(false);
            if (v.getOptions() != null) {
                for (ValueData x : v.getOptions()) {
                    enableWithChild(x);
                }
            }
        }
    }

    private void disableWithChild(ValueData v) {
        if (v.isOption() || v.isOptValue()) {
            v.setDisabled(true);
            v.setSelection(false);
            if (v.getOptions() != null) {
                for (ValueData x : v.getOptions()) {
                    disableWithChild(x);
                }
            }
        } else if (v.isNotOptGroup()) {
            v.setDisabled(true);
            if (v.getOptions() != null) {
                for (ValueData x : v.getOptions()) {
                    disableWithChild(x);
                }
            }
        } else if (v.isAValue()) {
            if (v.getParentNode() != null) {
                if (!v.getParentNode().isSelected() || v.getParentNode().isDisabled()) {
                    v.setDisabled(true);
                }
            }
        }
    }

    private void selectParentNodes(ValueData v) {
        if (v.getParentNode() != null) {
            if (v.getParentNode().isOption() || v.getParentNode().isOptValue()) {
                v.getParentNode().setSelectedNew(true);
                selectParentNodes(v.getParentNode());
            } else {
                selectParentNodes(v.getParentNode());
            }
        }
    }

    public boolean hasOptValue() {
        if (getOptions() != null) {
            for (ValueData v : getOptions()) {
                if (v.isOptValue()) {
                    return true;
                }
            }
        }
        return false;
    }
}
