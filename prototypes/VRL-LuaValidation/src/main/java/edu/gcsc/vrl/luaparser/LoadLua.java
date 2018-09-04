package edu.gcsc.vrl.luaparser;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class LoadLua {
    public LoadLua() {
        throw new AssertionError();
    }

    public static Group parseLuaFile(String filepath) throws IOException {
        // load lua code from resources
        byte[] code2Data = ByteStreams.toByteArray(Main.class.getResourceAsStream(filepath));
        String code = new String(code2Data, "UTF-8");

        // convert code to group
        Group importedCode = Group.toGroup(code);

        return importedCode;
    }

    public static void visitingLuaCode(Entry e, List<ValueData> lv) {
        if (e instanceof Value) {
            ValueData v = new ValueData(e.getName().toString());
            ActualDataValue adv = new ActualDataValue();
            settingType((Value) e, adv);
            adv.setValue(((Value) e).getValueAsString());
            v.setActData(adv);
            v.isValue(true);
            lv.add(v);
        } else if (e instanceof Group) {
            if (!e.getName().toString().equals("problem") && !e.getName().toString().equals("root")) {
                ValueData v = new ValueData(e.getName().toString());
                lv.add(v);
                if (onlyGroups((Group) e)) {
                    for (Entry ed : ((Group) e).getEntries()) {
                        visitGroup((Group) ed, v);
                    }
                } else if (onlyValues((Group) e)) {
                    for (Entry ed : ((Group) e).getEntries()) {
                        ValueData d = new ValueData(ed.getName().toString());
                        ActualDataValue adv = new ActualDataValue();
                        settingType((Value) ed, adv);
                        adv.setValue(((Value) ed).getValueAsString());
                        d.setActData(adv);
                        d.isValue(true);
                        v.addSubParam(d);
                        d.setParentNode(v);
                    }
                } else {
                    System.out.println(e.getName().toString() + " ++++");
                    for (Entry ed : ((Group) e).getEntries()) {
                        if (ed instanceof Value) {
                            System.out.println(ed.getName().toString() + " VAL");
                            ValueData vf = new ValueData(ed.getName().toString());
                            ActualDataValue adv = new ActualDataValue();
                            settingType((Value) ed, adv);
                            adv.setValue(((Value) ed).getValueAsString());
                            vf.setActData(adv);
                            vf.isValue(true);
                            v.addSubParam(vf);
                            vf.setParentNode(v);
                        } else if (ed instanceof Group) {
                            System.out.println(ed.getName().toString() + " GROUP");
                            ValueData vf = new ValueData(ed.getName().toString());
                            v.addSubParam(vf);
                            vf.setParentNode(v);
                            visitGroup(ed, vf);
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

    public static void visitGroup(Entry e, ValueData v) {
        if (e instanceof Group) {
            for (Entry ede : ((Group) e).getEntries()) {
                if (ede instanceof Value) {
                    ValueData vd = new ValueData(ede.getName().toString());
                    ActualDataValue adv = new ActualDataValue();
                    settingType((Value) ede, adv);
                    adv.setValue(((Value) ede).getValueAsString());
                    vd.setActData(adv);
                    vd.isValue(true);
                    v.addSubParam(vd);
                    vd.setParentNode(v);
                } else if (ede instanceof Group) {
                    if (!ede.getName().toString().equals("problem") && !ede.getName().toString().equals("root")) {
                        ValueData vd = new ValueData(ede.getName().toString());
                        v.addSubParam(vd);
                        vd.setParentNode(v);

                        if (onlyGroups((Group) ede)) {
                            for (Entry ed : ((Group) ede).getEntries()) {
                                visitGroup((Group) ed, vd);
                            }
                        } else if (onlyValues((Group) ede)) {
                            for (Entry ed : ((Group) ede).getEntries()) {
                                ValueData d = new ValueData(ed.getName().toString());
                                ActualDataValue adv = new ActualDataValue();
                                settingType((Value) ed, adv);
                                adv.setValue(((Value) ed).getValueAsString());
                                d.setActData(adv);
                                d.isValue(true);
                                vd.addSubParam(d);
                                d.setParentNode(vd);
                            }
                        } else {
                            for (Entry ed : ((Group) ede).getEntries()) {
                                if (ed instanceof Value) {
                                    ValueData vf = new ValueData(ed.getName().toString());
                                    ActualDataValue adv = new ActualDataValue();
                                    settingType((Value) ed, adv);
                                    adv.setValue(((Value) ed).getValueAsString());
                                    vf.setActData(adv);
                                    vf.isValue(true);
                                    vd.addSubParam(vf);
                                    vf.setParentNode(vd);
                                } else if (ed instanceof Group) {
                                    ValueData vf = new ValueData(ed.getName().toString());
                                    vd.addSubParam(vf);
                                    vf.setParentNode(vd);
                                    visitGroup(ed, vf);
                                }
                            }
                        }
                    } else {
                        // TEST
                    }
                }
            }
        } else if (e instanceof Value) {
            ValueData ve = new ValueData(e.getName().toString());
            ActualDataValue adv = new ActualDataValue();
            settingType((Value) e, adv);
            adv.setValue(((Value) e).getValueAsString());
            ve.setActData(adv);
            //v.setActData(adv);
            ve.isValue(true);
            v.addSubParam(ve);
            ve.setParentNode(v);
        }
    }

    private static boolean onlyGroups(Group group) {
        for (Entry e : group.getEntries()) {
            if (e instanceof Value) {
                return false;
            }
        }
        return true;
    }

    private static boolean onlyValues(Group group) {
        for (Entry e : group.getEntries()) {
            if (e instanceof Group) {
                return false;
            }
        }
        return true;
    }

    private static void settingType(Value v, ActualDataValue adv) {
        if (v.isString()) {
            adv.setType("String");
        } else if (v.isDouble()) {
            adv.setType("Double");
        } else if (v.isInteger()) {
            adv.setType("Integer");
        } else if (v.isBoolean()) {
            adv.setType("Boolean");
        } else if (v.isFunction()) {
            adv.setType("Function");
        }
    }

    public static void matchingValues(List<ValueData> runtimeSpec, List<ValueData> loadedLuaFileVals){

    }
}
