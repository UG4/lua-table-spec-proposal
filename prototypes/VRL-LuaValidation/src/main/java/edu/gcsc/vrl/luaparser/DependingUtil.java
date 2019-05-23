package edu.gcsc.vrl.luaparser;

import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides methods helping to validate the visibility-property
 * */
public final class DependingUtil {

    private DependingUtil() {
        throw new AssertionError();
    }

    /**
     * Returns all the parameters a parameter depends on
     * regarding its visibility-property
     *
     * @param objectToValidate object to check
     * @param runtimeData the actual data set
     * @return List<ValueData> list of params
     *
     * */
    public static List<ValueData> validateAValue(ValueData objectToValidate, List<ValueData> runtimeData) {
        List<ValueData> visibObjDependsOn = new ArrayList<>();


        if (objectToValidate.getVis_dependsOn() != null) {
            for (String dependsOnVal : objectToValidate.getVis_dependsOn()) {
                ValueData act = GenUtil.doXPath(runtimeData, dependsOnVal);
                if (act != null) {
                    visibObjDependsOn.add(act);
                }
            }
        }
        return visibObjDependsOn;
    }

    /**
     * Returns all parameters, which depend on other parameter
     * regarding the visibility-property
     *
     * @param dataToSearch data to search
     * @return List<ValueData> depending parameters
     * */
    public static List<ValueData> getAllDependingVisibleValues(List<ValueData> dataToSearch) {
        List<ValueData> dependingValues = new ArrayList<>();

        for (ValueData v : dataToSearch) {
            if (v.dependsOnVisible() && !dependingValues.contains(v)) {
                dependingValues.add(v);
            }
            if (v.getOptions() != null) {
                for (ValueData vd : v.getOptions()) {
                    searchDependingVisibleValues(dependingValues, vd);
                }
            }
        }

        return dependingValues;
    }

    /**
     * Helping method
     * */
    private static void searchDependingVisibleValues(List<ValueData> dependingValues, ValueData actObj) {
        if (actObj.dependsOnVisible() && !dependingValues.contains(actObj)) {
            dependingValues.add(actObj);
        }
        if (actObj.getOptions() != null) {
            for (ValueData v : actObj.getOptions()) {
                searchDependingVisibleValues(dependingValues, v);
            }
        }
    }

    /**
     * validates the given object regarding its visibility-property
     *
     * @param runtimeData actual data set
     * @param vd object to validate
     * @return boolean is valid
     * */
    public static boolean validateVisible(ValueData vd, List<ValueData> runtimeData) {
        /**
         * First: checks, whether the object has a evaluation-function
         * */
        if (vd.getVis_eval() != null) {
            // Get all parameters, the parameter depends on
            List<ValueData> dependsOn = new ArrayList<>();
            if (vd.dependsOnVisible()) {
                dependsOn = validateAValue(vd, runtimeData);
            }
            boolean valid = true;
            // validate these parameters
            for (int i = 0; i < dependsOn.size(); i++) {
                boolean temp = validateVisible(dependsOn.get(i), runtimeData);
                if (temp == false) {
                    valid = false;
                }
            }
            // validate the parameter to check
            if (valid) {

                List<ValueData> vals = new ArrayList<>();

                for (ValueData v : dependsOn) {
                    vals.add(getActualData(v, runtimeData));
                }

                List<Value> valsForEval = new ArrayList<>();

                // Saves all values as an Value-object
                if (vd.getActData() != null && vd.getActData().getValue() != null) {
                    System.out.println("Test " + vd.getValName());
                    doArgList(vd, valsForEval);
                }

                if (!vals.isEmpty() && !valsForEval.isEmpty()) {
                    doArgList(vals, valsForEval);
                }

                String resultOfEval = "";
                // try to invoke the evalute-function with the arguments
                try {
                    resultOfEval = vd.getVis_eval().asFunction().eval(valsForEval).getValueAsString();
                } catch (Exception ex) {
                    System.out.println("Cannot call f !");
                    ex.printStackTrace(System.err);
                }

                boolean result = false;
                if (!resultOfEval.isEmpty()) {
                    result = Boolean.valueOf(resultOfEval);
                }

                return result;
            } else {
                return false;
            }

        } else {
            return true;
        }

    }

    /**
     * Creates the arguments as <code>Value</code>-objects
     *
     * @param vals <code>Value</code>-list
     * @param vData data to add
     * */
    private static void doArgList(List<ValueData> vData, List<Value> vals) {
        for (ValueData v : vData) {
            if (v.getActData() != null && v.getActData().getValue() != null) {
                Object temp = v.getActData().getValue();
                String name = v.getValName();

                if (v.getActData().getType().equals("String")) {
                    LuaValue lua = LuaValue.valueOf(String.valueOf(temp));
                    Value val = new Value(lua, name);
                    vals.add(val);
                } else if (v.getActData().getType().equals("Integer")) {
                    LuaValue lua = LuaValue.valueOf(Integer.parseInt(temp.toString()));
                    Value val = new Value(lua, name);
                    vals.add(val);
                } else if (v.getActData().getType().equals("Double")) {
                    LuaValue lua = LuaValue.valueOf(Double.parseDouble(temp.toString()));
                    Value val = new Value(lua, name);
                    vals.add(val);
                } else if (v.getActData().getType().equals("Boolean")) {
                    LuaValue lua = LuaValue.valueOf(Boolean.valueOf(temp.toString()));
                    Value val = new Value(lua, name);
                    vals.add(val);
                }
            }
        }
    }

    private static void doArgList(ValueData v, List<Value> vals) {
        Object temp = v.getActData().getValue();
        String name = v.getValName();

        if (v.getActData().getType().equals("String")) {
            LuaValue lua = LuaValue.valueOf(String.valueOf(temp));
            Value val = new Value(lua, name);
            vals.add(val);
        } else if (v.getActData().getType().equals("Integer")) {
            LuaValue lua = LuaValue.valueOf(Integer.parseInt(temp.toString()));
            Value val = new Value(lua, name);
            vals.add(val);
        } else if (v.getActData().getType().equals("Double")) {
            LuaValue lua = LuaValue.valueOf(Double.parseDouble(temp.toString()));
            Value val = new Value(lua, name);
            vals.add(val);
        } else if (v.getActData().getType().equals("Boolean")) {
            LuaValue lua = LuaValue.valueOf(Boolean.valueOf(temp.toString()));
            Value val = new Value(lua, name);
            vals.add(val);
        }

    }

    /**
     * Helping method
     * */
    private static ValueData getActualData(ValueData vd, List<ValueData> runtimeData) {
        if (!vd.dependsOnVisible()) {
            if (vd.getActData() != null && vd.getActData().getValue() != null) {
                return vd;
            } else {
                System.out.println(vd.getValName() + " has no Value!");
                return null;
            }
        } else {
            List<ValueData> dependsOn = validateAValue(vd, runtimeData);
            boolean valid = true;

            for (int i = 0; i < dependsOn.size(); i++) {
                boolean temp = validateVisible(dependsOn.get(i), runtimeData);
                if (temp == false) {
                    valid = false;
                }
            }
            if (valid) {
                return vd;
            }
        }
        return null;
    }
}

