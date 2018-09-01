package edu.gcsc.vrl.luaparser;

import java.util.List;

public final class ExportLua {
    public ExportLua(){ throw new AssertionError(); }

    //private static StringBuilder sb = new StringBuilder();

    public static void doExport(List<ValueData> data){
        StringBuilder sb = new StringBuilder();
        sb.append("root={\n");
        for(ValueData v: data){
            if(v.isAValue()){
                if(v.getActData() != null && v.getActData().getValue() != null) {
                    sb.append(v.getValName().get()+" = "+v.getActData().getValue().toString()+",\n");
                } else {
                    System.out.println(v.getValName().get() + " has no Value to save!");
                }
            } else if(!v.isAValue()){
                sb.append(v.getValName().get()+" = "+ "{\n");
                if(v.getOptions() != null){
                    checkVals(v.getOptions(), sb);
                }
                sb.append("\n }");
            }
        }
        sb.append("\n}");
        String lua = sb.toString();
        sb = new StringBuilder();
        System.out.print(lua);
    }

    private static void checkVals(List<ValueData> data, StringBuilder sb){
        for(ValueData v: data){
            if(v.isAValue() && v.isSelected()){
                if(v.getActData() != null && v.getActData().getValue() != null) {
                    sb.append(v.getValName().get()+" = "+v.getActData().getValue().toString() + ",\n");
                } else {
                    System.out.println(v.getValName().get() + " has no Value to save!");
                }
            } else if(!v.isAValue() && v.isSelected()){
                //sb.append(v.getValName().get()+" = "+ "{\n");
                if(v.getOptions() != null){
                    checkVals(v.getOptions(), sb);
                }
                sb.append("\n }");
            }
        }
    }

}
