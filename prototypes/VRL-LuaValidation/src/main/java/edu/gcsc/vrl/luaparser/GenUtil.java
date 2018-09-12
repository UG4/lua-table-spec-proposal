package edu.gcsc.vrl.luaparser;

import java.util.List;

public final class GenUtil {
    public GenUtil() {
        throw new AssertionError();
    }

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


    // XPath - Funktionen

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
                    System.out.println("Given XPath returns no result!");
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
                                    System.out.println("Given XPath returns no result!");
                                }
                                currentNameSb = new StringBuilder();
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("FAIL");
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
