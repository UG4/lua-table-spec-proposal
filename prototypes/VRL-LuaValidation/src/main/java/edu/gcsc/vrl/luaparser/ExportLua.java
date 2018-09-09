package edu.gcsc.vrl.luaparser;

import java.util.List;

public final class ExportLua {
    public ExportLua() {
        throw new AssertionError();
    }

    /*public static String doExport(List<ValueData> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("problem = {\n");

        for (int i = 0; i < data.size(); i++) {
            sb.append(indent(1) + data.get(i).getValName().get() + " = ");
            if (data.get(i).isNestedGroup()) {
                if (i < data.size() - 1) {
                    doNested2(data.get(i).getOptions(), sb, 1);
                } else {
                    doLastNested(data.get(i).getOptions(), sb, 1);
                }
            } else if (data.get(i).isAValue()) {
                if (i < data.size() - 1) {
                    doVal(data.get(i), sb, false, false);
                } else {
                    doVal(data.get(i), sb, true, false);
                }
            } else if (data.get(i).isOption()) {
                System.out.println("LALA: "+data.get(i).getValName().get());
                if (i < data.size() - 1) {
                    doSub(data.get(i).getOptions(), sb, false, 1);
                } else {
                    doSub(data.get(i).getOptions(), sb, true, 1);
                }
            }
        }
        sb.append("}");
        String str = sb.toString();
        //System.out.println(str);
        return str;
    }


    private static void doLastNested(List<ValueData> vl, StringBuilder sb, int dis) {
        sb.append("{\n");

        for (int i = 0; i < vl.size(); i++) {
            if (vl.get(i).isNestedGroup() && vl.get(i).isSelected()) {
                if (i < vl.size() - 1) {
                    sb.append(indent(dis + 1) + vl.get(i).getValName().get() + " = ");
                    doNested2(vl.get(i).getOptions(), sb, dis + 1);
                } else {
                    sb.append(indent(dis + 1) + vl.get(i).getValName().get() + " = ");
                    doLastNested(vl.get(i).getOptions(), sb, dis + 1);
                }
            } else if (vl.get(i).isAValue() && vl.get(i).isSelected()) {
                sb.append(indent(dis + 1) + vl.get(i).getValName().get() + " = " + vl.get(i).getActData().getValue());
                if (i < vl.size() - 1) {
                    sb.append(",\n");
                } else {

                }
            } else if(vl.get(i).isOption() && vl.get(i).isSelected()) {
                if (i < vl.size() - 1) {
                    //sb.append(indent(dis + 1) + vl.get(i).getValName().get() + " = ");
                    //doNested2(vl.get(i).getOptions(), sb, dis + 1);
                    doSub(vl.get(i).getOptions(), sb, false,dis+1);
                } else {
                    //sb.append(indent(dis + 1) + vl.get(i).getValName().get() + " = ");
                    //doLastNested(vl.get(i).getOptions(), sb, dis + 1);
                    doSub(vl.get(i).getOptions(), sb, true,dis+1);
                }
            }
        }
        sb.append("\n" + indent(dis) + "}\n");

    }

    private static void doNested2(List<ValueData> vl, StringBuilder sb, int dis) {
        sb.append("{\n");
        for (int i = 0; i < vl.size(); i++) {
            if (vl.get(i).isNestedGroup() && vl.get(i).isSelected()) {
                if (i < vl.size() - 1) {
                    sb.append(indent(dis + 1) + vl.get(i).getValName().get() + " = \n");
                    doNested2(vl.get(i).getOptions(), sb, dis);
                } else {
                    sb.append(indent(dis + 1) + vl.get(i).getValName().get() + " = \n");
                    doLastNested(vl.get(i).getOptions(), sb, dis);
                }
            } else if (vl.get(i).isAValue() && vl.get(i).isSelected()) {
                sb.append(indent(dis + 2) + vl.get(i).getValName().get() + " = " + vl.get(i).getActData().getValue());
                if (i < vl.size() - 1) {
                    sb.append(",\n");
                } else {
                    // Zum Testen
                }
            } else if(vl.get(i).isOption() && vl.get(i).isSelected()) {
                if (i < vl.size() - 1) {
                    //sb.append(indent(dis + 1) + vl.get(i).getValName().get() + " = ");
                    //doNested2(vl.get(i).getOptions(), sb, dis + 1);
                    doSub(vl.get(i).getOptions(), sb, false,dis+1);
                } else {
                    //sb.append(indent(dis + 1) + vl.get(i).getValName().get() + " = ");
                    //doLastNested(vl.get(i).getOptions(), sb, dis + 1);
                    doSub(vl.get(i).getOptions(), sb, true,dis+1);
                }
            }
        }
        sb.append("\n" + indent(dis) + "},\n");
    }

    private static void doVal(ValueData v, StringBuilder sb, boolean last, boolean opt) {
        if (v.getActData() != null) {
            if(v.getParentNode() != null) {
                if (opt || v.getParentNode().isNestedGroup()) {
                    sb.append(v.getValName().get() + " 11= " + v.getActData().getValue());
                } else {
                    sb.append(v.getActData().getValue());
                }
            }
        } else {
            System.out.println(v.getValName().get() + " has no Value!");
        }
        if (last) {
            sb.append("\n");
        } else {
            sb.append(",\n");
        }
    }

    private static void doSub(List<ValueData> vl, StringBuilder sb, boolean last, int dis) {
        System.out.println("TEST");
        for (int i = 0; i < vl.size(); i++) {
            if (vl.get(i).isNestedGroup() && vl.get(i).isSelected()) {
                if (last) {
                    doLastNested(vl.get(i).getOptions(), sb, dis);
                } else {
                    doNested2(vl.get(i).getOptions(), sb, dis);
                }
            } else if (vl.get(i).isAValue() && vl.get(i).isSelected()) {
                sb.append(indent(dis));
                if(vl.get(i).getParentNode().isOption()){
                    System.out.println("fals "+vl.get(i).getValName().get());
                    doVal(vl.get(i), sb, last, false);
                } else {
                    doVal(vl.get(i), sb, last, true);
                }
            } else if (vl.get(i).havingParamsExtended() && vl.get(i).isSelected()) {
                doSub(vl.get(i).getOptions(), sb, last, dis);
            }
        }
    }*/
    public static String doExport(List<ValueData> data){
        StringBuilder sb = new StringBuilder();
        sb.append("problem = {\n");

        for(int i = 0; i < data.size(); i++){
            sb.append(indent(1) + data.get(i).getValName().get() + " = ");

            if(data.get(i).isAValue()){
                System.out.println("HALLO1");
                if(i < data.size()-1){
                    //doVal(data.get(i), sb, 1, false);
                    if(data.get(i).getActData() != null && data.get(i).getActData().getValue() != null){
                        sb.append(data.get(i).getActData().getValue() + ",\n");
                    }
                } else {
                    //doVal(data.get(i), sb, 1, true);
                    if(data.get(i).getActData() != null && data.get(i).getActData().getValue() != null){
                        sb.append(data.get(i).getActData().getValue() + "\n");
                    }
                }
            } else if(data.get(i).isOption()){
                System.out.println("HALLO2");
                if(i < data.size()-1){
                    doOption(data.get(i).getOptions(), sb, 1, false);
                } else {
                    doOption(data.get(i).getOptions(), sb, 1, true);
                }
            } else if(!data.get(i).isOption()){
                System.out.println("HALLO3");
                if(i < data.size()-1){
                    doNotOpt(data.get(i).getOptions(), sb , 1, false);
                } else {
                    doNotOpt(data.get(i).getOptions(), sb , 1, true);
                }
            } else if(data.get(i).isOptValue()){
                System.out.println("HALLO4");
                if(i < data.size()-1){
                    doOptVal(data.get(i), sb, 1, false);
                } else {
                    doOptVal(data.get(i), sb, 1 , true);
                }
            }
        }

        sb.append("}");
        String str = sb.toString();

        return str;
    }

