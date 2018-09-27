package edu.gcsc.vrl.luaparser;

import java.util.List;

/**
 * This class creates a lua-conform string to write it in a lua-file.
 * This contains the actual values and params, the user selected
 * */
public final class ExportLua {

    public ExportLua() {
        throw new AssertionError();
    }

    /**
     * Builds the export-string
     *
     * @param data actual data set
     * @param filename file name of validation-file
     * */
    public static String doExport(List<ValueData> data, String filename) {
        StringBuilder sb = new StringBuilder();
        sb.append("problem={");

        for (int i = 0; i < data.size(); i++) {
            sb.append(data.get(i).getValName().get() + "=");

            if (data.get(i).isAValue() && data.get(i).isSelected()) {
                if (data.get(i).getActData() != null && data.get(i).getActData().getValue() != null) {
                    doStr(data.get(i), sb);
                }
            } else if ((data.get(i).isOption() || data.get(i).isNotOptGroup()) && GenUtil.haveOptValue(data.get(i)) && data.get(i).isSelected()) {
                if (GenUtil.haveOptValSelected(data.get(i))) {
                    if (i < data.size() - 1) {
                        doSubParams(data.get(i).getOptions(), sb, 1, false);
                    } else {
                        doSubParams(data.get(i).getOptions(), sb, 1, true);
                    }
                } else {
                    if (i < data.size() - 1) {
                        sb.append("{");
                        doSubParams(data.get(i).getOptions(), sb, 1, false);
                        sb.append("}");
                    } else {
                        sb.append("{");
                        doSubParams(data.get(i).getOptions(), sb, 1, true);
                        sb.append("}");
                    }
                }
            } else if (data.get(i).isOption() && !GenUtil.haveOptValue(data.get(i)) && data.get(i).isSelected()) {
                if (i < data.size() - 1) {
                    doOption(data.get(i).getOptions(), sb, 1, false);
                } else {
                    doOption(data.get(i).getOptions(), sb, 1, true);
                }
            } else if (data.get(i).isNotOptGroup() && !GenUtil.haveOptValue(data.get(i)) && data.get(i).isSelected()) {
                if (i < data.size() - 1) {
                    doNotOpt(data.get(i).getOptions(), sb, 1, false);
                } else {
                    doNotOpt(data.get(i).getOptions(), sb, 1, true);
                }
            } else if (data.get(i).isOptValue() && data.get(i).isSelected()) {
                if (i < data.size() - 1) {
                    doOptVal(data.get(i), sb, 1, false);
                } else {
                    doOptVal(data.get(i), sb, 1, true);
                }
            }
        }

        sb.append("}");
        String str = doFormatting(sb, filename);

        return str;
    }

