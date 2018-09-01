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
                    if (i == data.size() - 1) {
                        sb.append("\n}\n");
                    } else {
                        sb.append(",\n");
                    }
                }
            } else if (!act.isAValue()) {
                sb.append(act.getValName().get() + " = ");
                if (act.getOptions() != null) {
                    List<ValueData> opts = act.getOptions();

                    for (int j = 0; j < opts.size(); j++) {
                        ValueData vd = opts.get(j);
                        if (vd.isAValue() && vd.isSelected()) {
                            if (vd.getActData() != null && vd.getActData().getValue() != null) {
                                if(vd.getParentNode().isNestedGroup()) {
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
                            sb.append(" { \n");
                            if (vd.getOptions() != null) {
                                List<ValueData> optsNow = vd.getOptions();
                                sb.append(vd.getValName().get() + " = {\n");
                                if(j == opts.size()-1){
                                    doExport(optsNow,sb,true);
                                } else {
                                    doExport(optsNow, sb, false);
                                }
                            }
                        }
                    }
                }

            } if(i == data.size()-1) {
                sb.append("\n}\n");
            }
        }
        //sb.append("\n}");
        String lua = sb.toString();
        sb = new StringBuilder();
        System.out.print(lua);
    }

    public static void doExport(List<ValueData> data, StringBuilder sb, boolean lastIteration) {
        //StringBuilder sb = new StringBuilder();
        //sb.append("problem = {\n");

        for (int i = 0; i < data.size(); i++) {
            ValueData act = data.get(i);
            if (act.isAValue() && act.isSelected()) {
                if (act.getActData() != null && act.getActData().getValue() != null) {
                    sb.append(act.getValName().get() + " = " + act.getActData().getValue());
                    if(!lastIteration) {
                        if (i == data.size() - 1) {
                            sb.append("\n},\n");
                        } else {
                            sb.append(",\n");
                        }
                    } else if(lastIteration){
                        if (i == data.size() - 1) {
                            sb.append("\n}\n");
                        } else {
                            sb.append(",\n");
                        }
                    }
                }
            } else if (!act.isAValue() && act.isSelected()) {
                if (act.getActData() != null && act.getActData().getValue() != null) {
                    sb.append(act.getValName().get() + " = ");
                    if (act.getOptions() != null) {
                        List<ValueData> opts = act.getOptions();

                        for (int j = 0; j < opts.size(); j++) {
                            ValueData vd = opts.get(j);
                            if (vd.isAValue() && vd.isSelected()) {
                                if (vd.getActData() != null && vd.getActData().getValue() != null) {
                                    if(vd.getParentNode().isNestedGroup()) {
                                        sb.append(vd.getValName().get() + " = " + vd.getActData().getValue().toString());
                                    } else {
                                        sb.append(vd.getActData().getValue().toString());
                                    }

                                    //sb.append(vd.getActData().getValue().toString());
                                    if (j == opts.size() - 1) {
                                        if (i == data.size() - 1) {
                                            sb.append("\n }\n ");
                                        } else {
                                            sb.append("\n },\n");
                                        }
                                    } else {
                                        sb.append(",\n");
                                    }
                                } else if (!vd.isAValue() && vd.isSelected()) {
                                    sb.append(" { \n");
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
    }
}
