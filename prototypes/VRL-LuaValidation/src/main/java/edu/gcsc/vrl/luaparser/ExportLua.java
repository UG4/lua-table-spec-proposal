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
                    sb.append(act.getValName().get() + " = " + act.getActData().getValue().toString());
                    if (i < data.size() - 1) {
                        sb.append(",\n");
                    }
                }
            } else if (!act.isAValue()) {

                if (act.havingParams()) {
                    if (act.getOptions() != null) {
                        for (ValueData g : act.getOptions()) {
                            if (g.isSelected()) {
                                if (g.isAValue()) {
                                    sb.append(act.getValName().get() + " = ");
                                } else if (g.isNestedGroup()) {
                                    sb.append(act.getValName().get() + " = {\n");
                                } else {
                                    sb.append(act.getValName().get() + " = ");
                                }
                            }
                        }
                    }
                } else {
                    sb.append(act.getValName().get() + " = {\n");
                }

                if (act.getOptions() != null) {
                    List<ValueData> opts = act.getOptions();

                    for (int j = 0; j < opts.size(); j++) {
                        ValueData vd = opts.get(j);
                        if (vd.isAValue() && vd.isSelected()) {
                            if (vd.getActData() != null && vd.getActData().getValue() != null) {
                                if (vd.getParentNode().isNestedGroup()) {
                                    sb.append(vd.getValName().get() + " = " + vd.getActData().getValue().toString());
                                } else {
                                    sb.append(vd.getActData().getValue().toString());
                                }
                                if (j == opts.size() - 1) {
                                    if (i == data.size() - 1) {
                                        sb.append("\n } \n");
                                    } else {
                                        sb.append("\n },\n");
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
                                        sb.append(vd.getValName().get() + " = {\n");
                                    }
                                } else {
                                    /*
                                     * Testzwecke
                                     * */
                                }
                                if (i == opts.size() - 1) {
                                    doExport(optsNow, sb, true);
                                } else {
                                    doExport(optsNow, sb, false);
                                }
                            }
                        }
                    }
                }

            }
            if (i == data.size() - 1) {
                sb.append("\n}\n");
            }
        }
        String lua = sb.toString();
        sb = new StringBuilder();
        System.out.print(lua);
    }

    public static void doExport(List<ValueData> data, StringBuilder sb, boolean lastIteration) {

        for (int i = 0; i < data.size(); i++) {
            ValueData act = data.get(i);
            if (act.isAValue() && act.isSelected()) {
                if (act.getActData() != null && act.getActData().getValue() != null) {
                    if (act.getParentNode() != null) {
                        if (act.getParentNode().isNestedGroup()) {
                            sb.append(act.getValName().get() + " = " + act.getActData().getValue());
                        } else {
                            sb.append(act.getActData().getValue());
                        }
                    }

                    if (!lastIteration) {
                        if (i == data.size() - 1) {
                            sb.append("\n},\n");
                        } else {
                            sb.append(",\n");
                        }
                    } else if (lastIteration) {
                        if (i == data.size() - 1) {
                            sb.append("\n}\n");
                        } else {
                            sb.append(",\n");
                        }
                    }
                }
            } else if (!act.isAValue() && act.isSelected()) {
                if (act.getParentNode() != null) {
                    if (act.getParentNode().isNestedGroup()) {
                        sb.append(act.getValName().get() + " = ");
                    }
                }

                if (act.getOptions() != null) {
                    List<ValueData> opts = act.getOptions();

                    for (int j = 0; j < opts.size(); j++) {
                        ValueData vd = opts.get(j);
                        if (vd.isAValue() && vd.isSelected()) {
                            if (vd.getActData() != null && vd.getActData().getValue() != null) {
                                if (vd.getParentNode().isNestedGroup()) {
                                    sb.append(vd.getValName().get() + " = " + vd.getActData().getValue().toString());
                                } else {
                                    sb.append(vd.getActData().getValue().toString());
                                }

                                if (j == opts.size() - 1) {
                                    if (i == data.size() - 1) {
                                        sb.append("\n }\n ");
                                    } else {
                                        sb.append("\n },\n");
                                    }
                                } else {
                                    sb.append(",\n");
                                }
                            }

                        } else if (!vd.isAValue() && vd.isSelected()) {
                            if (vd.getParentNode().isNestedGroup()) {
                                sb.append(" { \n");
                            }
                            if (vd.getOptions() != null) {
                                List<ValueData> optsNow = vd.getOptions();
                                doExport(optsNow, sb, lastIteration);
                            }
                        }
                    }
                }
            }

        }
    }
}
