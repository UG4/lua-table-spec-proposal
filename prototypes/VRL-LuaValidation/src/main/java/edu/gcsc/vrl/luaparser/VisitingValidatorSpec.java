package edu.gcsc.vrl.luaparser;

import org.apache.commons.lang.math.NumberUtils;

import java.util.List;

public final class VisitingValidatorSpec {

    /*
     * Hier wird sicher gestellt, dass die Utility-Klasse nicht
     * instanziiert werden kann.
     * */
    public VisitingValidatorSpec() {
        throw new AssertionError();
    }

    public static void visitOne(Entry e, List<ValueData> dataList) {
        if (e instanceof Group) {
            if (!"problem".equals(e.getName()) && isVal(e) && !"root".equals(e.getName())) {
                //VALUE
                System.out.println(e.getName() + " is a Val!");
                ValueData xd = new ValueData(e.getName());
                xd.setSelection(true);
                xd.isValue(true);
                setInfos(xd, (Group) e);
                ActualDataValue adv = new ActualDataValue();
                adv.setType(xd.getType().get());
                if (xd.getDefaultVal() != null) {
                    adv.setValue(xd.getDefaultVal());
                }
                xd.setActData(adv);
                dataList.add(xd);

            } else if (!"problem".equals(e.getName()) && !NumberUtils.isNumber(e.getName()) && !isOptVal(e) && !isVal(e) && !"root".equals(e.getName())) {
                // NICHT-OPTIONALE GRUPPE
                System.out.println(e.getName() + " is NOT-OPTIONAL!");
                ValueData xd = new ValueData(e.getName());
                xd.setNotOptGroup(true);
                xd.setSelection(true);
                dataList.add(xd);
                for (Entry p : ((Group) e).getEntries()) {
                    visitTwo(p, dataList, xd);
                }

            } else if (!"problem".equals(e.getName()) && NumberUtils.isNumber(e.getName()) && !"root".equals(e.getName()) && !isOptVal(e)) {
                //OPTIONALE GRUPPE
                System.out.println(e.getName() + " is OPTIONAL!");
                ValueData xd = new ValueData(e.getName());
                xd.setOptional(true);
                dataList.add(xd);
                for (Entry p : ((Group) e).getEntries()) {
                    visitTwo(p, dataList, xd);
                }

            } else if (!"problem".equals(e.getName()) && !"root".equals(e.getName()) && isOptVal(e)) {
                //OPTIONALER VALUE
                System.out.println(e.getName() + " is a optional Value!");

                ValueData xd = new ValueData(e.getName().toString());
                xd.setOptVal(true);
                setInfos(xd, (Group) e);
                ActualDataValue adv = new ActualDataValue();
                adv.setType(xd.getType().get());
                if (xd.getDefaultVal() != null) {
                    adv.setValue(xd.getDefaultVal());
                }
                xd.setActData(adv);
                dataList.add(xd);

            } else {
                for (Entry l : ((Group) e).getEntries()) {
                    visitOne(l, dataList);
                }
            }

        } else if (e instanceof Value) {
            Value v = (Value) e;

            if (v.isFunction() && "eval".equals(v.getName())) {
                try {
                    // invoke methods without arguments
                    System.out.println("eval: "
                            + v.asFunction().eval().getValueAsString());
                } catch (Exception ex) {
                    //System.out.print("-> ERROR: cannot call f!");
                }
            }
        }
    }

    private static void visitTwo(Entry e, List<ValueData> dataList, ValueData v) {
        if (e instanceof Group) {
            if (!"problem".equals(e.getName()) && isVal(e)) {
                //VALUE
                System.out.println(e.getName() + " is a Val!");
                ValueData xd = new ValueData(e.getName().toString());
                xd.isValue(true);
                xd.setSelection(true);
                setInfos(xd, (Group) e);
                ActualDataValue adv = new ActualDataValue();
                adv.setType(xd.getType().get());
                if (xd.getDefaultVal() != null) {
                    adv.setValue(xd.getDefaultVal());
                }
                xd.setActData(adv);
                xd.setParentNode(v);
                v.addSubParam(xd);
            } else if (!"problem".equals(e.getName()) && !NumberUtils.isNumber(e.getName()) && !isVal(e) && !isOptVal(e)) {
                //NICHT-OPTIONALE GRUPPE
                System.out.println(e.getName() + " is NOT-OPTIONAL!");
                ValueData xd = new ValueData(e.getName());
                xd.setSelection(true);
                xd.setNotOptGroup(true);
                xd.setParentNode(v);
                v.addSubParam(xd);

                for (Entry p : ((Group) e).getEntries()) {
                    visitTwo(p, dataList, xd);
                }
            } else if (!"problem".equals(e.getName()) && NumberUtils.isNumber(e.getName()) && !isOptVal(e)) {
                //OPTIONALE GRUPPE
                System.out.println(e.getName() + " is OPTIONAL!");
                ValueData xd = new ValueData(e.getName());
                xd.setOptional(true);
                xd.setParentNode(v);
                v.addSubParam(xd);

                for (Entry p : ((Group) e).getEntries()) {
                    visitTwo(p, dataList, xd);
                }

            } else if (!"problem".equals(e.getName()) && !"root".equals(e.getName()) && isOptVal(e)) {
                //OPTIONALER VALUE
                System.out.println(e.getName() + " is a optional Value!");

                ValueData xd = new ValueData(e.getName().toString());
                xd.setOptVal(true);
                setInfos(xd, (Group) e);
                ActualDataValue adv = new ActualDataValue();
                adv.setType(xd.getType().get());
                if (xd.getDefaultVal() != null) {
                    adv.setValue(xd.getDefaultVal());
                }
                xd.setActData(adv);
                xd.setParentNode(v);
                v.addSubParam(xd);

            }
        } else if (e instanceof Value) {
            Value va = (Value) e;

            if (va.isFunction() && "eval".equals(va.getName())) {
                try {
                    // invoke methods without arguments
                    System.out.println("eval: "
                            + va.asFunction().eval().getValueAsString());
                } catch (Exception ex) {
                    //System.out.print("-> ERROR: cannot call f!");
                }
            }
        }
    }

