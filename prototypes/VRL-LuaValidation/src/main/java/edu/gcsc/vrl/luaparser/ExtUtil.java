package edu.gcsc.vrl.luaparser;

import java.util.List;

public final class ExtUtil {

    private ExtUtil(){
        throw new AssertionError();
    }

    public static void visit(Group g, List<ValueData> dataList){
        ValueData actData = new ValueData(g.getName().toString());
        ValProperty actProp = new ValProperty(g.getName().toString());

        for(Entry e : g.getEntries()){
            if(e instanceof Group){
                if(!ExtractionHelper.checkForValues((Group)e)){
                    for(Entry r : ((Group) e).getEntries()){
                        System.out.println(r.getName().toString()+ " is a Subparam from " + r.getParent().getParent().getName().toString());
                        ValueData subParamVD = new ValueData(r.getName().toString());
                        ValProperty subParamProp = new ValProperty(r.getName().toString());
                        subParamVD.setValprop(subParamProp);
                        actData.addSubParam(subParamVD);
                    }
                } else if (ExtractionHelper.checkVal(e)){
                    System.out.println(e.getName().toString() + " is a Subparam from " + e.getParent().getName().toString());
                    ValueData subParamVD = new ValueData(e.getName().toString());
                    ValProperty subParamProp = new ValProperty(e.getName().toString());
                    subParamVD.setValprop(subParamProp);
                    actData.addSubParam(subParamVD);
                }
            }
        }
        if (actData != null){
            dataList.add(actData);
        }

    }

}
