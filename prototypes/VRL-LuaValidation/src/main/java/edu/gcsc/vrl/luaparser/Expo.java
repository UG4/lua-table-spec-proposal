package edu.gcsc.vrl.luaparser;

import java.util.List;

public final class Expo {
    public Expo(){ throw new AssertionError();}

    public static void doExport(List<ValueData> data){
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < data.size(); i++){
            sb.append(data.get(i).getValName().get());
            if(data.get(i).isNestedGroup()){
                if(i < data.size()){
                    doNested(data.get(i).getOptions(),sb,false);
                } else {
                    doNested(data.get(i).getOptions(),sb,true);
                }
            } else if(data.get(i).isAValue()){
                if(i < data.size()){
                    doVal(data.get(i),sb,false);
                } else {
                    doVal(data.get(i),sb,true);
                }
            } else if(data.get(i).havingParams()){
                if(i < data.size()){
                    doSub(data.get(i).getOptions(),sb,false);
                } else {
                    doSub(data.get(i).getOptions(),sb,true);
                }
            }
            if(i < data.size()){
                sb.append(",");
            } else {
                sb.append("}");
            }
        }
    }

    private static void doNested(List<ValueData> vl, StringBuilder sb, boolean last){
        sb.append("{");
        for(int i = 0; i < vl.size(); i++){
            if(vl.get(i).isNestedGroup()){
                doNested(vl.get(i).getOptions(),sb,last);
            } else if(vl.get(i).isAValue()){
                sb.append(vl.get(i).getValName().get() + " = " + vl.get(i).getActData().getValue());
                if(i < vl.size()){
                    sb.append(",");
                } else {
                    if(last){
                        sb.append("}");
                    } else {
                        sb.append("},");
                    }
                }
            }
        }
    }

    private static void doVal(ValueData v, StringBuilder sb, boolean last){
        sb.append(v.getActData().getValue());
        if(last){
            sb.append("}");
        } else {
            sb.append(",");
        }
    }

    private static void doSub(List<ValueData> vl, StringBuilder sb, boolean last){
        for(int i = 0; i < vl.size(); i++){
            if(vl.get(i).isNestedGroup()){
                doNested(vl.get(i).getOptions(),sb, last);
            } else if(vl.get(i).isAValue()){
                doVal(vl.get(i),sb, last);
            } else if(vl.get(i).havingParams()){
                doSub(vl.get(i).getOptions(),sb, last);
            }
        }
    }
}