    private static boolean isVal(Entry e) {
        if (e instanceof Group) {
            if (!NumberUtils.isNumber(e.getName())) {
                for (Entry p : ((Group) e).getEntries()) {
                    if (p instanceof Value) {
                        if ("type".equals(p.getName())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean isOptVal(Entry e) {
        if (e instanceof Group) {
            if (NumberUtils.isNumber(e.getName())) {
                for (Entry p : ((Group) e).getEntries()) {
                    if (p instanceof Value) {
                        if ("type".equals(p.getName())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static void setInfos(ValueData vd, Group e) {
        for (Entry l : ((Group) e).getEntries()) {
            if (l instanceof Value) {
                switch (l.getName().toString()) {
                    case "type":
                        vd.setType(((Value) l).getValueAsString());
                        break;
                    case "default":
                        vd.setDefaultVal(((Value) l).getValueAsString());
                        break;
                    case "style":
                        vd.setStyle(((Value) l).getValueAsString());
                        break;
                    case "tooltip":
                        vd.setTooltip(((Value) l).getValueAsString());
                        break;
                }
            } else if (l instanceof Group) {
                System.out.println(l.getName().toString() + "    NAME");
                if (!isVal(l)) {
                    switch (l.getName().toString()) {
                        case "range":
                            double[] myValues = getRange((Group) l);
                            if (myValues.length == 2) {
                                vd.setRange_min(myValues[0]);
                                vd.setRange_max(myValues[1]);
                            } else if (myValues.length > 2) {
                                vd.setValues(myValues);
                            }
                            break;
                        case "visibility":
                            setVisibilityInfos((Group) l, vd);
                            break;
                        case "validation":
                            setValidationInfos((Group) l, vd);
                            break;
                    }
                }
            }
        }

    }

    private static double[] getRange(Group range) {
        double min = 0;
        double max = 0;
        for (Entry e : range.getEntries()) {
            switch (e.getName().toString()) {
                case "min":
                    min = Double.parseDouble(((Value) e).getValueAsString());
                    break;
                case "max":
                    max = Double.parseDouble(((Value) e).getValueAsString());
                    double[] vals = {min, max};
                    return vals;
                case "values":
                    int counter = 0;
                    double[] myVals = new double[((Group) e).getEntries().size()];
                    for (Entry h : ((Group) e).getEntries()) {
                        myVals[counter] = Double.parseDouble(((Value) h).getValueAsString());
                        counter++;
                    }
                    return myVals;
            }
        }
        return null;
    }

    private static void setVisibilityInfos(Group visib, ValueData vd) {
        for (Entry e : visib.getEntries()) {
            switch (e.getName()) {
                case "dependsOn":
                    vd.setDependsOnVisible(true);
                    String[] dO = new String[((Group) e).getEntries().size()];
                    for (int i = 0; i < ((Group) e).getEntries().size(); i++) {
                        Value h = (Value) ((Group) e).getEntries().get(i);
                        dO[i] = h.getValueAsString();
                    }
                    vd.setVis_dependsOn(dO);
                    break;
                case "eval":
                    Value eval = ((Value) e);
                    if (eval.isFunction()) {
                        vd.setVis_eval(eval);
                    }
                    break;
            }
        }
    }

    private static void setValidationInfos(Group valid, ValueData vd) {
        for (Entry e : valid.getEntries()) {
            switch (e.getName()) {
                case "dependsOn":
                    vd.setDependsOnValidate(true);
                    String[] dO = new String[((Group) e).getEntries().size()];
                    for (int i = 0; i < ((Group) e).getEntries().size(); i++) {
                        Value h = (Value) ((Group) e).getEntries().get(i);
                        dO[i] = h.getValueAsString();
                    }
                    vd.setValid_dependsOn(dO);
                    break;
                case "eval":
                    Value eval = ((Value) e);
                    if (eval.isFunction()) {
                        vd.setValid_eval(eval);
                    }
                    break;
            }
        }
    }
}
