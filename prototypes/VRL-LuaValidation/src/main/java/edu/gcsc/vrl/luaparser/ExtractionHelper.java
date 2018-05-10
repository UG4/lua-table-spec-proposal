package edu.gcsc.vrl.luaparser;

public class ExtractionHelper {
    public static void visitE(Entry e){
        if(e instanceof Value) {
            System.out.println("Val-Name: "+ e.getName().toString() + "  Val: "+ ((Value) e).getValueAsString());

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
                System.out.println(e.getName().toString() + " is a Value");
                for (Entry l : ((Group) e).getEntries()) {

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
