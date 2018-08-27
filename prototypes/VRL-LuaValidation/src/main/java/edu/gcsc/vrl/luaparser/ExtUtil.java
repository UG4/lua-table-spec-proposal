package edu.gcsc.vrl.luaparser;

import java.util.List;

public final class ExtUtil {

    private ExtUtil() {
        throw new AssertionError();
    }

    public static void visit(Group g, List<ValueData> dataList) {
        ValueData actData = new ValueData(g.getName().toString());

        for (Entry e : g.getEntries()) {
            if (e instanceof Group) {
                if (!ExtractionHelper.checkForValues((Group) e)) {
                    for (Entry r : ((Group) e).getEntries()) {
                        System.out.println(r.getName().toString() + " is a Subparam from " + r.getParent().getParent().getName().toString());
                        ValueData subParamVD = new ValueData(r.getName().toString());
                        checkParameters(r, subParamVD);
                        actData.addSubParam(subParamVD);
                        /*
                         * Hier müssen noch types usw. erkannt werden
                         * */
                    }
                } else if (ExtractionHelper.checkVal(e)) {
                    System.out.println(e.getName().toString() + " is a Subparam from " + e.getParent().getName().toString());
                    ValueData subParamVD = new ValueData(e.getName().toString());
                    checkParameters(e, subParamVD);
                    actData.addSubParam(subParamVD);
                }
            }
        }
        if (actData != null) {
            dataList.add(actData);
        }
    }

    public static void checkParameters(Entry v, ValueData actData) {
        for (Entry l : ((Group) v).getEntries()) {
            if (l instanceof Value) {
                switch (l.getName().toString()) {
                    case "type":
                        actData.setType(((Value) l).getValueAsString());
                        break;
                    case "default":
                        actData.setDefaultVal(((Value) l).getValueAsString());
                        break;
                    case "style":
                        actData.setStyle(((Value) l).getValueAsString());
                        break;
                    case "tooltip":
                        actData.setTooltip(((Value) l).getValueAsString());
                        break;
                }
            } else if (l instanceof Group) {
                System.out.println(l.getName().toString() + "    NAME");
                if (!ExtractionHelper.checkVal(l)) {
                    switch (l.getName().toString()) {
                        case "range":
                            double[] myValues = ExtractionHelper.getRange((Group) l);
                            if (myValues.length == 2) {
                                actData.setRange_min(myValues[0]);
                                actData.setRange_max(myValues[1]);
                            } else if (myValues.length > 2) {
                                actData.setValues(myValues);
                            }
                            break;
                        case "visibility":
                            break;
                    }
                }
            }
        }
    }
}
