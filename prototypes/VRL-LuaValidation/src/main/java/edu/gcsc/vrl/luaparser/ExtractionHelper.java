package edu.gcsc.vrl.luaparser;

import java.util.ArrayList;
import java.util.List;

public final class ExtractionHelper {
    /*
    * Hier wird sicher gestellt, dass die Utility-Klasse nicht
    * instanziiert werden kann.
    * */
    private ExtractionHelper(){
        throw new AssertionError();
    }

    /*
    * Hier werden einfach nur die Elemente mit den enthaltenen Informationen
    * geprintet - für Testzwecke!
    * */

    public static void printElemProp(Validator v){
        for(ValueData d: v.getData()){
            System.out.println("Name: "+ d.getValName().get());
            System.out.println("Type: "+ d.getType().get());
            System.out.println("Default: "+ d.getDefaultVal());
            System.out.println("Style: "+ d.getStyle());
            System.out.println("Tooltip: "+ d.getTooltip());
            System.out.println("range min: " + d.getRange_min());
            System.out.println("range max: " + d.getRange_max());
            System.out.println("values: ");
            if(d.getValues() != null) {
                for (double i : d.getValues()) {
                    System.out.println(i);
                }
            }
            System.out.println("____________");
        }
    }

    /*
    * Erweiterte Visit-Funktion, die wieder zwischen 'Value' und 'Group' differenziert.
    * Dabei überprüft sie aber im Falle einer 'Group', ob diese 'Group' ein Wert ist,
    * der nachher auch visualiert werden soll, mittels checkVal().
    * Wenn dem so ist, wird ein neues Objekt von 'ValueData' angelegt und dann nacheinander
    * den Einträgen entsprechend die Informationen herausgefiltert, die für die Visualisierung
    * vonnöten sind. Um die Werte für die range bzw. die values zu bestimmen, wird die Methode
    * getRange() aufgerufen.
    *
    * TO-DO: Visibility muss noch herausgefunden werden!!!!!!!!
    *
    * */
    public static void visitE(Entry e, List<ValueData> dataList){
        if(e instanceof Value) {
            //System.out.println("Val-Name: "+ e.getName().toString() + "  Val: "+ ((Value) e).getValueAsString());

            Value v = (Value) e;

            if (v.isFunction() && "eval".equals(v.getName())) {
                try {
                    // invoke methods without arguments
                    System.out.println("eval: "
                            + v.asFunction().eval().getValueAsString());
                } catch (Exception ex) {
                    //System.out.print("-> ERROR: cannot call f!");
                }
            }
        } else if(e instanceof Group){
            //System.out.println(e.getName().toString() + " + Parent: " + e.getParent().getName().toString());
            if(checkVal(e)) {
                ValueData actData = new ValueData(e.getName().toString());
                System.out.println(e.getName().toString() + " is a Value");
                for (Entry l : ((Group) e).getEntries()) {
                    if(l instanceof Value) {
                        switch (l.getName().toString()) {
                            case "type":
                                actData.setType(((Value) l).getValueAsString());
                                break;
                            case "default":
                                actData.setDefaultVal(((Value) l).getValueAsString());
                                break;
                            case "style":
                                actData.setStyle(((Value) l).getValueAsString());
                                break;
                            case "tooltip":
                                actData.setTooltip(((Value) l).getValueAsString());
                                break;
                        }
                    } else if(l instanceof Group){
                        System.out.println(l.getName().toString() + "    NAME");
                        if(!checkVal(l)) {
                            switch (l.getName().toString()) {
                                case "range":
                                    double[] myValues = getRange((Group) l);
                                    if (myValues.length == 2) {
                                        actData.setRange_min(myValues[0]);
                                        actData.setRange_max(myValues[1]);
                                    } else if (myValues.length > 2) {
                                        actData.setValues(myValues);
                                    }
                                    break;
                                case "visibility":
                                    break;
                            }
                        } else {
                            /*
                             * Fragen, ob Subparameter auch default-Werte, range, usw
                             * haben können.
                             * */
                            /*ValueData subParamVD = new ValueData(l.toString());

                            System.out.println(l.getName().toString() + " test1");
                            if(l.getName().toString().equals("type")){
                                subParamVD.setType(((Value) l).getValueAsString());
                                System.out.println("test2");
                            }
                            actData.addSubParam(subParamVD);*/
                        }
                    }
                }
                if(actData != null) {
                    dataList.add(actData); //vorher myData.add(actData);
                }
            } else {
                for(Entry p : ((Group) e).getEntries()){
                    if(checkVal(p) && !checkForGroups((Group)p)){
                        System.out.println("SUBPARAM: " + p.getName().toString());
                    }
                }
            }

            for(Entry h : ((Group) e).getEntries()){
                //System.out.println("Group-Name: " + e.getName().toString());
                visitE(h, dataList);
            }
        }
    }

