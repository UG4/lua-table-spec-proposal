package edu.gcsc.vrl.luaparser;

import com.google.common.io.ByteStreams;
import org.apache.commons.lang.math.NumberUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public final class LoadLua {
    public LoadLua() {
        throw new AssertionError();
    }

    public static Group parseLuaFile(String filepath) throws IOException {
        // load lua code from resources
        byte[] code2Data = ByteStreams.toByteArray(new FileInputStream(filepath));
        String code = new String(code2Data, "UTF-8");

        // convert code to group
        Group importedCode = Group.toGroup(code);

        return importedCode;
    }

    public static void visitingLuaCode(Entry e, List<ValueData> lv) {
        if (e instanceof Value) {
            ValueData v = new ValueData(e.getName());
            System.out.println("VAL: " + v.getValName().get());
            ActualDataValue adv = new ActualDataValue();
            settingType((Value) e, adv);
            adv.setValue(((Value) e).getValueAsString());
            v.setActData(adv);
            System.out.println("wert: " + v.getActData().getValue());
            v.isValue(true);
            lv.add(v);
        } else if (e instanceof Group) {
            if (!e.getName().equals("problem") && !e.getName().equals("root")) {
                ValueData v = new ValueData(e.getName());
                System.out.println("GROUP1: " + v.getValName().get());
                lv.add(v);
                if (onlyGroups((Group) e)) {
                    for (Entry ed : ((Group) e).getEntries()) {
                        visitGroup((Group) ed, v);
                    }
                } else if (onlyValues((Group) e)) {
                    for (Entry ed : ((Group) e).getEntries()) {
                        ValueData d = new ValueData(ed.getName());
                        System.out.println("VAL: " + d.getValName().get());
                        ActualDataValue adv = new ActualDataValue();
                        settingType((Value) ed, adv);
                        adv.setValue(((Value) ed).getValueAsString());
                        d.setActData(adv);
                        System.out.println("wert: " + d.getActData().getValue());
                        d.isValue(true);
                        v.addSubParam(d);
                        d.setParentNode(v);
                    }
                } else {
                    for (Entry ed : ((Group) e).getEntries()) {
                        if (ed instanceof Value) {
                            ValueData vf = new ValueData(ed.getName());
                            System.out.println("VAL: " + vf.getValName().get());
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
                            System.out.println("GROUP2: " + vf.getValName().get());
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

    private static void visitGroup(Entry e, ValueData v) {
        if (e instanceof Group) {
            ValueData x = new ValueData(e.getName());
            x.setParentNode(v);
            v.addSubParam(x);

            for (Entry ede : ((Group) e).getEntries()) {
                if (ede instanceof Value) {
                    ValueData vd = new ValueData(ede.getName());
                    System.out.println("VAL: " + vd.getValName().get());
                    ActualDataValue adv = new ActualDataValue();
                    settingType((Value) ede, adv);
                    adv.setValue(((Value) ede).getValueAsString());
                    vd.setActData(adv);
                    System.out.println("wert: " + vd.getActData().getValue());
                    vd.isValue(true);
                    x.addSubParam(vd);
                    vd.setParentNode(x);
                    System.out.println("VAL: " + vd.getValName().get());
                } else if (ede instanceof Group) {
                    if (!ede.getName().equals("problem") && !ede.getName().equals("root")) {
                        ValueData vd = new ValueData(ede.getName());
                        System.out.println("GROUP3: " + vd.getValName().get());
                        x.addSubParam(vd);
                        vd.setParentNode(x);
                        // In die folgenden if-Abfragen einen Check einbauen, der prüft , ob alle Entries
                        // KEINE GROUP und KEINE VALUES sind.
                        // -> dann weiß man, dass die Group auch ein Parameter mit mehreren Eingabewerten(im Array) ist
                        // Wie unterscheidet man List<String> von einer List<Function>? Beides sind ja erstmal List<String>

                        if (onlyGroups((Group) ede)) {
                            for (Entry ed : ((Group) ede).getEntries()) {
                                visitGroup((Group) ed, vd);
                            }
                        } else if (onlyValues((Group) ede)) {
                            if (isArrayOfValues((Group) ede)) {
                                StringBuilder sb = new StringBuilder();
                                String actType = "";
                                int counter = 0;
                                for (Entry ed : ((Group) ede).getEntries()) {
                                    if (counter == 0) {
                                        actType = settingType((Value) ed);
                                        counter++;
                                    }
                                    sb.append(((Value)ed).getValueAsString()).append(",");
                                }
                                if(sb.length() > 0) {
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
                                    System.out.println("GROUP: " + d.getValName().get());
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
                                    System.out.println("VAL: " + vf.getValName().get());
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
                                    System.out.println("GROUP4: " + vf.getValName().get());
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
            ValueData ve = new ValueData(e.getName());
            System.out.println("VAL: " + ve.getValName().get());
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

    private static boolean isArrayOfValues(Group g) {
        System.out.println("check Group: " + g.getName());
        for (Entry e : g.getEntries()) {
            if (e instanceof Value && NumberUtils.isNumber(e.getName())) {
                return true;
            }
        }
        return false;
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

    private static String settingType(Value v) {

        if (v.isDouble()) {
            System.out.println(v.getName() + " dou");
            return "Double[]";
        } else if (v.isInteger()) {
            System.out.println(v.getName() + " int");
            return "Integer[]";
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

    public static void match(List<ValueData> spec, List<ValueData> lua) {
        for (ValueData v : lua) {
            for (ValueData s : spec) {
                if (v.isAValue()) {
                    if (s.hasOptValue() && s.getValName().get().equals(v.getValName().get())) {
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
                    } else if (s.isAValue() && s.getValName().get().equals(v.getValName().get())) {
                        if (s.getActData() != null && s.getActData().getValue() != null) {
                            s.getActData().setValueLoad(v.getActData().getValue());
                            if (s.getParentNode() != null && s.getParentNode().isOption()) {
                                s.getParentNode().setSelectedNew(true);
                            }
                        } else {
                            ActualDataValue adv = new ActualDataValue();
                            adv.setType(v.getActData().getType());
                            adv.setValueLoad(v.getActData().getValue());
                            s.setActData(adv);
                            if (s.getParentNode() != null && s.getParentNode().isOption()) {
                                s.getParentNode().setSelectedNew(true);
                            }
                        }
                    } else if (s.isOption()) {
                        if (v.getParentNode().getOptions() != null) {
                            match(s.getOptions(), v.getParentNode().getOptions());
                        }
                    } else if (s.isNotOptGroup()) {
                        if (v.getParentNode().getOptions() != null) {
                            match(s.getOptions(), v.getParentNode().getOptions());
                        }
                    }
                } else if (v.getOptions() != null) {
                    if (s.getOptions() != null && s.isOption()) {
                        System.out.println("1.RT: " + s.getValName().get() + " LUA: " + v.getValName().get());
                        for (ValueData opt : s.getOptions()) {
                            if (opt.getOptions() != null) {
                                match(opt.getOptions(), v.getOptions());
                            }
                        }
                    } else if (s.isNotOptGroup() && s.getValName().get().equals(v.getValName().get())) {
                        System.out.println("2.RT: " + s.getValName().get() + " LUA: " + v.getValName().get());
                        match(s.getOptions(), v.getOptions());
                    }
                }
            }
        }
    }
}
