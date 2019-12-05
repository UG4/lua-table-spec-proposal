package edu.gcsc.vrl.luaparser;

import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides general purpose methods for all kind of use
 */
public final class GenUtil {
    public GenUtil() {
        throw new AssertionError();
    }

    /**
     * Method, that checks, if the value of a specific <code>ValueData</code>-object is in the given
     * value range.
     * Supported data types: 'Integer', 'Integer[]', 'Double', 'Double[]'.
     * Because of not initializing the <code>range_min</code> and <code>range_max</code> properties
     * of a <code>ValueData</code>-object, the methods first checks whether both a not zero.
     *
     * @param v <code>ValueData</code>-object to check
     * @return boolean is in range
     * */
    public static boolean valueIsInRange(ValueData v) {
        if(!(v.getRange_min() == 0 && v.getRange_max() == 0)) {
            if (v.getType().equals("Integer") || v.getType().equals("Double")) {
                if (v.getActData() != null && v.getActData().getValue() != null) {
                    if (v.getValues() != null) {
                        double[] temp = v.getValues();
                        if (v.getType().equals("Integer")) {
                            for (double a : temp) {
                                if (a == (Integer) v.getActData().getValue()) {
                                    return true;
                                }
                            }
                        } else if (v.getType().equals("Double")) {
                            for (double a : temp) {
                                if (a == (Double) v.getActData().getValue()) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    } else {
                        if (v.getType().equals("Double")) {
                            double temp = (Double) v.getActData().getValue();
                            if (temp >= v.getRange_min() && temp <= v.getRange_max()) {
                                return true;
                            } else {
                                return false;
                            }
                        } else if (v.getType().equals("Integer")) {
                            int temp = (Integer) v.getActData().getValue();
                            if (temp >= v.getRange_min() && temp <= v.getRange_max()) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                } else {
                    return true;
                }
            } else if(v.getType().equals("Integer[]")||v.getType().equals("Double[]")){
                if (v.getActData() != null && v.getActData().getValue() != null) {
                    if (v.getValues() != null) {
                        double[] temp = v.getValues();
                        if (v.getType().equals("Integer[]")) {
                            List<Integer> vals = (List<Integer>) v.getActData().getValue();
                            for(int i : vals) {
                                boolean isIn = intInArray(temp,i);
                                if(isIn == false){
                                    return false;
                                }
                            }
                            return true;
                        } else if (v.getType().equals("Double[]")) {
                            List<Double> vals = (List<Double>) v.getActData().getValue();
                            for(double i : vals) {
                                boolean isIn = doubleInArray(temp,i);
                                if(isIn == false){
                                    return false;
                                }
                            }
                            return true;
                        }
                        return false;
                    } else {
                        if (v.getType().equals("Double[]")) {
                            List<Double> vals = (List<Double>) v.getActData().getValue();
                            for(double d : vals){
                                if (!(d >= v.getRange_min() && d <= v.getRange_max())) {
                                    return false;
                                }
                            }
                            return true;
                        } else if (v.getType().equals("Integer[]")) {
                            List<Integer> vals = (List<Integer>) v.getActData().getValue();
                            for(int i : vals){
                                if (!(i >= v.getRange_min() && i <= v.getRange_max())) {
                                    return false;
                                }
                            }
                            return true;
                        }
                    }
                } else {
                    return true;
                }

            } else {
                return true;
            }
        }
        return true;
    }

    /**
     * Checks whether the actual string value is a valid one
     *
     * @param v the <code>ValueData</code>-Object to check
     * @return boolean valid or not
     * **/
    public static boolean stringValIsInRange(ValueData v) {
        String[] validStrings = v.getRangeOfStrings();
        String val = String.valueOf(v.getActData().getValue());

        for(String s : validStrings){
            if(s.equals(val)){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a Array of doubles contains a specific Integer
     *
     * @param arr array
     * @param i specific Integer
     * @return boolean
     * */
    private static boolean intInArray(double[] arr, int i){
        for(double d : arr){
            if(i == d){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a Array of doubles contains a specific Double
     *
     * @param arr array
     * @param d specific Double
     * @return boolean
     * */
    private static boolean doubleInArray(double[] arr, double d){
        for(double db : arr){
            if(db == d){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the param has a optional parameter
     *
     * @param v object to check
     * @return boolean has or has not
     */
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
     */
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
     * @param vd          object to validate
     * @return boolean is valid
     */
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
                    System.out.println("Test " + vd.getValName());
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
     * @param vals  <code>Value</code>-list
     * @param vData data to add
     */
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

    private static ValueData getActualData(ValueData vd, List<ValueData> runtimeData) {
        if (!vd.dependsOnValidate()) {
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
     */
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
     * @param runtimeData      the actual data set
     * @return List<ValueData> list of params
     */
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
     * @param v     object to check
     * @param vData array to check
     * @return boolean contains
     */
    public static boolean containsVD(ValueData[] vData, ValueData v) {
        //System.out.println(vData[0] + " Val: " + v.getValName());
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
     */
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
     * @param treeToSearch     data to search
     * @param xpath_expression xpath command
     * @return <ocde>ValueData</ocde> if found
     */

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
            resultNode = doRelativeSearch(xpath_expression.substring(2), rootNode);
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

    /**
     * Does the relative xpath search.
     *
     * @param rootNode the root node of the tree
     * @param xpath    the xpath-command
     * @return <code>ValueData</code> result node
     */
    private static ValueData doRelativeSearch(String xpath, ValueData rootNode) {
        List<String> path_names = new ArrayList<>();
        char[] charsOfPath = xpath.toCharArray();
        StringBuilder sb = new StringBuilder();

        /**
         * Getting the name of nodes along the path.
         * Seperation character is '/'
         * */
        for (char c : charsOfPath) {
            if (!"/".equals(String.valueOf(c))) {
                sb.append(c);
            } else if ("/".equals(String.valueOf(c))) {
                String temp = sb.toString();
                path_names.add(temp);
                sb = new StringBuilder();
            }
        }

        /**
         * Creating a <code>List</code> with <code>Lists</code> for each node along the path.
         * The node-specific <code>Lists</code> contain each occurrence of the node in the tree.
         * */
        List<List<ValueData>> allOccs = new ArrayList<>();
        for (String s : path_names) {
            List<ValueData> currentOccs = new ArrayList<>();
            searchAllOccurrences(currentOccs, s, rootNode);
            if (!currentOccs.isEmpty()) {
                allOccs.add(currentOccs);
            }
        }

        /**
         * At least you must have one occurrence of each node along the path.
         * If not then the path is not valid.
         * */
        if (allOccs.size() == path_names.size()) {
            return getResultNode(allOccs, path_names);
        } else {
            return null;
        }
    }


    /**
     * Iterates through all nodes and their occurrences and tries to find a valid
     * path with a result node that matches.
     *
     * @param allOccs    <code>List</code> with all occurrences
     * @param path_names <code>List</code> with all names of the nodes
     * @return <code>ValueData</code>-Object result node
     */
    private static ValueData getResultNode(List<List<ValueData>> allOccs, List<String> path_names) {
        for (int i = 0; i < allOccs.size(); i++) {
            for (int j = 0; j < allOccs.get(i).size(); j++) {
                ValueData currentNode = allOccs.get(i).get(j);
                for (int h = 0; h < path_names.size(); h++) {
                    if (h == path_names.size() - 1) {
                        if (currentNode.hasParam(path_names.get(h))) {
                            return currentNode.getParam(path_names.get(h));
                        } else if (currentNode.getValName().equals(path_names.get(h))) {
                            return currentNode;
                        }
                    } else {
                        if (currentNode.hasParam(path_names.get(h))) {
                            currentNode = currentNode.getParam(path_names.get(h));
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Finds all occurrences of a node in a tree of <code>ValueData</code>-objects.
     * Modifies a given <code>List</code>.
     *
     * @param rootNode root node of the tree
     * @param foundOcc occurrences already found
     * @param name     name of the node to find
     */
    private static void searchAllOccurrences(List<ValueData> foundOcc, String name, ValueData rootNode) {
        if (rootNode.hasParam(name)) {
            foundOcc.add(rootNode.getParam(name));
            for (ValueData xd : rootNode.getOptions()) {
                searchAllOccurrences(foundOcc, name, xd);
            }
        } else {
            if (rootNode.getOptions() != null) {
                for (ValueData vd : rootNode.getOptions()) {
                    searchAllOccurrences(foundOcc, name, vd);
                }
            }
        }
    }

    /**
     * Adds quote marks und does string escaping
     *
     * @param input input string
     * @return String modified String
     */
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
                sb.append("\\\\");
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
     */
    public static String doString(String input) {
        char[] temp = input.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char a : temp) {
            if (String.valueOf(a).equals("\"")) {
                sb.append("\\\"");
            } else if (String.valueOf(a).equals("\'")) {
                sb.append("\\\'");
            } else if (String.valueOf(a).equals("\\")) {
                sb.append("\\\\");
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
     */
    public static List<String> getAllVDNames(List<ValueData> data) {
        List<String> names = new ArrayList<>();
        for (ValueData v : data) {
            names.add(v.getValName());
            if (v.getOptions() != null) {
                loopAllVDNames(names, v.getOptions());
            }
        }

        return names;
    }

    private static void loopAllVDNames(List<String> names, List<ValueData> options) {
        for (ValueData v : options) {
            names.add(v.getValName());
            if (v.getOptions() != null) {
                loopAllVDNames(names, v.getOptions());
            }
        }
    }

    /**
     * Returns all <code>ValueData</code>-objects
     *
     * @param data data to search
     * @return list with all <code>ValueData</code>-objects
     */
    public static List<ValueData> getAllVD(List<ValueData> data) {
        List<ValueData> vd = new ArrayList<>();
        for (ValueData v : data) {
            vd.add(v);
            if (v.getOptions() != null) {
                loopAllVD(vd, v.getOptions());
            }
        }

        return vd;
    }

    private static void loopAllVD(List<ValueData> values, List<ValueData> options) {
        for (ValueData v : options) {
            values.add(v);
            if (v.getOptions() != null) {
                loopAllVD(values, v.getOptions());
            }
        }
    }

    public static void printGroupTree(Entry e){
        if(e instanceof Value){
            System.out.println("NAME: " + e.getName() + " | VALUE: " + ((Value) e).getValueAsString());
        } else if(e instanceof Group){
            System.out.println("GROUP-NAME: " + e.getName());
            for(Entry ee : ((Group)e).getEntries()){
                printGroupTree(ee);
            }
        }
    }

    public static void printVdTree(ValueData v, int ind){
        System.out.println(indent(ind) + "Name: " + v.getValName());
        System.out.println(indent(ind) + "path: " + v.getPathToRoot());
        if(v.getOptions().size() > 0) {
            for(ValueData opt : v.getOptions()){
                System.out.println(indent(ind) + "Child name: " + opt.getValName());
                System.out.println(indent(ind) + "path: " + opt.getPathToRoot());
                printVdTreeRec(opt, ind+1);
            }
        }
    }

    public static void printVdTreeRec(ValueData v, int ind){
        if(v.getOptions().size() > 0) {
            for(ValueData opt : v.getOptions()){
                System.out.println(indent(ind) + "Child name: " + opt.getValName());
                System.out.println(indent(ind) + "path: " + opt.getPathToRoot());
                printVdTreeRec(opt, ind+1);
            }
        }
    }

    private static String indent(int n) {
        String s = "  ";
        String result = "";

        for (int i = 0; i < n; i++) {
            result += s;
        }

        return result;
    }

    public static String getRootPath(ValueData v){
        StringBuilder sb = new StringBuilder();
        sb.append(v.getValName());
        if(v.getParentNode() != null) {
            rootPathLoop(v.getParentNode(),sb);
        } else if(v.getParentNode() == null){
            System.out.println("No direct parent node ");
        }
        return sb.toString();
    }

    private static void rootPathLoop(ValueData v, StringBuilder sb) {
        sb.insert(0,v.getValName() +"/");
        if(v.getParentNode() != null){
            rootPathLoop(v.getParentNode(),sb);
        } else if(v.getParentNode() == null) {
            System.out.println("Finished!");
        }
    }
}