    public static void doVal(ValueData vData, StringBuilder sb, int dis, boolean last){
        if(vData.getActData() != null && vData.getActData().getValue() != null){
            sb.append(vData.getValName().get() + " = " + vData.getActData().getValue());

            if(!last){
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }
    }

    public static void doOptVal(ValueData vData, StringBuilder sb, int dis, boolean last){
        System.out.println("Opt val");
        if(vData.getActData() != null && vData.getActData().getValue() != null) {
            sb.append(vData.getActData().getValue());

            if(!last){
                sb.append(",");
            } else {
                //sb.append("}");
            }
        }
    }

    public static void doNotOpt(List<ValueData> vList, StringBuilder sb, int dis, boolean last){
        sb.append("{\n");

        for(int i = 0; i < vList.size(); i++){
            if(vList.get(i).isAValue() && vList.get(i).isSelected()){ // Value
                if(i < vList.size()-1){
                    doVal(vList.get(i), sb, dis+1, false);
                } else {
                    doVal(vList.get(i), sb, dis+1, true);
                }
            } else if(vList.get(i).isOption() && vList.get(i).isSelected()){ // Optionale Gruppe
                if(i < vList.size()-1){
                    doOption(vList.get(i).getOptions(), sb, dis+1, false);
                } else {
                    doOption(vList.get(i).getOptions(), sb, dis+1, true);
                }
            } else if(!vList.get(i).isOption() && vList.get(i).isSelected()){ // Nicht-Opt Gruppe
                if(i < vList.size()-1){
                    sb.append(indent(dis+1)+ vList.get(i).getValName().get() + " = ");
                    doNotOpt(vList.get(i).getOptions(), sb, dis+1, false);
                } else {
                    sb.append(indent(dis+1)+ vList.get(i).getValName().get() + " = ");
                    doNotOpt(vList.get(i).getOptions(), sb, dis+1, true);
                }
            } else if(vList.get(i).isOptValue() && vList.get(i).isSelected()){ // Optionaler Value
                if(i < vList.size()-1){
                    doOptVal(vList.get(i), sb, dis+1, false);
                } else {
                    doOptVal(vList.get(i), sb, dis+1, true);
                }
            }

        }
        if(last){
            sb.append("\n"+ indent(dis) + "}\n");
        } else {
            sb.append("\n"+ indent(dis) + "},\n");
        }
    }

    public static void doOption(List<ValueData> vList, StringBuilder sb, int dis, boolean last){
        for(int i = 0; i < vList.size(); i++){
            if(vList.get(i).isAValue() && vList.get(i).isSelected()){ // Value
                if(i < vList.size()-1){
                    doVal(vList.get(i), sb, dis+1, false);
                } else {
                    doVal(vList.get(i), sb, dis+1, true);
                }
            } else if(vList.get(i).isOption() && vList.get(i).isSelected()){ // Optionale Gruppe
                if(i < vList.size()-1){
                    doOption(vList.get(i).getOptions(), sb, dis+1, false);
                } else {
                    doOption(vList.get(i).getOptions(), sb, dis+1, true);
                }
            } else if(!vList.get(i).isOption() && vList.get(i).isSelected()){ // Nicht-Opt Gruppe
                if(i < vList.size()-1){
                    sb.append(indent(dis+1)+ vList.get(i).getValName().get() + " = ");
                    doNotOpt(vList.get(i).getOptions(), sb, dis+1, false);
                } else {
                    sb.append(indent(dis+1)+ vList.get(i).getValName().get() + " = ");
                    doNotOpt(vList.get(i).getOptions(), sb, dis+1, true);
                }
            } else if(vList.get(i).isOptValue() && vList.get(i).isSelected()){ // Optionaler Value
                if(i < vList.size()-1){
                    doOptVal(vList.get(i), sb, dis+1, false);
                } else {
                    doOptVal(vList.get(i), sb, dis+1, true);
                }
            }

        }
    }

    private static String indent(int n) {
        String s = "  ";
        String result = "";

        for (int i = 0; i < n; i++) {
            result += s;
        }

        return result;
    }
}
