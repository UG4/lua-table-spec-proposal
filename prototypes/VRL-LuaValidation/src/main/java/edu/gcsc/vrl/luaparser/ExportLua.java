package edu.gcsc.vrl.luaparser;

import java.util.List;

public final class ExportLua {
    public ExportLua() {
        throw new AssertionError();
    }

    public static String doExport(List<ValueData> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("problem = {\n");

        for (int i = 0; i < data.size(); i++) {
            sb.append(indent(1) + data.get(i).getValName().get() + " = ");

            if (data.get(i).isAValue()) {
                System.out.println("HALLO1");
                if (i < data.size() - 1) {
                    //doVal(data.get(i), sb, 1, false);
                    if (data.get(i).getActData() != null && data.get(i).getActData().getValue() != null) {
                        sb.append(data.get(i).getActData().getValue() + ",\n");
                    }
                } else {
                    //doVal(data.get(i), sb, 1, true);
                    if (data.get(i).getActData() != null && data.get(i).getActData().getValue() != null) {
                        sb.append(data.get(i).getActData().getValue() + "\n");
                    }
                }
            } else if ((data.get(i).isOption() || data.get(i).isNotOptGroup()) && GeneralUtil.haveOptValue(data.get(i))) {
                if (i < data.size() - 1) {
                    doSubParams(data.get(i).getOptions(), sb, 1, false);
                } else {
                    doSubParams(data.get(i).getOptions(), sb, 1, true);
                }
            } else if (data.get(i).isOption() && !GeneralUtil.haveOptValue(data.get(i))) {
                if (i < data.size() - 1) {
                    doOption(data.get(i).getOptions(), sb, 1, false);
                } else {
                    doOption(data.get(i).getOptions(), sb, 1, true);
                }
            } else if (data.get(i).isNotOptGroup() && !GeneralUtil.haveOptValue(data.get(i))) {
                if (i < data.size() - 1) {
                    doNotOpt(data.get(i).getOptions(), sb, 1, false);
                } else {
                    doNotOpt(data.get(i).getOptions(), sb, 1, true);
                }
            } else if (data.get(i).isOptValue()) {
                System.out.println("HALLO4");
                if (i < data.size() - 1) {
                    doOptVal(data.get(i), sb, 1, false);
                } else {
                    doOptVal(data.get(i), sb, 1, true);
                }
            }
        }

        sb.append("}");
        String str = sb.toString();

        return str;
    }

