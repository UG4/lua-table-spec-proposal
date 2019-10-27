package edu.gcsc.vrl.luaparser;

import com.google.common.io.ByteStreams;
import org.apache.commons.lang.math.NumberUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class loads a lua file and matches it parameters on the actual data set.
 */
public final class LoadLua {
    public LoadLua() {
        throw new AssertionError();
    }


    /**
     * Parses a lua-file
     *
     * @param filepath file path
     * @return importedCode lua-group
     */
    public static Group parseLuaFile(String filepath) throws IOException {

        // load lua code from resources
        byte[] code2Data = ByteStreams.toByteArray(new FileInputStream(filepath));
        String code = new String(code2Data, "UTF-8");

        // convert code to group
        Group importedCode = Group.toGroup(code);

        return importedCode;
    }

    public static Group parseLuaFile(String filepath, boolean test) throws IOException {


        // load lua code
        byte[] code2Data = ByteStreams.toByteArray(Main.class.getResourceAsStream(filepath));
        String code = new String(code2Data, "UTF-8");

        // convert code to group
        Group importedCode = Group.toGroup(code);

        return importedCode;
    }

    /**
     * Iterates through the imported lua-group and creates <code>ValueData</code>-objects for
     * each element. Furthermore it sets the values, if a element is a parameter.
     *
     * @param e  lua-entry
     * @param lv data list
     */
    public static void visitingLuaCode(Entry e, List<ValueData> lv) {

        if (e instanceof Value) {
            ValueData v = new ValueData(e.getName());
            ActualDataValue adv = new ActualDataValue();
            settingType((Value) e, adv);
            adv.setValue(((Value) e).getValueAsString());
            v.setActData(adv);
            v.isValue(true);
            lv.add(v);
        } else if (e instanceof Group) {
            if (!e.getName().equals("problem") && !e.getName().equals("root")) {
                if (isTimeTable((Group) e)){
                    ValueData v = new ValueData(e.getName());
                    v.isValue(true);
                    v.setIsTable(true);
                    v.setIsTimeTable(true);
                    extractTimeTableValues((Group) e, v);
                    System.out.println("TimeTable geladen: " + e.getName());
                    lv.add(v);
                } else if(onlyGroups((Group) e)) {
                    ValueData v = new ValueData(e.getName());
                    System.out.println("GROUP1: " + v.getValName());
                    lv.add(v);
                    for (Entry ed : ((Group) e).getEntries()) {
                        visitGroup((Group) ed, v);
                    }
                } else if (onlyValues((Group) e)) {
                    if (isArrayOfValues((Group) e)) {
                        ValueData v = new ValueData(e.getName());
                        System.out.println("Arrs: " + v.getValName());
                        lv.add(v);

                        StringBuilder sb = new StringBuilder();
                        String actType = "";
                        int counter = 0;
                        for (Entry ed : ((Group) e).getEntries()) {
                            if (counter == 0) {
                                actType = settingType((Value) ed);
                                counter++;
                            }
                            sb.append(((Value) ed).getValueAsString()).append(",");
                        }
                        if (sb.length() > 0) {
                            sb.setLength(sb.length() - 1);
                        }
                        if (v.getActData() != null && v.getActData().getValue() != null) {
                            v.getActData().setValue(sb.toString());
                            v.isValue(true);
                        } else {
                            ActualDataValue actData = new ActualDataValue();
                            if (!actType.isEmpty()) {
                                actData.setType(actType);
                                actData.setValue(sb.toString());
                                v.setActData(actData);
                            }
                            v.isValue(true);
                        }
                    } else {
                        ValueData v = new ValueData(e.getName());
                        System.out.println("GROUP2: " + v.getValName());
                        lv.add(v);

                        for (Entry ed : ((Group) e).getEntries()) {
                            ValueData d = new ValueData(ed.getName());
                            ActualDataValue adv = new ActualDataValue();
                            settingType((Value) ed, adv);
                            adv.setValue(((Value) ed).getValueAsString());
                            d.setActData(adv);
                            d.isValue(true);
                            v.addSubParam(d);
                            d.setParentNode(v);

                        }
                    }
                } else {
                    ValueData v = new ValueData(e.getName());
                    System.out.println("GROUP3: " + v.getValName());
                    lv.add(v);
                    for (Entry ed : ((Group) e).getEntries()) {
                        if (ed instanceof Value) {
                            ValueData vf = new ValueData(ed.getName());
                            ActualDataValue adv = new ActualDataValue();
                            settingType((Value) ed, adv);
                            adv.setValue(((Value) ed).getValueAsString());
                            vf.setActData(adv);
                            vf.isValue(true);
                            v.addSubParam(vf);
                            vf.setParentNode(v);
                        } else if (ed instanceof Group) {
                            ValueData vf = new ValueData(ed.getName());
                            System.out.println("GROUP4: " + vf.getValName());
                            v.addSubParam(vf);
                            vf.setParentNode(v);
                            visitGroup(ed, v);
                        }
                    }
                }
            } else {
                for (Entry l : ((Group) e).getEntries()) {
                    visitingLuaCode(l, lv);
                }
            }
        }
    }

