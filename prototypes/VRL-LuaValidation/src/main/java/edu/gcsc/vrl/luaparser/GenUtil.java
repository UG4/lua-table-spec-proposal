package edu.gcsc.vrl.luaparser;

import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

public final class GenUtil {
    public GenUtil() {
        throw new AssertionError();
    }

    // Helping-Functions: Selection Model for Params

    public static void disableWithAllChildNodes(ValueData v) {
        v.setDisabled(true);
        if (v.getOptions() != null) {
            for (ValueData vd : v.getOptions()) {
                if (vd.isOption() || vd.isOptValue()) {
                    vd.setDisabled(true);
                    if (vd.getOptions() != null) {
                        for (ValueData ve : vd.getOptions()) {
                            disableWithAllChildNodes(ve);
                        }
                    }
                } else if(vd.isAValue()){
                    vd.setDisabled(true);
                }
            }
        }
    }

    public static void enableWithAllChildNodes(ValueData v) {
        v.setDisabled(false);
        v.setSelection(false);

        if (v.getOptions() != null) {
            for (ValueData vd : v.getOptions()) {
                if (vd.isOption() || vd.isOptValue()) {
                    vd.setDisabled(false);
                    vd.setSelection(false);
                    if (vd.getOptions() != null) {
                        for (ValueData ve : vd.getOptions()) {
                            enableWithAllChildNodes(ve);
                        }
                    }
                } else if(vd.isAValue()) {
                    vd.setDisabled(false);
                }
            }
        }
    }

    public static void selectAllParentNodes(ValueData v) {
        if (v.getParentNode() != null) {
            if (v.getParentNode() != v.getRootNode()) {
                if (v.getParentNode().isOption()) {
                    v.getParentNode().setSelectedNew(true);
                    if (v.getParentNode().getParentNode() != null) {
                        selectAllParentNodes(v.getParentNode());
                    }
                } else {
                    selectAllParentNodes(v.getParentNode());
                }
            }
        }
    }

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

    // Hilfsfunktionen dependsOn()
    public static boolean validate(ValueData vd, List<ValueData> runtimeData) {
        // Als erstes wird überprüft, ob der Parameter eine eval-Funktion hat.
        // Wenn nicht, dann ist die Validation immer true, da ja kein 'falscher'
        // Fall eintreten kann.
        if (vd.getValid_eval() != null) {
            // Hier werden alle Parameter herausgesucht, von denen er abhängig ist
            List<ValueData> dependsOn = new ArrayList<>();
            if (vd.dependsOnValidate()) {
                dependsOn = validateAValue(vd, runtimeData);
            }
            boolean valid = true;
            // Die Parameter von denen er abhängig ist, werden validiert.
            // Erst wenn alle diese Parameter valide sind, kann der eigentliche
            // Parameter validiert werden.
            for (int i = 0; i < dependsOn.size(); i++) {
                boolean temp = validate(dependsOn.get(i), runtimeData);
                if (temp == false) {
                    valid = false;
                }
            }
            // Hier wird der Parameter validiert
            if(valid) {

                List<ValueData> vals = new ArrayList<>();

                // Alle Werte der von denen der Parameter abhängig ist, werden herausgesucht
                for (ValueData v : dependsOn) {
                    vals.add(getActualData(v, runtimeData));
                }

                List<Value> valsForEval = new ArrayList<>();

                // Hier werden die Werte, die als Argument für die eval-Funktion dienen
                // in einer Liste gespeichert.
                if (vd.getActData() != null && vd.getActData().getValue() != null) {
                    System.out.println("Test " + vd.getValName().get());
                    doArgList(vd, valsForEval);
                }

                if (!vals.isEmpty() && !valsForEval.isEmpty()) {
                    doArgList(vals, valsForEval);
                }

                String resultOfEval = "";
                // Jetzt wird versucht die eval-Funktion mit den Argumenten aufzurufen
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
                // Falls einer der Parameter, von denen der eigentliche Parameter abhängig ist, nicht valide ist,
                // kann der zu überprüfende Parameter auch nicht valide sein -> return false
                return false;
            }

        } else {
            return true;
        }

    }

