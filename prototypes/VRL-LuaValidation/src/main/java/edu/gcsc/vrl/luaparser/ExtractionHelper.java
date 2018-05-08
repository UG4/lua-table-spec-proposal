package edu.gcsc.vrl.luaparser;

public class ExtractionHelper {
    public static void visitE(Entry e){
        if(e instanceof Value) {
            System.out.println("Val-Name: "+ e.getName().toString() + "  Val: "+ ((Value) e).getValue().toString());
            //System.out.println("Val-Name: "+ e.getName().toString());
        } else if(e instanceof Group){
            for(Entry h : ((Group) e).getEntries()){
                System.out.println("Group-Name: " + e.getName().toString());
                visitE(h);
            }
        }
    }
}