    /**
     * Builds and adds the string for sub-parameters
     *
     * @param dis distance from root
     * @param last is last iteration
     * @param sb <code>StringBuilder</code>
     * @param vList actual data set
     * */
    private static void doSubParams(List<ValueData> vList, StringBuilder sb, int dis, boolean last) {
        for (int i = 0; i < vList.size(); i++) {
            if (vList.get(i).isAValue() && vList.get(i).isSelected()) {
                doVal(vList.get(i), sb, dis + 1, last);
            } else if (vList.get(i).isOptValue() && vList.get(i).isSelected()) {
                doOptVal(vList.get(i), sb, dis + 1, last);
            } else if (((vList.get(i).isOption() || vList.get(i).isNotOptGroup()) && GenUtil.haveOptValue(vList.get(i))) && vList.get(i).isSelected()) {
                if (GenUtil.haveOptValSelected(vList.get(i))) {
                    doSubParams(vList.get(i).getOptions(), sb, dis + 1, last);
                } else {
                    sb.append("{");
                    doSubParams(vList.get(i).getOptions(), sb, dis + 1, last);
                    sb.append("}");
                }
            } else if (vList.get(i).isOption() && !GenUtil.haveOptValue(vList.get(i)) && vList.get(i).isSelected()) {
                if (i < vList.size() - 1) {
                    doOption(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    doOption(vList.get(i).getOptions(), sb, dis + 1, true);
                }
            } else if (vList.get(i).isNotOptGroup() && !GenUtil.haveOptValue(vList.get(i)) && vList.get(i).isSelected()) {
                if (i < vList.size() - 1) {
                    sb.append(vList.get(i).getValName().get() + "=");
                    doNotOpt(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    sb.append(vList.get(i).getValName().get() + "=");
                    doNotOpt(vList.get(i).getOptions(), sb, dis + 1, true);
                }
            }
        }
    }

    /**
     * Builds and adds the string for values
     *
     * @param dis distance from root
     * @param last is last iteration
     * @param sb <code>StringBuilder</code>
     * @param vData actual data set
     * */
    private static void doVal(ValueData vData, StringBuilder sb, int dis, boolean last) {
        if (vData.getActData() != null && vData.getActData().getValue() != null) {
            sb.append(vData.getValName().get() + "=");
            doStr(vData, sb);
        }
    }

    /**
     * Builds and adds the string for optional values
     *
     * @param dis distance from root
     * @param last is last iteration
     * @param sb <code>StringBuilder</code>
     * @param vData actual data set
     * */
    private static void doOptVal(ValueData vData, StringBuilder sb, int dis, boolean last) {
        if (vData.getActData() != null && vData.getActData().getValue() != null) {
            doStr(vData, sb);
        }
    }

    /**
     * Builds and adds the string for not-optional groups
     *
     * @param dis distance from root
     * @param last is last iteration
     * @param sb <code>StringBuilder</code>
     * @param vList actual data set
     * */
    public static void doNotOpt(List<ValueData> vList, StringBuilder sb, int dis, boolean last) {
        sb.append("{");

        for (int i = 0; i < vList.size(); i++) {
            if (vList.get(i).isAValue() && vList.get(i).isSelected()) { // Value
                if (i < vList.size() - 1) {
                    doVal(vList.get(i), sb, dis + 1, false);
                } else {
                    doVal(vList.get(i), sb, dis + 1, true);
                }
            } else if (vList.get(i).isOptValue() && vList.get(i).isSelected()) { // Optionaler Value
                if (i < vList.size() - 1) {
                    doOptVal(vList.get(i), sb, dis + 1, false);
                } else {
                    doOptVal(vList.get(i), sb, dis + 1, true);
                }
            } else if (((vList.get(i).isOption() || vList.get(i).isNotOptGroup()) && GenUtil.haveOptValue(vList.get(i))) && vList.get(i).isSelected()) {
                if (GenUtil.haveOptValSelected(vList.get(i))) {
                    if (i < vList.size() - 1) {
                        doSubParams(vList.get(i).getOptions(), sb, dis + 1, false);
                    } else {
                        doSubParams(vList.get(i).getOptions(), sb, dis + 1, true);
                    }
                } else {
                    if (i < vList.size() - 1) {
                        sb.append("{");
                        doSubParams(vList.get(i).getOptions(), sb, dis + 1, false);
                    } else {
                        sb.append("{");
                        doSubParams(vList.get(i).getOptions(), sb, dis + 1, true);
                    }
                }
            } else if (vList.get(i).isOption() && !GenUtil.haveOptValue(vList.get(i)) && vList.get(i).isSelected()) {
                if (i < vList.size() - 1) {
                    doOption(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    doOption(vList.get(i).getOptions(), sb, dis + 1, true);
                }
            } else if (vList.get(i).isNotOptGroup() && !GenUtil.haveOptValue(vList.get(i)) && vList.get(i).isSelected()) {
                if (i < vList.size() - 1) {
                    sb.append(vList.get(i).getValName().get() + "=");
                    doNotOpt(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    sb.append(vList.get(i).getValName().get() + "=");
                    doNotOpt(vList.get(i).getOptions(), sb, dis + 1, true);
                }
            }
        }
        sb.append("}");
    }

    /**
     * Builds and adds the string for optional groups
     *
     * @param dis distance from root
     * @param last is last iteration
     * @param sb <code>StringBuilder</code>
     * @param vList actual data set
     * */
    public static void doOption(List<ValueData> vList, StringBuilder sb, int dis, boolean last) {
        for (int i = 0; i < vList.size(); i++) {
            if (vList.get(i).isAValue() && vList.get(i).isSelected()) { // Value
                if (i < vList.size() - 1) {
                    doVal(vList.get(i), sb, dis + 1, false);
                } else {
                    doVal(vList.get(i), sb, dis + 1, true);
                }
            } else if (((vList.get(i).isOption() || vList.get(i).isNotOptGroup()) && GenUtil.haveOptValue(vList.get(i))) && vList.get(i).isSelected()) {
                if (i < vList.size() - 1) {
                    doSubParams(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    doSubParams(vList.get(i).getOptions(), sb, dis + 1, true);
                }
            } else if (vList.get(i).isOption() && !GenUtil.haveOptValue(vList.get(i)) && vList.get(i).isSelected()) {
                if (i < vList.size() - 1) {
                    doOption(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    doOption(vList.get(i).getOptions(), sb, dis + 1, true);
                }
            } else if (vList.get(i).isNotOptGroup() && !GenUtil.haveOptValue(vList.get(i)) && vList.get(i).isSelected()) {
                if (i < vList.size() - 1) {
                    sb.append(vList.get(i).getValName().get() + "=");
                    doNotOpt(vList.get(i).getOptions(), sb, dis + 1, false);
                } else {
                    sb.append(vList.get(i).getValName().get() + "=");
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

    /**
     * Helping method
     * */
    private static String indent(int n) {
        String s = "  ";
        String result = "";

        for (int i = 0; i < n; i++) {
            result += s;
        }

        return result;
    }

    /**
     * This method formatting the current values to a string for the export.
     * */
    private static void doStr(ValueData vd, StringBuilder sb) {
        if (vd.getActData().getType().equals("String")) {
            String temp = GenUtil.doQuoteMark(vd.getActData().getValue().toString());
            sb.append(temp);
            sb.append("#");
        } else if (vd.getActData().getType().equals("Double[]")) {
            List<Double> temp = (List<Double>) vd.getActData().getValue();
            if (temp != null) {
                sb.append("{");
                String s = ConversionUtil.fromDoubleListToString(temp);
                sb.append(s);
                sb.append("}");
            }
        } else if (vd.getActData().getType().equals("Integer[]")) {
            List<Integer> temp = (List<Integer>) vd.getActData().getValue();
            if (temp != null) {
                sb.append("{");
                String s = ConversionUtil.fromIntegerListToString(temp);
                sb.append(s);
                sb.append("}");
            }
        } else if (vd.getActData().getType().equals("Boolean[]")) {
            List<Boolean> temp = (List<Boolean>) vd.getActData().getValue();
            if (temp != null) {
                sb.append("{");
                String s = ConversionUtil.fromBooleanListToString(temp);
                sb.append(s);
                sb.append("}");
            }
        } else if (vd.getActData().getType().equals("String[]")||vd.getActData().getType().equals("Function[]")) {
            List<String> temp = (List<String>) vd.getActData().getValue();
            if (temp != null) {
                sb.append("{");
                String s = ConversionUtil.fromStringListToStringQuoteMarks(temp);
                sb.append(s);
                sb.append("}");
            }
        } else {
            sb.append(vd.getActData().getValue() + "#");
        }
    }

    /**
     * This method formats the export-string. It adds comma
     * */
    private static String doFormatting(StringBuilder sb, String filename) {
        StringBuilder sbNew = new StringBuilder();
        sbNew.append("--validation file: " + filename + "\n");
        System.out.println(sb.toString());

        for (int i = 0; i < sb.length() - 1; i++) {
            if (!Character.toString(sb.charAt(i)).equals("#")) {
                if (Character.toString(sb.charAt(i)).equals("}")) {
                    sbNew.append("\n}");
                } else {
                    sbNew.append(sb.charAt(i));
                }
            }
            if (Character.toString(sb.charAt(i)).equals("{")) {
                sbNew.append("\n");
            } else if (Character.toString(sb.charAt(i)).equals("}")) {
                if (Character.toString(sb.charAt(i + 1)).equals("{")) {
                    sbNew.append(",");
                } else if (Character.toString(sb.charAt(i + 1)).equals("}")) {

                } else {
                    sbNew.append(",\n");
                }
            } else if (Character.toString(sb.charAt(i)).equals("#")) {
                if (Character.toString(sb.charAt(i + 1)).equals("{")) {
                    sbNew.append(",\n");
                } else if (Character.toString(sb.charAt(i + 1)).equals("}")) {

                } else {
                    sbNew.append(",\n");
                }

            } else if (Character.toString(sb.charAt(i)).equals("=")) {

            } else {
                if (Character.toString(sb.charAt(i + 1)).equals("{")) {
                    sbNew.append(",\n");
                } else {

                }
            }
        }
        sbNew.append("\n}");

        String str = sbNew.toString();
        System.out.println(str);
        return str;
    }

}