    private static void doArgList(List<ValueData> vData, List<Value> vals) {
        // Hier wird die List von Values erstellt, die die Argumente für die eval-Funktion enthalten.
        // WEITERE DATENTYPEN HINZUFÜGEN: ARRAYS UND FUNCTION
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
        // Hier kann ein einzelnes Argument der Argumentenliste hinzugefügt werden
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
        // Hier werden die Werte der Parameter herausgesucht, die übergeben werden sollen
        if (!vd.dependsOnValidate()) {
            // Wenn der Parameter von keinem Anderen abhängig ist, kann direkt das ValueData-Objekt
            // mit dem Value zurückgegeben werden.(dafür muss dieses ValueData-Objekt natürlich auch einen
            // Value besitzen)
            if (vd.getActData() != null && vd.getActData().getValue() != null) {
                return vd;
            } else {
                System.out.println(vd.getValName().get() + " has no Value!");
                return null;
            }
        } else {
            // Falls der Parameter abbhängig ist, müssen die Parameter von denen der zu überprüfende
            // Parameter abhängig ist, natürlich auch erst überprüft werden.
            List<ValueData> dependsOn = validateAValue(vd, runtimeData);
            boolean valid = true;

            //Falls einer der Parameter nicht valide ist, wird null zurückgegeben.
            // EVENTUELL ÄNDERN
            // Wenn alle Parameter valide sind, wird der zu überprüfende Parameter zurückgegeben.

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
        // Temporär
        return null;
    }


    // Alle Parameter mit einer dependsOn-Abhängigkeit herausfinden( bzgl. validate)
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

    // Hilfsfunktion für dependsOn-Funktion
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

    // Alle dependsOn-Werte für einen Parameter herausfinden und in einer Liste speichern
    public static List<ValueData> validateAValue(ValueData objectToValidate, List<ValueData> runtimeData) {
        List<ValueData> validObjDependsOn = new ArrayList<>();


        if (objectToValidate.getValid_dependsOn() != null) {
            for (String dependsOnVal : objectToValidate.getValid_dependsOn()) {
                ValueData act = doXPath(runtimeData, dependsOnVal);
                if (act != null) {
                    validObjDependsOn.add(act);
                }
            }
            // Was wird mit den Werten gemacht
        }


        return validObjDependsOn;

        /*if(objectToValidate.getVis_dependsOn() != null){
            for(String dependsOnVal : objectToValidate.getVis_dependsOn()){
                ValueData act = doXPath(runtimeData, dependsOnVal);
                if(act != null){
                    visibObjDependsOn.add(act);
                }
            }
            // Was wird mit den Werten gemacht
        }*/

    }

    // Hilfsfunktion, die checkt, ob ein Array ein bestimmtes ValueData-Objekt enthält
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

    // Alle Values (nicht die optionalen!) herausfinden TEIL1
    public static List<ValueData> getAllValues(List<ValueData> data) {
        List<ValueData> vals = new ArrayList<>();

        for (ValueData v : data) {
            if (v.isAValue()) {
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

    // Alle Values (nicht die optionalen!) herausfinden TEIL2
    private static void getVal(List<ValueData> vals, ValueData act) {
        if (act.isAValue()) {
            vals.add(act);
        }
        if (act.getOptions() != null) {
            for (ValueData v : act.getOptions()) {
                getVal(vals, v);
            }
        }
    }

    private static void visibilityValidation() {
    }

    private static void validationValidate() {
    }

    // Helping-Functions: XPath Implementation

    public static ValueData doXPath(List<ValueData> treeToSearch, String xpath_expression) {
        // Hier muss noch eine Lösung gefunden werden, um nicht immer 'problem' behelfsmässig als
        // root-Node hinzu zu fügen
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

    // Sonstige Hilfsfunktionen
    public static String deleteQuoteMark(String input){
        char[] temp = input.toCharArray();
        StringBuilder sb = new StringBuilder();

        for(char a : temp){
            if(!String.valueOf(a).equals("\"")){
                sb.append(a);
            }
        }
        return sb.toString();
    }

    public static String doQuoteMark(String input){
        char[] temp = input.toCharArray();
        StringBuilder sb = new StringBuilder();

        sb.append("\"");
        for(char a : temp){
            if(!String.valueOf(a).equals("\"")){
                sb.append(a);
            }
        }
        sb.append("\"");
        return sb.toString();
    }
}
