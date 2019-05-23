package edu.gcsc.vrl.luaparser;

import com.google.common.io.ByteStreams;
import org.apache.commons.lang.math.NumberUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

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
            System.out.println("VAL: " + v.getValName());
            ActualDataValue adv = new ActualDataValue();
            settingType((Value) e, adv);
            adv.setValue(((Value) e).getValueAsString());
            v.setActData(adv);
            System.out.println("wert: " + v.getActData().getValue());
            v.isValue(true);
            lv.add(v);
        } else if (e instanceof Group) {
            if (!e.getName().equals("problem") && !e.getName().equals("root")) {
                if (onlyGroups((Group) e)) {
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
                        System.out.println("GROUP1: " + v.getValName());
                        lv.add(v);

                        for (Entry ed : ((Group) e).getEntries()) {
                            ValueData d = new ValueData(ed.getName());
                            System.out.println("VAL: " + d.getValName());
                            ActualDataValue adv = new ActualDataValue();
                            settingType((Value) ed, adv);
                            adv.setValue(((Value) ed).getValueAsString());
                            d.setActData(adv);
                            System.out.println("wert: " + d.getActData().getValue());
                            d.isValue(true);
                            v.addSubParam(d);
                            d.setParentNode(v);
                        }
                    }
                } else {
                    ValueData v = new ValueData(e.getName());
                    System.out.println("GROUP1: " + v.getValName());
                    lv.add(v);
                    for (Entry ed : ((Group) e).getEntries()) {
                        if (ed instanceof Value) {
                            ValueData vf = new ValueData(ed.getName());
                            System.out.println("VAL: " + vf.getValName());
                            ActualDataValue adv = new ActualDataValue();
                            settingType((Value) ed, adv);
                            adv.setValue(((Value) ed).getValueAsString());
                            vf.setActData(adv);
                            System.out.println("wert: " + vf.getActData().getValue());
                            vf.isValue(true);
                            v.addSubParam(vf);
                            vf.setParentNode(v);
                        } else if (ed instanceof Group) {
                            ValueData vf = new ValueData(ed.getName());
                            System.out.println("GROUP2: " + vf.getValName());
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
            ValueData x = new ValueData(e.getName());
            x.setParentNode(v);
            v.addSubParam(x);

            for (Entry ede : ((Group) e).getEntries()) {
                if (ede instanceof Value) {
                    ValueData vd = new ValueData(ede.getName());
                    System.out.println("VAL: " + vd.getValName());
                    ActualDataValue adv = new ActualDataValue();
                    settingType((Value) ede, adv);
                    adv.setValue(((Value) ede).getValueAsString());
                    vd.setActData(adv);
                    System.out.println("wert: " + vd.getActData().getValue());
                    vd.isValue(true);
                    x.addSubParam(vd);
                    vd.setParentNode(x);
                    System.out.println("VAL: " + vd.getValName());
                } else if (ede instanceof Group) {
                    if (!ede.getName().equals("problem") && !ede.getName().equals("root")) {
                        ValueData vd = new ValueData(ede.getName());
                        System.out.println("GROUP3: " + vd.getValName());
                        x.addSubParam(vd);
                        vd.setParentNode(x);

                        if (onlyGroups((Group) ede)) {
                            for (Entry ed : ((Group) ede).getEntries()) {
                                visitGroup((Group) ed, vd);
                            }
                        } else if (onlyValues((Group) ede)) {
                            if (isArrayOfValues((Group) ede)) {
                                System.out.println("Array of vals: " +ede.getName());
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
                                    System.out.println("VAL: " + vf.getValName());
                                    ActualDataValue adv = new ActualDataValue();
                                    settingType((Value) ed, adv);
                                    adv.setValue(((Value) ed).getValueAsString());
                                    vf.setActData(adv);
                                    System.out.println("wert: " + vf.getActData().getValue());
                                    vf.isValue(true);
                                    vd.addSubParam(vf);
                                    vf.setParentNode(vd);
                                } else if (ed instanceof Group) {
                                    ValueData vf = new ValueData(ed.getName());
                                    System.out.println("GROUP4: " + vf.getValName());
                                    vd.addSubParam(vf);
                                    vf.setParentNode(vd);
                                    visitGroup(ed, vf);
                                }
                            }
                        }
                    } else {
                    }
                }
            }
        } else if (e instanceof Value) {
            ValueData ve = new ValueData(e.getName());
            System.out.println("VAL: " + ve.getValName());
            ActualDataValue adv = new ActualDataValue();
            settingType((Value) e, adv);
            adv.setValue(((Value) e).getValueAsString());
            ve.setActData(adv);
            System.out.println("wert: " + ve.getActData().getValue());
            ve.isValue(true);
            v.addSubParam(ve);
            ve.setParentNode(v);
        }
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
        System.out.println("check Group: " + g.getName());
        for (Entry e : g.getEntries()) {
            if (e instanceof Value && NumberUtils.isNumber(e.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the data type for a <code>Value</code>
     * Only for a single value.
     *
     * @param v   Value
     * @param adv ActualDataValue-object
     */
    private static void settingType(Value v, ActualDataValue adv) {
        if (v.isString()) {
            adv.setType("String");
        } else if (v.isInteger()) {
            adv.setType("Integer");
        } else if (v.isDouble()) {
            adv.setType("Double");
        } else if (v.isBoolean()) {
            adv.setType("Boolean");
        } else if (v.isFunction()) {
            adv.setType("Function");
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
                                    p.getActData().setValueLoad(v.getActData().getValue());
                                    p.setSelectedNew(true);
                                } else {
                                    ActualDataValue adv = new ActualDataValue();
                                    adv.setType(v.getActData().getType());
                                    adv.setValueLoad(v.getActData().getValue());
                                    p.setActData(adv);
                                    p.setSelectedNew(true);
                                }
                            }
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
                        //System.out.println("1.RT: " + s.getValName() + " LUA: " + v.getValName());
                        for (ValueData opt : s.getOptions()) {
                            if (opt.getOptions() != null) {
                                match(opt.getOptions(), v.getOptions());
                            }
                        }
                    } else if (s.isNotOptGroup() && s.getValName().equals(v.getValName())) {
                        //System.out.println("2.RT: " + s.getValName() + " LUA: " + v.getValName());
                        match(s.getOptions(), v.getOptions());
                    }
                }
            }
        }
        lua.remove(0);
    }
}