    public static boolean checkForGroups(Group g){
        boolean haveGroup;
        if(g.getEntries().size() > 0){
            for (Entry e : ((Group)g).getEntries()){
                if(e instanceof Group){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkForValues(Group g){
        boolean haveGroup;
        if(g.getEntries().size() > 0){
            for (Entry e : ((Group)g).getEntries()){
                if(e instanceof Value){
                    return true;
                }
            }
        }
        return false;
    }

    /*
    * Funktion, die die Werte für range-min, range-max oder values herausfindet.
    * */
    private static double[] getRange(Group range){
        double min = 0;
        double max = 0;
        for(Entry e : range.getEntries()){
            switch(e.getName().toString()){
                case "min":
                    min = Double.parseDouble(((Value)e).getValueAsString());
                    break;
                case "max":
                    max = Double.parseDouble(((Value)e).getValueAsString());
                    double[] vals = {min,max};
                    return vals;
                case "values":
                    int counter = 0;
                    double[] myVals = new double[((Group)e).getEntries().size()];
                    for(Entry h : ((Group)e).getEntries()){
                        myVals[counter] = Double.parseDouble(((Value)h).getValueAsString());
                        counter++;
                    }
                    return myVals;
            }
        }
        return null;
    }

    /*
    * Prüft, ob die ausgewählte 'Group' ein Wert ist, der visualisiert werden soll.
    * Dabei wird geschaut, ob in einem Eintrag der 'Value' "type" existierst.
    * */
    public static boolean checkVal(Entry e) {
        boolean isVal = false;

        if(e instanceof Group) {
            for (Entry g : ((Group) e).getEntries()){
                if(g instanceof Value){
                    if(isEqual(g.getName().toString(),"type")){
                        isVal = true;
                    }
                }
            }
        }
        return isVal;
    }

    private static boolean isEqual(String a, String b){
        boolean eq = false;
        if(a.equals(b)){
            eq = true;
        }
        return eq;
    }

    /*
    * Standart visit-Funktionen, die in der Main sind
    * */

    public static void visit(Entry e, List<ValueData> dataList) {
        System.out.println("TESTING");
        String indent;
        indent = indent(e.distanceToRoot());
        if (e instanceof Group) {
            System.out.println(indent + e.getName() + " = {");
            visit((Group) e, dataList);
            System.out.println(indent + "}");
        } else if (e instanceof Value) {

            System.out.println(indent + e.getName()
                    + " = " + ((Value) e).getValueAsString());

            Value v = (Value) e;

            if (v.isFunction() && "eval".equals(v.getName())) {
                try {
                    // invoke methods without arguments
                    System.out.println(indent + "eval: "
                            + v.asFunction().eval().getValueAsString());
                } catch (Exception ex) {
                    System.out.println(
                            indent + "-> ERROR: cannot call f!");
                }
            }

        } else {
            System.out.println(indent + "unknown type: " + e);
        }
    }

    private static void visit(Group g, List<ValueData> dataList) {
        String indent = indent(g.distanceToRoot() + 1);
        String nameOfVal = "";
        //System.out.println(indent + "#entries: " + g.getEntries().size());

        for (Entry e : g.getEntries()) {
            System.out.println(indent + e.getName() + " = {");
            if(e instanceof Group){
                if(!checkForValues((Group)e)) {
                    if(hasSubParams((Group)e) && !e.getName().toString().equals("problem")) // Konvention, dass erste Group Problem genannt werden muss?
                                                                                            // sonst Unterscheidung schwierig
                    {
                        /*
                        * Hier muss in eine spezielle visitFunktion für Subparameter übergeleitet werden.
                        * Dort werden dann die Subparameter erkannt und an die jeweilige Liste des Value's angehängt
                        * */
                        System.out.println(e.getName().toString() + " has Subparams & is a Val");
                        //visit((Group) e, dataList);
                        ExtUtil.visit((Group)e, dataList);
                    } else {
                        System.out.println(e.getName().toString() + " is a Group");
                        visit((Group) e, dataList);
                    }

                } else { // Hier könnte man noch die Funktion checkVal() einfügen, als Sicherheit. Wenn man später Fälle hat,
                        // die man vorher noch nicht berücksichtigt hat!
                    System.out.println(e.getName().toString() + " is a Val");
                    ExtractionHelper.visitE(e, dataList);
                }
            }
            System.out.println(indent + "}");
        }
    }

    public static boolean hasSubParams(Group g){
        /*
        * Hier wird gecheckt, ob der Value Subparameter hat.
        * Eine Eigenschaft von Subparametern ist, dass ihr Name eine
        * natürliche Zahl ist. -> deswegen wurde der Längencheck
        * in die if-Abfrage eingebaut.
        * */
        for(Entry e : ((Group)g).getEntries()){
            if(checkVal(e) && (e.getName().toString().length() < 2)){
                return true;
            }
        }
        return false;
    }

    /*
    * Indent-Funktion, die in der Main ist
    * */
    private static String indent(int n) {
        String s = "  ";
        String result = "";

        for (int i = 0; i < n; i++) {
            result += s;
        }
        return result;
    }
}
