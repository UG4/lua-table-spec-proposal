package edu.gcsc.vrl.luaparser;

import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class generates the basic data set with <code>ValueData</code>-objects
 * from the validation-spec. It sets the information and all relation
 * */
public final class VisitingValidatorSpec {

    public VisitingValidatorSpec() { throw new AssertionError(); }

    /**
     * Searchs for specific elements like parameters, options, not-optional groups in a
     * data structure of <code>Groups</code>  and <code>Values</code>.
     *
     * @param dataList list to save the data
     * @param e data structure of <code>Groups</code> and <code>Values</code>
     * */
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
                // not-optional group
                System.out.println(e.getName() + " is NOT-OPTIONAL!");
                ValueData xd = new ValueData(e.getName());
                xd.setNotOptGroup(true);
                xd.setSelection(true);
                dataList.add(xd);
                for (Entry p : ((Group) e).getEntries()) {
                    visitTwo(p, dataList, xd);
                }

            } else if (!"problem".equals(e.getName()) && NumberUtils.isNumber(e.getName()) && !"root".equals(e.getName()) && !isOptVal(e)) {
                //OPTIONAL Group
                System.out.println(e.getName() + " is OPTIONAL!");
                ValueData xd = new ValueData(e.getName());
                xd.setOptional(true);
                dataList.add(xd);
                for (Entry p : ((Group) e).getEntries()) {
                    visitTwo(p, dataList, xd);
                }

            } else if (!"problem".equals(e.getName()) && !"root".equals(e.getName()) && isOptVal(e)) {
                //OPTIONAL VALUE
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
                //not-optional  group
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
                //optional
                System.out.println(e.getName() + " is OPTIONAL!");
                ValueData xd = new ValueData(e.getName());
                xd.setOptional(true);
                xd.setParentNode(v);
                v.addSubParam(xd);

                for (Entry p : ((Group) e).getEntries()) {
                    visitTwo(p, dataList, xd);
                }

            } else if (!"problem".equals(e.getName()) && !"root".equals(e.getName()) && isOptVal(e)) {
                //OPTIONAL value
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

    /**
     * Checks whether a Entry is a parameter
     *
     * @param e Entry
     * @return boolean is param
     * */
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

    /**
     * Checks whether a Entry is a option
     *
     * @param e Entry
     * @return boolean is option
     * */
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

    /**
     * Creates all information from a entry, that is a param
     *
     * @param e data to extract
     * @param vd object to set data
     * */
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
                    case "name":
                        vd.setName(((Value) l).getValueAsString());
                        break;
                }
            } else if (l instanceof Group) {
                System.out.println(l.getName().toString() + "    NAME");
                if (!isVal(l)) {
                    switch (l.getName().toString()) {
                        case "range":
                            if(!checkRangeIfString((Group)l)) {
                                double[] myValues = getRange((Group) l);
                                if (myValues.length == 2) {
                                    vd.setRange_min(myValues[0]);
                                    vd.setRange_max(myValues[1]);
                                } else if (myValues.length > 2) {
                                    vd.setValues(myValues);
                                }
                                break;
                            } else {
                                String[] myValues = getStringRange((Group) l);
                                vd.setStringRange(myValues);
                                for (String s : myValues){
                                    System.out.println(s);
                                }
                            }
                        case "visibility":
                            setVisibilityInfos((Group) l, vd);
                            break;
                        case "validation":
                            setValidationInfos((Group) l, vd);
                            break;
                        case "default":
                            setArrayOfDefault((Group) l, vd);
                            break;
                        case "styleOptions":
                            setStyleOptions((Group) l, vd);
                            break;
                    }
                }
            }
        }

    }

    private static void setStyleOptions(Group values, ValueData actItem){
        for(Entry e : values.getEntries()){
            if(e instanceof Value){
                if(e.getName().equals("desc")){
                    actItem.setStyle_option_desc(((Value) e).getValueAsString());
                }
            } else if (e instanceof Group){
                if(e.getName().equals("endings")){
                    List<String> ends = new ArrayList<>();
                    for(Entry ee : ((Group) e).getEntries()){
                        if(ee instanceof Value){
                            ends.add(((Value)ee).getValueAsString());
                        }
                    }
                    actItem.setStyle_option_endings(ends);
                }

            }
        }
    }

    private static void setArrayOfDefault(Group values, ValueData actItem){
            StringBuilder sb = new StringBuilder();
            for (Entry e : values.getEntries()) {
                sb.append(((Value) e).getValueAsString()).append(",");
            }
            sb.setLength(sb.length() - 1);
            actItem.setDefaultVal(sb.toString());
    }

    private static boolean checkRangeIfString(Group g){
        boolean isString = true;

        for (Entry e : g.getEntries()){
            if(e.getName().equals("values")) {
                for(Entry ee : ((Group)e).getEntries()) {
                    if (!((Value) ee).getValue().isstring()) {
                        isString = false;
                    }
                }
            } else {
                isString = false;
            }
        }
        return isString;
    }

    private static String[] getStringRange(Group g){
        for(Entry e : g.getEntries()){
            if(e.getName().equals("values")) {
                String[] stringsInRange = new String[((Group)e).getEntries().size()];
                int counter = 0;
                for(Entry ee: ((Group)e).getEntries()) {
                    String tempStr = ((Value) ee).getValueAsString();
                    System.out.println(tempStr);
                    stringsInRange[counter] = tempStr;
                    counter++;
                }
                return stringsInRange;
            }
        }
        return null;
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
