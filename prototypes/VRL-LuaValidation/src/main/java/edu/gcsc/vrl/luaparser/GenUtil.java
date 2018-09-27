package edu.gcsc.vrl.luaparser;

import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides general purpose methods for all kind of use
 * */
public final class GenUtil {
    public GenUtil() {
        throw new AssertionError();
    }

    /**
     * Checks whether the param has a optional parameter
     *
     * @param v object to check
     * @retun boolean has or has not
     * */
    public static boolean haveOptValue(ValueData v) {
        if (!v.isOption()) {
            if (v.getOptions() != null) {
                for (ValueData vd : v.getOptions()) {
                    if (vd.isOptValue()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks, whether the object has a selected optional parameter
     *
     * @param v object to check
     * @return boolean has or has not
     * */
    public static boolean haveOptValSelected(ValueData v) {
        if (!v.isOption()) {
            if (v.getOptions() != null) {
                for (ValueData vd : v.getOptions()) {
                    if (vd.isOptValue() && vd.isSelected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * validates the given object regarding its validation-property
     *
     * @param runtimeData actual data set
     * @param vd object to validate
     * @return boolean is valid
     * */
    public static boolean validate(ValueData vd, List<ValueData> runtimeData) {
        if (vd.getValid_eval() != null) {
            List<ValueData> dependsOn = new ArrayList<>();
            if (vd.dependsOnValidate()) {
                dependsOn = validateAValue(vd, runtimeData);
            }
            boolean valid = true;

            for (int i = 0; i < dependsOn.size(); i++) {
                boolean temp = validate(dependsOn.get(i), runtimeData);
                if (temp == false) {
                    valid = false;
                }
            }

            if (valid) {

                List<ValueData> vals = new ArrayList<>();

                for (ValueData v : dependsOn) {
                    vals.add(getActualData(v, runtimeData));
                }

                List<Value> valsForEval = new ArrayList<>();

                if (vd.getActData() != null && vd.getActData().getValue() != null) {
                    System.out.println("Test " + vd.getValName().get());
                    doArgList(vd, valsForEval);
                }

                if (!vals.isEmpty() && !valsForEval.isEmpty()) {
                    doArgList(vals, valsForEval);
                }

                String resultOfEval = "";

                try {
                    resultOfEval = vd.getValid_eval().asFunction().eval(valsForEval).getValueAsString();
                } catch (Exception ex) {
                    System.out.println("Cannot call f !");
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
                String name = v.getValName().get();

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
        String name = v.getValName().get();

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

    private static ValueData getActualData(ValueData vd, List<ValueData> runtimeData) {
        if (!vd.dependsOnValidate()) {
            if (vd.getActData() != null && vd.getActData().getValue() != null) {
                return vd;
            } else {
                System.out.println(vd.getValName().get() + " has no Value!");
                return null;
            }
        } else {
            List<ValueData> dependsOn = validateAValue(vd, runtimeData);
            boolean valid = true;

            for (int i = 0; i < dependsOn.size(); i++) {
                boolean temp = validate(dependsOn.get(i), runtimeData);
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


    /**
     * Returns all parameters, which depend on other parameter
     * regarding the validation-property
     *
     * @param dataToSearch data to search
     * @return List<ValueData> depending parameters
     * */
    public static List<ValueData> getAllDependingValidateValues(List<ValueData> dataToSearch) {
        List<ValueData> dependingValues = new ArrayList<>();

        for (ValueData v : dataToSearch) {
            if (v.dependsOnValidate() && !dependingValues.contains(v)) {
                dependingValues.add(v);
            }
            if (v.getOptions() != null) {
                for (ValueData vd : v.getOptions()) {
                    searchDependingValidateValues(dependingValues, vd);
                }
            }
        }

        return dependingValues;
    }

    private static void searchDependingValidateValues(List<ValueData> dependingValues, ValueData actObj) {
        if (actObj.dependsOnValidate() && !dependingValues.contains(actObj)) {
            dependingValues.add(actObj);
        }
        if (actObj.getOptions() != null) {
            for (ValueData v : actObj.getOptions()) {
                searchDependingValidateValues(dependingValues, v);
            }
        }
    }

    /**
     * Returns all the parameters a parameter depends on regarding
     * validation-property
     *
     * @param objectToValidate object to check
     * @param runtimeData the actual data set
     * @return List<ValueData> list of params
     *
     * */
    public static List<ValueData> validateAValue(ValueData objectToValidate, List<ValueData> runtimeData) {
        List<ValueData> validObjDependsOn = new ArrayList<>();


        if (objectToValidate.getValid_dependsOn() != null) {
            for (String dependsOnVal : objectToValidate.getValid_dependsOn()) {
                ValueData act = doXPath(runtimeData, dependsOnVal);
                if (act != null) {
                    validObjDependsOn.add(act);
                }
            }
        }
        return validObjDependsOn;
    }

    /**
     * Helping method that check whether a array contains a specific
     * <code>ValueData</code>-object.
     *
     * @param v object to check
     * @param vData array to check
     * @return boolean contains
     * */
    public static boolean containsVD(ValueData[] vData, ValueData v) {
        //System.out.println(vData[0] + " Val: " + v.getValName().get());
        for (int i = 0; i < vData.length; i++) {
            if (vData[i] != null) {
                if (vData[i].equals(v)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns all parameters from a list of <code>ValueData</code>-objects
     *
     * @param data data to check
     * @return List<ValueData> all parameters
     * */
    public static List<ValueData> getAllValues(List<ValueData> data) {
        List<ValueData> vals = new ArrayList<>();

        for (ValueData v : data) {
            if (v.isAValue() || v.isOptValue()) {
                vals.add(v);
            }
            if (v.getOptions() != null) {
                for (ValueData vd : v.getOptions()) {
                    getVal(vals, vd);
                }
            }
        }

        return vals;
    }

    private static void getVal(List<ValueData> vals, ValueData act) {
        if (act.isAValue() || act.isOptValue()) {
            vals.add(act);
        }
        if (act.getOptions() != null) {
            for (ValueData v : act.getOptions()) {
                getVal(vals, v);
            }
        }
    }

    /**
     * Execute a simple variant of a xpath command.
     * / - starts a absolute search
     * ./ - starts a relative search
     *
     * @param treeToSearch data to search
     * @param xpath_expression xpath command
     * @return <ocde>ValueData</ocde> if found
     * */

    public static ValueData doXPath(List<ValueData> treeToSearch, String xpath_expression) {
        ValueData rootNode = new ValueData("problem");
        for (ValueData x : treeToSearch) {
            rootNode.addSubParam(x);
            x.setParentNode(rootNode);
        }
        treeToSearch.add(0, rootNode);
        ValueData resultNode = null;

        if (xpath_expression.startsWith("/")) {
            resultNode = doAbsoluteSearch(rootNode, xpath_expression.substring(1));
        } else if (xpath_expression.startsWith("./")) {
            resultNode = doRelativeSearch(rootNode, xpath_expression.substring(2));
        }

        treeToSearch.remove(0);

        return resultNode;
    }

    private static ValueData doAbsoluteSearch(ValueData rootNode, String xpath) {
        char[] charsOfPath = xpath.toCharArray();
        ValueData currentNode = rootNode;
        StringBuilder currentNameSb = new StringBuilder();

        for (char character : charsOfPath) {
            if (!"/".equals(String.valueOf(character))) {
                currentNameSb.append(character);
            } else if ("/".equals(String.valueOf(character))) {
                String currentName = currentNameSb.toString();

                if (currentNode.hasParam(currentName)) {
                    currentNode = currentNode.getParam(currentName);
                } else {

                }
                currentNameSb = new StringBuilder();
            }
        }
        return currentNode;
    }

    private static ValueData doRelativeSearch(ValueData rootNode, String xpath) {
        boolean firstNode = true;
        char[] charsOfPath = xpath.toCharArray();
        ValueData currentNode = rootNode;
        StringBuilder currentNameSb = new StringBuilder();

        if (rootNode != null) {
            for (char character : charsOfPath) {
                if (!"/".equals(String.valueOf(character))) {
                    currentNameSb.append(character);
                } else if ("/".equals(String.valueOf(character))) {
                    String currentName = currentNameSb.toString();
                    if (firstNode) {
                        firstNode = false;
                        ValueData actual = searchCompleteDoc(rootNode, currentName);
                        if (actual != null) {
                            currentNode = actual;
                        }
                        currentNameSb = new StringBuilder();
                    } else if (!firstNode) {
                        if (currentNode.getOptions() != null) {
                            if (currentNode.getOptions().size() > 0) {
                                if (currentNode.hasParam(currentName)) {
                                    currentNode = currentNode.getParam(currentName);
                                } else {

                                }
                                currentNameSb = new StringBuilder();
                            }
                        }
                    }
                }
            }
        } else {

        }
        return currentNode;
    }

    private static ValueData searchCompleteDoc(ValueData rootNode, String name) {
        ValueData current = null;
        if (rootNode.hasParam(name)) {
            ValueData returnV = rootNode.getParam(name);
            current = returnV;
        } else {
            if (rootNode.getOptions() != null) {
                for (ValueData v : rootNode.getOptions()) {
                    ValueData temp = searchCompleteDoc(v, name);
                    if (temp != null) {
                        current = temp;
                    }
                }
            }
        }
        return current;
    }

    /**
     * Adds quote marks und does string escaping
     *
     * @param input input string
     * @return String modified String
     * */
    public static String doQuoteMark(String input) {
        char[] temp = input.toCharArray();
        StringBuilder sb = new StringBuilder();

        sb.append("\"");
        for (char a : temp) {
            if (String.valueOf(a).equals("\"")) {
                sb.append("\\\"");
            } else if (String.valueOf(a).equals("\'")) {
                sb.append("\\\'");
            } else if (String.valueOf(a).equals("\\")) {
                sb.append("\\");
            } else {
                sb.append(a);
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    /**
     * Does string escaping
     *
     * @param input input string
     * @return modified string
     * */
    public static String doString(String input) {
        char[] temp = input.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char a : temp) {
            if (String.valueOf(a).equals("\"")) {
                sb.append("\"");
            } else if (String.valueOf(a).equals("\'")) {
                sb.append("\'");
            } else if (String.valueOf(a).equals("\\")) {
                sb.append("\\");
            } else {
                sb.append(a);
            }
        }
        return sb.toString();
    }

    /**
     * Returns names of all <code>ValueData</code>-objects
     *
     * @param data data to search
     * @return List of strings
     * */
    public static List<String> getAllVDNames(List<ValueData> data){
        List<String> names = new ArrayList<>();
        for(ValueData v : data){
            names.add(v.getValName().get());
            if(v.getOptions() != null){
                loopAllVDNames(names ,v.getOptions());
            }
        }

        return names;
    }

    private static void loopAllVDNames(List<String> names, List<ValueData> options){
        for(ValueData v : options){
            names.add(v.getValName().get());
            if(v.getOptions() != null){
                loopAllVDNames(names,v.getOptions());
            }
        }
    }

    /**
     * Returns all <code>ValueData</code>-objects
     *
     * @param data data to search
     * @return list with all <code>ValueData</code>-objects
     * */
    public static List<ValueData> getAllVD(List<ValueData> data){
        List<ValueData> vd = new ArrayList<>();
        for(ValueData v : data){
            vd.add(v);
            if(v.getOptions() != null){
                loopAllVD(vd ,v.getOptions());
            }
        }

        return vd;
    }

    private static void loopAllVD(List<ValueData> values, List<ValueData> options){
        for(ValueData v : options){
            values.add(v);
            if(v.getOptions() != null){
                loopAllVD(values,v.getOptions());
            }
        }
    }
}