    /*
     * Helping method
     * */
    private static void visitGroup(Entry e, ValueData v) {
        if (e instanceof Group) {
            if (isTimeTable((Group) e)) {
                ValueData x = new ValueData(e.getName());
                x.isValue(true);
                x.setIsTable(true);
                x.setIsTimeTable(true);
                extractTimeTableValues((Group) e, x);
                System.out.println("TimeTable geladen(bei visitGroup()): " + e.getName());
                x.setParentNode(v);
                v.addSubParam(x);
            } else {
                ValueData x = new ValueData(e.getName());
                x.setParentNode(v);
                v.addSubParam(x);

                for (Entry ede : ((Group) e).getEntries()) {
                    if (ede instanceof Value) {
                        ValueData vd = new ValueData(ede.getName());
                        ActualDataValue adv = new ActualDataValue();
                        settingType((Value) ede, adv);
                        adv.setValue(((Value) ede).getValueAsString());
                        vd.setActData(adv);
                        vd.isValue(true);
                        x.addSubParam(vd);
                        vd.setParentNode(x);
                    } else if (ede instanceof Group) {
                        if (!ede.getName().equals("problem") && !ede.getName().equals("root")) {
                            ValueData vd = new ValueData(ede.getName());
                            System.out.println("GROUP5: " + vd.getValName());
                            x.addSubParam(vd);
                            vd.setParentNode(x);

                            if (isTimeTable((Group) ede)) {
                                ValueData zz = new ValueData(ede.getName());
                                zz.isValue(true);
                                zz.setIsTable(true);
                                zz.setIsTimeTable(true);
                                extractTimeTableValues((Group) ede, zz);
                                System.out.println("TimeTable geladen: " + ede.getName());
                                zz.setParentNode(vd);
                                vd.addSubParam(zz);
                            } else if (onlyGroups((Group) ede)) {
                                for (Entry ed : ((Group) ede).getEntries()) {
                                    visitGroup((Group) ed, vd);
                                }
                            } else if (onlyValues((Group) ede)) {
                                if (isArrayOfValues((Group) ede)) {
                                    System.out.println("Array of vals: " + ede.getName());
                                    StringBuilder sb = new StringBuilder();
                                    String actType = "";
                                    int counter = 0;
                                    for (Entry ed : ((Group) ede).getEntries()) {
                                        if (counter == 0) {
                                            actType = settingType((Value) ed);
                                            counter++;
                                        }
                                        sb.append(((Value) ed).getValueAsString()).append(",");
                                    }
                                    if (sb.length() > 0) {
                                        sb.setLength(sb.length() - 1);
                                    }
                                    if (vd.getActData() != null && vd.getActData().getValue() != null) {
                                        vd.getActData().setValue(sb.toString());
                                        vd.isValue(true);
                                    } else {
                                        ActualDataValue actData = new ActualDataValue();
                                        if (!actType.isEmpty()) {
                                            actData.setType(actType);
                                            actData.setValue(sb.toString());
                                            vd.setActData(actData);
                                        }
                                        vd.isValue(true);
                                    }
                                } else {
                                    for (Entry ed : ((Group) ede).getEntries()) {
                                        ValueData d = new ValueData(ed.getName());
                                        System.out.println("GROUP: " + d.getValName());
                                        System.out.println("Value: " + ((Value) ed).getValueAsString());
                                        ActualDataValue adv = new ActualDataValue();
                                        settingType((Value) ed, adv);
                                        adv.setValue(((Value) ed).getValueAsString());
                                        d.setActData(adv);
                                        d.isValue(true);
                                        vd.addSubParam(d);
                                        d.setParentNode(vd);
                                    }
                                }
                            } else {
                                for (Entry ed : ((Group) ede).getEntries()) {
                                    if (ed instanceof Value) {
                                        ValueData vf = new ValueData(ed.getName());
                                        ActualDataValue adv = new ActualDataValue();
                                        settingType((Value) ed, adv);
                                        adv.setValue(((Value) ed).getValueAsString());
                                        vf.setActData(adv);
                                        vf.isValue(true);
                                        vd.addSubParam(vf);
                                        vf.setParentNode(vd);
                                    } else if (ed instanceof Group) {
                                        ValueData vf = new ValueData(ed.getName());
                                        System.out.println("GROUP6: " + vf.getValName());
                                        vd.addSubParam(vf);
                                        vf.setParentNode(vd);
                                        visitGroup(ed, vf);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (e instanceof Value) {
            ValueData ve = new ValueData(e.getName());
            ActualDataValue adv = new ActualDataValue();
            settingType((Value) e, adv);
            adv.setValue(((Value) e).getValueAsString());
            ve.setActData(adv);
            ve.isValue(true);
            v.addSubParam(ve);
            ve.setParentNode(v);
        }
    }

    private static boolean isTimeTable(Group g) {
        if (onlyGroups(g)) {
            if(onlyNamelessGroups(g)) {
                for (Entry e : g.getEntries()) {
                    if(!onlyValues((Group)e)){
                        return false;
                    } else {
                        return isArrayOfValues((Group)e);
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return false;
    }

    private static boolean onlyNamelessGroups(Group g) {
        for (Entry e : g.getEntries()){
            if (!NumberUtils.isNumber(e.getName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a Group contains Groups only
     *
     * @param group Lua-Group
     * @return boolean
     */
    private static boolean onlyGroups(Group group) {
        for (Entry e : group.getEntries()) {
            if (e instanceof Value) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a Group contains Values only
     *
     * @param group Lua-Group
     * @return boolean
     */
    private static boolean onlyValues(Group group) {
        for (Entry e : group.getEntries()) {
            if (e instanceof Group) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a Group is a array of values
     *
     * @param g Lua-Group
     * @return boolean
     */
    private static boolean isArrayOfValues(Group g) {
        for (Entry e : g.getEntries()) {
            if (e instanceof Value && NumberUtils.isNumber(e.getName())) {
                System.out.println("Discovered array of vals");
                return true;
            }
        }
        return false;
    }

    private static void extractTimeTableValues(Group timetable, ValueData vd) {
        System.out.println("Extract time-table vals");
        HashMap<String, String> tempMap = new HashMap<>();

        for (Entry e : timetable.getEntries()) {
            if (e instanceof Group) {
                StringBuilder valAsString = new StringBuilder();
                for (Entry val : ((Group) e).getEntries()){
                    if (val instanceof Value) {
                        valAsString.append(((Value) val).getValueAsString()).append(",");
                    }
                }
                valAsString.setLength(valAsString.length()-1);
                tempMap.put(e.getName(),valAsString.toString());
                valAsString = new StringBuilder();
            } else {
                System.out.println("timetables format is wrong");
            }
        }
        vd.setTable(tempMap);

        for (Map.Entry<String, String> ee : vd.getTable().entrySet()){
            System.out.println("LOADED KEY: " + ee.getKey() + " | LOADED VAL: " + ee.getValue());
        }
    }

    /**
     * Sets the data type for a <code>Value</code>
     * Only for a single value.
     *
     * @param v   Value
     * @param adv ActualDataValue-object
     */
    private static void settingType(Value v, ActualDataValue adv) {
        if(v.isInteger()){
            adv.setType("Integer");
        } else if (v.isDouble()){
            adv.setType("Double");
        } else if (v.isBoolean()){
            adv.setType("Boolean");
        } else if (v.isFunction()){
            adv.setType("Function");
        } else if (v.isString()){
            adv.setType("String");
        }
    }

    /**
     * Sets the data type for a <code>Value</code>
     * Only for arrays of values.
     *
     * @param v Value
     */
    private static String settingType(Value v) {

        if (v.isInteger()) {
            System.out.println(v.getName() + " int");
            return "Integer[]";
        } else if (v.isDouble()) {
            System.out.println(v.getName() + " dou");
            return "Double[]";
        } else if (v.isBoolean()) {
            System.out.println(v.getName() + " bool");
            return "Boolean[]";
        } else if (v.isString()) {
            System.out.println(v.getName() + " str");
            return "String[]";
        } else if (v.isFunction()) {
            System.out.println(v.getName() + " func");
            return "Function[]";
        } else {
            return "";
        }
    }


    /**
     * This method matches the parameters from the imported lua-file on the existing data set from the validationspec.
     * If a parameter matches, the actual values gets overwrited.
     *
     * @param spec actual data set
     * @param lua  imported lua parameters
     */
    public static void match(List<ValueData> spec, List<ValueData> lua) {
        ValueData rootNode = new ValueData("problem");
        for (ValueData x : lua) {
            rootNode.addSubParam(x);
            x.setParentNode(rootNode);
        }
        lua.add(0, rootNode);

        for (ValueData v : lua) {
            for (ValueData s : spec) {
                if (v.isAValue()) {
                    if (s.hasOptValue() && s.getValName().equals(v.getValName())) {
                        for (ValueData p : s.getOptions()) {
                            if (p.isOptValue()) {
                                if (p.getActData() != null && p.getActData().getValue() != null) {
                                    if(p.getActData().getType().equals(v.getActData().getType())) {
                                        p.getActData().setValueLoad(v.getActData().getValue());
                                        p.setSelectedNew(true);
                                    }
                                } else {
                                    ActualDataValue adv = new ActualDataValue();
                                    adv.setType(v.getActData().getType());
                                    adv.setValueLoad(v.getActData().getValue());
                                    p.setActData(adv);
                                    p.setSelectedNew(true);
                                }
                            }
                        }
                    }  else if (v.getValName().equals(s.getValName()) && s.isTable()) {
                        if (!s.getTable().isEmpty() && !v.getTable().isEmpty()) {
                            for (Map.Entry<String,String> ent : v.getTable().entrySet()) {
                                s.getTable().put(ent.getKey(),ent.getValue());
                            }
                        }
                        for (Map.Entry<String,String> ent2 : s.getTable().entrySet()) {
                            System.out.println("KEY: " + ent2.getKey() + " | VAL: " + ent2.getValue());
                        }

                    } else if (s.isAValue() && s.getValName().equals(v.getValName())) {
                        if (s.getActData() != null && s.getActData().getValue() != null) {
                            s.getActData().setValueLoad(v.getActData().getValue());
                            if (s.getParentNode() != null) {
                                s.getParentNode().setSelectedNew(true);
                            }
                        } else {
                            ActualDataValue adv = new ActualDataValue();
                            adv.setType(v.getActData().getType());
                            adv.setValueLoad(v.getActData().getValue());
                            s.setActData(adv);
                            if (s.getParentNode() != null) {
                                s.getParentNode().setSelectedNew(true);
                            }
                        }
                    } else if (s.isOption()) {
                        if (v.getParentNode() != null && v.getParentNode().getOptions() != null) {
                            match(s.getOptions(), v.getParentNode().getOptions());
                        }
                    } else if (s.isNotOptGroup()) {
                        if (v.getParentNode() != null && v.getParentNode().getOptions() != null) {
                            match(s.getOptions(), v.getParentNode().getOptions());
                        }
                    }
                } else if (v.getOptions() != null) {
                    if (s.getOptions() != null && s.isOption()) {
                        for (ValueData opt : s.getOptions()) {
                            if (opt.getOptions() != null) {
                                match(opt.getOptions(), v.getOptions());
                            }
                        }
                    } else if (s.isNotOptGroup() && s.getValName().equals(v.getValName())) {
                        match(s.getOptions(), v.getOptions());
                    } else if(s.isTable()) {
                        if (s.getValName().equals(v.getValName())) {
                            if (v.getOptions() != null) {
                                for (ValueData tableEntries : v.getOptions()) {
                                    if (s.getTable() != null) {
                                        if(s.getTable().containsKey(tableEntries.getValName())) {
                                            s.getTable().put(tableEntries.getValName(), tableEntries.getActData().getValue().toString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        lua.remove(0);
    }
}
