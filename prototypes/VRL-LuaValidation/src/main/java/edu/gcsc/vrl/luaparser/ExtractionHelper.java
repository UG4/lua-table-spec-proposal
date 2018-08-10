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
    * in myData werden alle Werte, die visualisiert werden sollen, gespeichert
    * und durch getData() verfügbar gemacht.
    * Wahrscheinlich unschöner Ansatz - eventuell ändern ?????
    * */
    private static List<ValueData> myData = new ArrayList<>();
    public static List<ValueData> getData(){
        return myData;
    }

    /*
    * Hier werden einfach nur die Elemente mit den enthaltenen Informationen
    * geprintet - für Testzwecke!
    * */

    public static void printElemProp(){
        for(ValueData d: myData){
            System.out.println("Name: "+ d.getValprop().getValue().getValName());
            System.out.println("Type: "+ d.getValprop().getValue().getType());
            System.out.println("Default: "+ d.getValprop().getValue().getDefaultVal());
            System.out.println("Style: "+ d.getValprop().getValue().getStyle());
            System.out.println("Tooltip: "+ d.getValprop().getValue().getTooltip());
            System.out.println("range min: " + d.getValprop().getValue().getRange_min());
            System.out.println("range max: " + d.getValprop().getValue().getRange_max());
            System.out.println("values: ");
            if(d.getValprop().getValue().getValues() != null) {
                for (double i : d.getValprop().getValue().getValues()) {
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
                    //System.out.println("eval: "
                    //        + v.asFunction().eval().getValueAsString());
                } catch (Exception ex) {
                    //System.out.print("-> ERROR: cannot call f!");
                }
            }
        } else if(e instanceof Group){
            if(checkVal(e)) {
                ValueData actData = new ValueData(e.getName().toString());
                ValProperty actProp = new ValProperty(e.getName().toString());
                System.out.println(e.getName().toString() + " is a Value");
                for (Entry l : ((Group) e).getEntries()) {
                    if(l instanceof Value) {
                        switch (l.getName().toString()) {
                            case "type":
                                //actData.setType(((Value) l).getValueAsString());
                                //actData.setTypeProp(((Value) l).getValueAsString());
                                actProp.setType(((Value) l).getValueAsString());
                                break;
                            case "default":
                                //actData.setDefaultVal(((Value) l).getValueAsString());
                                //actData.setDefValProp((((Value) l).getValueAsString()));
                                actProp.setDefaultVal(((Value) l).getValueAsString());
                                break;
                            case "style":
                                //actData.setStyle(((Value) l).getValueAsString());
                                actProp.setStyle(((Value) l).getValueAsString());
                                break;
                            case "tooltip":
                                //actData.setTooltip(((Value) l).getValueAsString());
                                actProp.setTooltip(((Value) l).getValueAsString());
                                break;
                        }
                    } else if(l instanceof Group){
                        switch (l.getName().toString()){
                            case "range":
                                double[] myValues = getRange((Group)l);
                                if(myValues.length == 2){
                                    //actData.setRangeMin(myValues[0]);
                                    //actData.setRangeMax(myValues[1]);
                                    actProp.setRange_min(myValues[0]);
                                    actProp.setRange_max(myValues[1]);
                                } else if (myValues.length > 2){
                                    //actData.setValues(myValues);
                                    actProp.setValues(myValues);
                                }
                                break;
                            case "visibility":
                                break;
                        }
                    }
                }
                actData.setValprop(actProp);
                if(actData != null) {
                    dataList.add(actData); //vorher myData.add(actData);
                }
            }

            for(Entry h : ((Group) e).getEntries()){
                //System.out.println("Group-Name: " + e.getName().toString());
                visitE(h, dataList);
            }
        }
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
            ExtractionHelper.visitE(e, dataList);
        }
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
