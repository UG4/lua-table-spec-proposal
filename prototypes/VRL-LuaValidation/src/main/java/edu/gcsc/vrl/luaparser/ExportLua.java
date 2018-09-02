package edu.gcsc.vrl.luaparser;

import java.util.List;

public final class ExportLua {
    public ExportLua() {
        throw new AssertionError();
    }

    public static void doExport(List<ValueData> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("problem = {\n");

        for (int i = 0; i < data.size(); i++) {
            ValueData act = data.get(i);
            if (act.isAValue()) {
                if (act.getActData() != null && act.getActData().getValue() != null) {
                    sb.append(indent(1) + act.getValName().get() + " = " + act.getActData().getValue().toString());
                    if (i < data.size() - 1) {
                        sb.append(",\n");
                    } else if (i == data.size() - 1) {
                        sb.append("\n}\n");
                    }
                }
            } else if (!act.isAValue()) {

                if (act.havingParams()) {
                    if (act.getOptions() != null) {
                        for (ValueData g : act.getOptions()) {
                            if (g.isSelected()) {
                                if (g.isAValue()) {
                                    sb.append(indent(1) + act.getValName().get() + " = ");
                                } else if (g.isNestedGroup()) {
                                    sb.append(indent(1) + act.getValName().get() + " = {\n");
                                } else {
                                    sb.append(indent(1) + act.getValName().get() + " = ");
                                }
                            }
                        }
                    }
                } else {
                    sb.append(indent(1) + act.getValName().get() + " = {\n");
                }

                if (act.getOptions() != null) {
                    List<ValueData> opts = act.getOptions();

                    for (int j = 0; j < opts.size(); j++) {
                        ValueData vd = opts.get(j);
                        if (vd.isAValue() && vd.isSelected()) {
                            if (vd.getActData() != null && vd.getActData().getValue() != null) {
                                if (vd.getParentNode().isNestedGroup()) {
                                    sb.append(indent(2) + vd.getValName().get() + " = " + vd.getActData().getValue().toString());
                                } else {
                                    sb.append(vd.getActData().getValue().toString());
                                }
                                if (j == opts.size() - 1) {
                                    if (i == data.size() - 1) {
                                        sb.append("\n" + indent(1) + "}\n");
                                    } else {
                                        sb.append("\n" + indent(1) + "},\n");
                                    }
                                } else {
                                    sb.append(",\n");
                                }
                            }
                        } else if (!vd.isAValue() && vd.isSelected()) {

                            if (vd.getOptions() != null) {
                                List<ValueData> optsNow = vd.getOptions();
                                if (vd.getParentNode() != null) {
                                    if (vd.getParentNode().isNestedGroup()) {
                                        sb.append(indent(2) + vd.getValName().get() + " = {\n");
                                    }
                                } else {
                                    /*
                                     * Testzwecke
                                     * */
                                }
                                /*if (i == data.size() - 1) {
                                    doExport(optsNow, sb, true,2);
                                } else {
                                    doExport(optsNow, sb, false,2);
                                }*/
                                doExport(optsNow, sb, 2);

                                if (i == data.size() - 1 && j == opts.size() - 1) {
                                    sb.append(indent(1) + "22}\n");
                                } /*else if(i == data.size()-1 && j < opts.size()-1){
                                    sb.append(indent(1)+"},\n");
                                }*/
                            }
                        }
                    }
                }

            }
            if (i == data.size() - 1) {
                sb.append("11}\n");
            }
        }
        String lua = sb.toString();
        sb = new StringBuilder();
        System.out.print(lua);
    }

    public static void doExport(List<ValueData> data, StringBuilder sb, int dis) {

        for (int i = 0; i < data.size(); i++) {
            ValueData act = data.get(i);
            if (act.isAValue() && act.isSelected()) {
                if (act.getActData() != null && act.getActData().getValue() != null) {
                    if (act.getParentNode() != null) {
                        if (act.getParentNode().isNestedGroup()) {
                            sb.append(indent(dis + 1) + act.getValName().get() + " = " + act.getActData().getValue());
                        } else {
                            sb.append(act.getActData().getValue());
                        }
                    }

                    if((isLast(act.getParentNode().getParentNode().getOptions(),act.getParentNode())) && act.getParentNode().getParentNode() != null) {
                        if (i == data.size() - 1) {
                            sb.append("\n" + data.get(i).getValName().get() + "\n");
                            sb.append("\n" + indent(dis) + "}66\n");
                        } else {
                            sb.append("6,\n");
                        }
                    } else {
                        if (i == data.size() - 1) {
                            sb.append("\n" + data.get(i).getValName().get() + "\n");
                            sb.append("\n" + indent(dis) + "}66,\n");
                        } else {
                            sb.append("6,\n");
                        }
                    }

                    /*if (!lastIteration) {
                        if (i == data.size() - 1) {
                            sb.append("\n"+indent(dis)+"},\n");
                        } else {
                            sb.append(",\n");
                        }
                    } else if (lastIteration) {
                        if (i == data.size() - 1) {
                            sb.append("\n"+indent(dis)+"00}\n");
                        } else {
                            sb.append(",\n");
                        }
                    }*/
                }
            } else if (!act.isAValue() && act.isSelected()) {
                if (act.getParentNode() != null) {
                    if (act.getParentNode().isNestedGroup()) {
                        sb.append(indent(dis + 1) + act.getValName().get() + " = ");
                    }
                }

                if (act.getOptions() != null) {
                    List<ValueData> opts = act.getOptions();

                    for (int j = 0; j < opts.size(); j++) {
                        ValueData vd = opts.get(j);
                        if (vd.isAValue() && vd.isSelected()) {
                            if (vd.getActData() != null && vd.getActData().getValue() != null) {
                                if (vd.getParentNode().isNestedGroup()) {
                                    sb.append(indent(dis + 2) + vd.getValName().get() + " = " + vd.getActData().getValue().toString());
                                } else {
                                    sb.append(indent(dis + 2) + vd.getActData().getValue().toString());
                                }

                                if (j == opts.size() - 1) {
                                    sb.append("\n}\n");
                                } else {
                                    sb.append("\n" + indent(dis) + "}99,\n");
                                }
                                if (i == data.size() - 1) {
                                    sb.append("\n}\n");
                                }
                            }

                        } else if (!vd.isAValue() && vd.isSelected()) {
                            if (vd.getParentNode().isNestedGroup()) {
                                sb.append(indent(dis + 1) + " { \n");
                            }
                            if (vd.getOptions() != null) {
                                List<ValueData> optsNow = vd.getOptions();
                                if (i == opts.size() - 1) {
                                    doExport(optsNow, sb, dis + 1);
                                } else {
                                    doExport(optsNow, sb, dis + 1);
                                }

                                if (j == opts.size() - 1) {
                                    if (vd.getParentNode().isNestedGroup()) {
                                        sb.append("\n}\n");
                                    }
                                } else {
                                    sb.append("\n" + indent(dis) + "55},\n");
                                }
                                if (i == data.size() - 1) {
                                    sb.append("\n}\n");
                                }
                            }
                        }
                    }
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

    private static boolean isLast(List<ValueData> lv, ValueData v) {
        System.out.println("ff : " + lv.get(lv.size() - 1).getValName().get());
        for(int j = 0; j < lv.size();j++){
            System.out.println("OBJ: "+ lv.get(j).getValName().get());
        }

        if (lv.get(lv.size() - 1).getValName().get().equals(v.getValName().get())) {
            System.out.println("true : " + lv.get(lv.size() - 1).getValName().get());
            return true;
        }
        return false;
    }
}
