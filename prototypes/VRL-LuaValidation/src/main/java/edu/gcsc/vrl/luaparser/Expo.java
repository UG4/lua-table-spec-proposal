package edu.gcsc.vrl.luaparser;

import java.util.List;

public final class Expo {
    public Expo() {
        throw new AssertionError();
    }

    public static void doExport(List<ValueData> data) {
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
                    doVal(data.get(i), sb, false);
                } else {
                    doVal(data.get(i), sb, true);
                }
            } else if (data.get(i).havingParams()) {
                if (i < data.size() - 1) {
                    doSub(data.get(i).getOptions(), sb, false, 1);
                } else {
                    doSub(data.get(i).getOptions(), sb, true, 1);
                }
            }
        }
        sb.append("}");
        String str = sb.toString();
        System.out.println(str);
    }


    private static void doLastNested(List<ValueData> vl, StringBuilder sb, int dis) {
        sb.append("{\n");

        for (int i = 0; i < vl.size(); i++) {
            if (vl.get(i).isNestedGroup() && vl.get(i).isSelected()) {
                if (i < vl.size() - 1) {
                    sb.append(indent(dis + 1) + vl.get(i).getValName().get() + " = ");
                    doNested2(vl.get(i).getOptions(), sb, dis+1);
                } else {
                    sb.append(indent(dis + 1) + vl.get(i).getValName().get() + " = ");
                    doLastNested(vl.get(i).getOptions(), sb, dis+1);
                }
            } else if (vl.get(i).isAValue() && vl.get(i).isSelected()) {
                sb.append(indent(dis + 1) + vl.get(i).getValName().get() + " = " + vl.get(i).getActData().getValue());
                if (i < vl.size() - 1) {
                    sb.append(",\n");
                } else {

                }
            }
        }
        sb.append("\n"+indent(dis) + "}\n");

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
            }
        }
        sb.append("\n" + indent(dis) + "},\n");
    }

    private static void doVal(ValueData v, StringBuilder sb, boolean last) {
        sb.append(v.getActData().getValue());
        if (last) {
            sb.append("\n");
        } else {
            sb.append(",\n");
        }
    }

    private static void doSub(List<ValueData> vl, StringBuilder sb, boolean last, int dis) {
        for (int i = 0; i < vl.size(); i++) {
            if (vl.get(i).isNestedGroup() && vl.get(i).isSelected()) {
                if (last) {
                    doLastNested(vl.get(i).getOptions(), sb, dis);
                } else {
                    doNested2(vl.get(i).getOptions(), sb, dis);
                }
            } else if (vl.get(i).isAValue() && vl.get(i).isSelected()) {
                doVal(vl.get(i), sb, last);
            } else if (vl.get(i).havingParamsExtended() && vl.get(i).isSelected()) {
                doSub(vl.get(i).getOptions(), sb, last, dis);
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
