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

    /*public static void visitOne(Entry e, List<ValueData> dataList) {
        if (e instanceof Group) {
            if (!"problem".equals(e.getName()) && hasOnlyGroups(e) && !isVal(e) && !"root".equals(e.getName()) && isNestedGroup(e)) {
                System.out.println("Nested: " + e.getName().toString() + " & SubParams");
                ValueData vd = new ValueData(e.getName().toString());
                vd.setNestedGroup(true);
                dataList.add(vd);
                for (Entry p : ((Group) e).getEntries()) {
                    visitTwo(p, dataList, vd);
                }
            } else if (!"problem".equals(e.getName()) && hasOnlyGroups(e) && !isVal(e) && !"root".equals(e.getName()) && isANum(e.getName())) {
                System.out.println("Option: " + e.getName());
                ValueData vd = new ValueData(e.getName().toString());
                vd.setOption(true);
                dataList.add(vd);
                for (Entry p : ((Group) e).getEntries()) {
                    visitTwo(p, dataList, vd);
                }
            } else if (isVal(e)) {
                if (!hasSubParams((Group) e)) {
                    System.out.println("Value: " + e.getName().toString() + " & no SubParams");
                    ValueData xd = new ValueData(e.getName().toString());
                    xd.isValue(true);
                    setInfos(xd, (Group) e);
                    ActualDataValue adv = new ActualDataValue();
                    adv.setType(xd.getType().get());
                    if (xd.getDefaultVal() != null) {
                        adv.setValue(xd.getDefaultVal());
                    }
                    xd.setActData(adv);
                    dataList.add(xd);
                } else if (hasSubParams((Group) e)) {
                    System.out.println("Value: " + e.getName().toString() + " & SubParams");
                    ValueData cd = new ValueData(e.getName().toString());
                    cd.setNestedGroup(false);
                    dataList.add(cd);
                    for (Entry p : ((Group) e).getEntries()) {
                        visitTwo(p, dataList, cd);
                    }
                }
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
            if (!"problem".equals(e.getName().toString()) && hasOnlyGroups(e) && !isVal(e) && isNestedGroup(e)) {
                System.out.println("nested: " + e.getName().toString() + " parent: " + v.getValName().get());
                ValueData vd = new ValueData(e.getName().toString());
                vd.setNestedGroup(true);
                vd.setParentNode(v);
                v.addSubParam(vd);

                for (Entry g : ((Group) e).getEntries()) {
                    visitTwo(g, dataList, vd);
                }
            } else if (!"problem".equals(e.getName()) && hasOnlyGroups(e) && !isVal(e) && isANum(e.getName())) {
                System.out.println("Optional: " + e.getName().toString() + " parent: " + v.getValName().get());
                ValueData vd = new ValueData(e.getName().toString());
                vd.setParentNode(v);
                //vd.setNestedGroup(true);
                vd.setOption(true);
                v.addSubParam(vd);

                for (Entry g : ((Group) e).getEntries()) {
                    visitTwo(g, dataList, vd);
                }

            } else if (isVal(e)) {
                if (!hasSubParams((Group) e)) {
                    System.out.println("Value: " + e.getName().toString() + " & no SubParams" + " parent: " + v.getValName().get());
                    ValueData xd = new ValueData(e.getName().toString());
                    xd.isValue(true);
                    setInfos(xd, (Group) e);
                    ActualDataValue adv = new ActualDataValue();
                    adv.setType(xd.getType().get());
                    if (xd.getDefaultVal() != null) {
                        adv.setValue(xd.getDefaultVal());
                    }
                    xd.setActData(adv);
                    xd.setParentNode(v);
                    v.addSubParam(xd);
                    //System.out.println("NAME: " + v.getValName().get() + " OPT: " + v.getOptions().size());

                } else if (hasSubParams((Group) e)) {
                    System.out.println("Value: " + e.getName().toString() + " & SubParams" + " parent: " + v.getValName().get());
                    ValueData cd = new ValueData(e.getName().toString());
                    cd.setNestedGroup(false);
                    cd.setParentNode(v);
                    v.addSubParam(cd);

                    for (Entry g : ((Group) e).getEntries()) {
                        visitTwo(g, dataList, cd);
                    }
                }
            } else if (!"problem".equals(e.getName().toString()) && hasOnlyGroups(e) && !isVal(e)) {
                System.out.println("nested: " + e.getName().toString() + " parent: " + v.getValName().get());
                ValueData vd = new ValueData(e.getName().toString());
                vd.setParentNode(v);
                v.addSubParam(vd);

                for (Entry g : ((Group) e).getEntries()) {
                    visitTwo(g, dataList, vd);
                }
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

    private static boolean hasOnlyGroupsNoVal(Entry e) {
        if (e instanceof Group) {
            for (Entry f : ((Group) e).getEntries()) {
                if (f instanceof Group && !isVal(f)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasOnlyGroups(Entry e) {
        if (e instanceof Group) {
            for (Entry f : ((Group) e).getEntries()) {
                if (f instanceof Group) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasSubParams(Group g) {
        for (Entry e : ((Group) g).getEntries()) {
            if (isVal(e) && (isANum(e.getName().toString()))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNestedGroup(Entry e) {

        if (e instanceof Group) {
            if (!isANum(e.getName().toString())) {
                if (((Group) e).getEntries() != null) {
                    for (Entry en : ((Group) e).getEntries()) {
                        if (!isANum(en.getName().toString())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean isANum(String str) {
        boolean isNum = NumberUtils.isNumber(str);
        return isNum;
    }

    private static boolean isVal(Entry e) {
        boolean isVal = false;

        if (e instanceof Group) {
            for (Entry g : ((Group) e).getEntries()) {
                if (g instanceof Value) {
                    if ("type".equals(g.getName().toString())) {
                        isVal = true;
                    }
                }
            }
        }
        return isVal;
    }*/
    ////TEST
    public static void visitOne(Entry e, List<ValueData> dataList) {
        if (e instanceof Group) {
            if (!"problem".equals(e.getName()) && isVal(e) && !"root".equals(e.getName())) {
                //VALUE
                System.out.println(e.getName() + " is a Val!");
                ValueData xd = new ValueData(e.getName());
                xd.isValue(true);
                setInfos(xd, (Group) e);
                ActualDataValue adv = new ActualDataValue();
                adv.setType(xd.getType().get());
                if (xd.getDefaultVal() != null) {
                    adv.setValue(xd.getDefaultVal());
                }
                xd.setActData(adv);
                dataList.add(xd);

            } else if (!"problem".equals(e.getName()) && !NumberUtils.isNumber(e.getName()) && !isVal(e) && !"root".equals(e.getName())) {
                // NICHT-OPTIONALE GRUPPE
                System.out.println(e.getName() + " is NOT-OPTIONAL!");
                ValueData xd = new ValueData(e.getName());
                xd.setOptional(false);
                dataList.add(xd);
                for (Entry p : ((Group) e).getEntries()) {
                    visitTwo(p, dataList, xd);
                }

            } else if (!"problem".equals(e.getName()) && NumberUtils.isNumber(e.getName()) && !"root".equals(e.getName())) {
                //OPTIONALE GRUPPE
                System.out.println(e.getName() + " is OPTIONAL!");
                ValueData xd = new ValueData(e.getName());
                xd.setOptional(true);
                dataList.add(xd);
                for (Entry p : ((Group) e).getEntries()) {
                    visitTwo(p, dataList, xd);
                }

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
                setInfos(xd, (Group) e);
                ActualDataValue adv = new ActualDataValue();
                adv.setType(xd.getType().get());
                if (xd.getDefaultVal() != null) {
                    adv.setValue(xd.getDefaultVal());
                }
                xd.setActData(adv);
                xd.setParentNode(v);
                v.addSubParam(xd);
            } else if (!"problem".equals(e.getName()) && !NumberUtils.isNumber(e.getName()) && !isVal(e)) {
                //NICHT-OPTIONALE GRUPPE
                System.out.println(e.getName() + " is NOT-OPTIONAL!");
                ValueData xd = new ValueData(e.getName());
                xd.setOptional(false);
                xd.setParentNode(v);
                v.addSubParam(xd);

                for (Entry p : ((Group) e).getEntries()) {
                    visitTwo(p, dataList, xd);
                }
            } else if (!"problem".equals(e.getName()) && NumberUtils.isNumber(e.getName())) {
                //OPTIONALE GRUPPE
                System.out.println(e.getName() + " is OPTIONAL!");
                ValueData xd = new ValueData(e.getName());
                xd.setOptional(false);
                xd.setParentNode(v);
                v.addSubParam(xd);

                for (Entry p : ((Group) e).getEntries()) {
                    visitTwo(p, dataList, xd);
                }

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

    ////TEST

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
}
