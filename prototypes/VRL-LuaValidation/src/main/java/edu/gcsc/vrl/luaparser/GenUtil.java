package edu.gcsc.vrl.luaparser;

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
                } else {
                }
            }
        }
    }

    public static void enableWithAllChildNodes(ValueData v) {
        v.setDisabled(false);

        if (v.getOptions() != null) {
            for (ValueData vd : v.getOptions()) {
                if (vd.isOption() || vd.isOptValue()) {
                    vd.setDisabled(false);
                    if (vd.getOptions() != null) {
                        for (ValueData ve : vd.getOptions()) {
                            enableWithAllChildNodes(ve);
                        }
                    }
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

    // Helping-Functions: DependsOn
    // Erweiterte Tiefensuche mit Zyklenerkennung
    // 0 = noch nicht bearbeitet; 1 = in Bearbeitung; 2 = bereits bearbeitet
    public static boolean cycle(int node, int[] markierung,int[][] adjazenz, int len){
        int[] initCoord = getCoor(node,len);
        int x = initCoord[0];
        int y = initCoord[1];
        boolean cyc = false;

        if(markierung[node] == 1){
            cyc = true;
        } else if(markierung[node] == 0){
            markierung[node] = 1;
            for(int i = 0; i < len; i++){
                if(adjazenz[i][node] == 1) {
                    cyc = cycle(i,markierung,adjazenz,len);
                    markierung[node] = 2;
                }
            }
        }
        return cyc;
    }

    // Hilfsfunktion, um für einen bestimmten Knoten die Koordinaten in der Adjazenzmatrix heraus zu finden
    private static int[] getCoor(int nodeNum, int len){
        int x = nodeNum%len;
        int y = nodeNum/len;
        int[] xy = {x,y};
        return xy;
    }

    // Alle Parameter mit einer dependsOn-Abhängigkeit herausfinden( bzgl. validate)
    public static List<ValueData> getDependingValidateValues(List<ValueData> dataToSearch){
        List<ValueData> dependingValues = new ArrayList<>();

        for(ValueData v : dataToSearch){
            if(v.dependsOnValidate()){
                dependingValues.add(v);
            }
            if(v.getOptions() != null){
                for(ValueData vd: v.getOptions()){
                    searchDependingValidateValues(dependingValues,vd);
                }
            }
        }

        return dependingValues;
    }

    // Hilfsfunktion für dependsOn-Funktion
    private static void searchDependingValidateValues(List<ValueData> dependingValues, ValueData actObj){
        if(actObj.dependsOnValidate()){
            dependingValues.add(actObj);
        }
        if(actObj.getOptions() != null){
            for(ValueData v : actObj.getOptions()){
                searchDependingValidateValues(dependingValues,v);
            }
        }
    }

    // Alle dependsOn-Werte für einen Parameter herausfinden und in einer Liste speichern
    public static List<ValueData> validateAValue(ValueData objectToValidate, List<ValueData> runtimeData){
        List<ValueData> validObjDependsOn = new ArrayList<>();


        if(objectToValidate.getValid_dependsOn() != null) {
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
    public static boolean containsVD(ValueData[] vData, ValueData v){
        //System.out.println(vData[0] + " Val: " + v.getValName().get());
        for(int i = 0; i < vData.length; i++){
            if(vData[i] != null){
                if(vData[i].equals(v)){
                    return true;
                }
            }
        }
        return false;
    }

    // Alle Values (nicht die optionalen!) herausfinden TEIL1
    public static List<ValueData> getAllValues(List<ValueData> data){
        List<ValueData> vals = new ArrayList<>();

        for(ValueData v : data){
            if(v.isAValue()){
                vals.add(v);
            }
            if(v.getOptions() != null){
                for(ValueData vd : v.getOptions()) {
                    getVal(vals, vd);
                }
            }
        }

        return vals;
    }

    // Alle Values (nicht die optionalen!) herausfinden TEIL2
    private static void getVal(List<ValueData> vals, ValueData act){
        if(act.isAValue()){
            vals.add(act);
        }
        if(act.getOptions() != null){
            for(ValueData v : act.getOptions()){
                getVal(vals,v);
            }
        }
    }

    private static void visibilityValidation(){}

    private static void validationValidate(){}

    // Helping-Functions: XPath Implementation

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
                    } else if(!firstNode){
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
                    if(temp != null){
                        current = temp;
                    }
                }
            }
        }
        return current;
    }
}