    private static void doSubParams(List<ValueData> vList, StringBuilder sb, int dis, boolean last) {
        for (int i = 0; i < vList.size(); i++) {
            if (vList.get(i).isAValue() && vList.get(i).isSelected()) {
                doVal(vList.get(i), sb, dis + 1, last);
            } else if (vList.get(i).isOptValue() && vList.get(i).isSelected()) {
                doOptVal(vList.get(i), sb, dis + 1, last);
            } else if (((vList.get(i).isOption() || vList.get(i).isNotOptGroup()) && GeneralUtil.haveOptValue(vList.get(i))) && vList.get(i).isSelected()) {
                doSubParams(vList.get(i).getOptions(), sb, dis + 1, last);
            } else if(vList.get(i).isOption() && !GeneralUtil.haveOptValue(vList.get(i)) && vList.get(i).isSelected()){
                if (i < vList.size() - 1) {
                    doOption(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    doOption(vList.get(i).getOptions(), sb, dis + 1, true);
                }
            } else if (vList.get(i).isNotOptGroup() && !GeneralUtil.haveOptValue(vList.get(i)) && vList.get(i).isSelected()) {
                if (i < vList.size() - 1) {
                    sb.append(vList.get(i).getValName().get() + " = ");
                    doNotOpt(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    sb.append(vList.get(i).getValName().get() + " = ");
                    doNotOpt(vList.get(i).getOptions(), sb, dis + 1, true);
                }
            }
        }
    }

    private static void doVal(ValueData vData, StringBuilder sb, int dis, boolean last) {
        if (vData.getActData() != null && vData.getActData().getValue() != null) {
            sb.append(vData.getValName().get() + " = " + vData.getActData().getValue());

            if (!last) {
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }
    }

    private static void doOptVal(ValueData vData, StringBuilder sb, int dis, boolean last) {
        System.out.println("Opt val");
        if (vData.getActData() != null && vData.getActData().getValue() != null) {
            sb.append(vData.getActData().getValue());

            if (!last) {
                sb.append(",");
            } else {
                //sb.append("}");
            }
        }
    }

    public static void doNotOpt(List<ValueData> vList, StringBuilder sb, int dis, boolean last) {
        sb.append("{\n");

        for (int i = 0; i < vList.size(); i++) {
            if (vList.get(i).isAValue() && vList.get(i).isSelected()) { // Value
                if (i < vList.size() - 1) {
                    doVal(vList.get(i), sb, dis + 1, false);
                } else {
                    doVal(vList.get(i), sb, dis + 1, true);
                }
            } else if (vList.get(i).isOptValue() && vList.get(i).isSelected()) { // Optionaler Value
                System.out.println("TEST OPT");
                System.out.println(vList.get(i).getValName().get());
                if (i < vList.size() - 1) {
                    doOptVal(vList.get(i), sb, dis + 1, false);
                } else {
                    doOptVal(vList.get(i), sb, dis + 1, true);
                }
            } else if (((vList.get(i).isOption() || vList.get(i).isNotOptGroup()) && GeneralUtil.haveOptValue(vList.get(i))) && vList.get(i).isSelected()) {
                if (i < vList.size() - 1) {
                    doSubParams(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    doSubParams(vList.get(i).getOptions(), sb, dis + 1, true);
                }
            } else if (vList.get(i).isOption() && !GeneralUtil.haveOptValue(vList.get(i)) && vList.get(i).isSelected()) {
                if (i < vList.size() - 1) {
                    doOption(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    doOption(vList.get(i).getOptions(), sb, dis + 1, true);
                }
            } else if (vList.get(i).isNotOptGroup() && !GeneralUtil.haveOptValue(vList.get(i)) && vList.get(i).isSelected()) {
                if (i < vList.size() - 1) {
                    sb.append(vList.get(i).getValName().get() + " = ");
                    doNotOpt(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    sb.append(vList.get(i).getValName().get() + " = ");
                    doNotOpt(vList.get(i).getOptions(), sb, dis + 1, true);
                }
            }
        }
        if (last) {
            sb.append("\n" + indent(dis) + "}\n");
        } else {
            sb.append("\n" + indent(dis) + "},\n");
        }
    }

    public static void doOption(List<ValueData> vList, StringBuilder sb, int dis, boolean last) {
        //sb.append("{\n");
        for (int i = 0; i < vList.size(); i++) {
            if (vList.get(i).isAValue() && vList.get(i).isSelected()) { // Value
                if (i < vList.size() - 1) {
                    doVal(vList.get(i), sb, dis + 1, false);
                } else {
                    doVal(vList.get(i), sb, dis + 1, true);
                }
            } else if (((vList.get(i).isOption() || vList.get(i).isNotOptGroup()) && GeneralUtil.haveOptValue(vList.get(i))) && vList.get(i).isSelected()) {
                if (i < vList.size() - 1) {
                    doSubParams(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    doSubParams(vList.get(i).getOptions(), sb, dis + 1, true);
                }
            } else if (vList.get(i).isOption() && !GeneralUtil.haveOptValue(vList.get(i)) && vList.get(i).isSelected()) {
                if (i < vList.size() - 1) {
                    doOption(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    doOption(vList.get(i).getOptions(), sb, dis + 1, true);
                }
            } else if (vList.get(i).isNotOptGroup() && !GeneralUtil.haveOptValue(vList.get(i)) && vList.get(i).isSelected()) {
                if (i < vList.size() - 1) {
                    sb.append(vList.get(i).getValName().get() + " = ");
                    doNotOpt(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    sb.append(vList.get(i).getValName().get() + " = ");
                    doNotOpt(vList.get(i).getOptions(), sb, dis + 1, true);
                }
            } else if (vList.get(i).isOptValue() && vList.get(i).isSelected()) { // Optionaler Value
                if (i < vList.size() - 1) {
                    doOptVal(vList.get(i), sb, dis + 1, false);
                } else {
                    doOptVal(vList.get(i), sb, dis + 1, true);
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
