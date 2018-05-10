package edu.gcsc.vrl.luaparser;

import java.util.ArrayList;

public class ExtractionHelper {
    private static ArrayList<ValueData> myData = new ArrayList<>();
    public static ArrayList<ValueData> getData(){
        return myData;
    }

    public static void printElements(){
        for(ValueData d : myData){
            System.out.println("Name: "+ d.getValName());
            System.out.println("Type: "+ d.getType());
            System.out.println("Default: "+ d.getDefaultVal());
            System.out.println("Style: "+ d.getStyle());
            System.out.println("Tooltip: "+ d.getTooltip());
            System.out.println("____________");
        }
    }

    public static void visitE(Entry e){
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
                            /*case "values":
                                break;
                            case "visibility":
                                break;*/
                        }
                    } else if(l instanceof Group){
                        switch (l.getName().toString()){
                            case "range":
                                break;
                            case "visibility":
                                break;
                        }
                    }
                }
                if(actData != null) {
                    myData.add(actData);
                }
            }

            for(Entry h : ((Group) e).getEntries()){
                //System.out.println("Group-Name: " + e.getName().toString());
                visitE(h);
            }
        }
    }

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
}