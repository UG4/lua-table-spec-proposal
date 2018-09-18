package edu.gcsc.vrl.luaparser;

import com.google.common.io.ByteStreams;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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

                        if (onlyGroups((Group) ede)) {
                            for (Entry ed : ((Group) ede).getEntries()) {
                                visitGroup((Group) ed, vd);
                            }
                        } else if (onlyValues((Group) ede)) {
                            for (Entry ed : ((Group) ede).getEntries()) {
                                ValueData d = new ValueData(ed.getName());
                                System.out.println("GROUP: " + d.getValName().get());
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
    public static void match(List<ValueData> spec, List<ValueData> lua){
        for(ValueData v : lua){
            for(ValueData s : spec){
                if(v.isAValue()){
                    if(s.hasOptValue() && s.getValName().get().equals(v.getValName().get())){
                        for(ValueData p : s.getOptions()){
                            if(p.isOptValue()){
                                if(p.getActData() != null && p.getActData().getValue() != null){
                                    p.getActData().setValue(v.getActData().getValue());
                                } else {
                                    ActualDataValue adv = new ActualDataValue();
                                    adv.setType(v.getActData().getType());
                                    adv.setValue(v.getActData().getValue());
                                    p.setActData(adv);
                                }
                            }
                        }
                    } else if(s.isAValue() && s.getValName().get().equals(v.getValName().get())){
                        if(s.getActData() != null && s.getActData().getValue() != null){
                            s.getActData().setValue(v.getActData().getValue());
                        } else {
                            ActualDataValue adv = new ActualDataValue();
                            adv.setType(v.getActData().getType());
                            adv.setValue(v.getActData().getValue());
                            s.setActData(adv);
                        }
                    } else if(s.isOption()){
                        if(v.getParentNode().getOptions() != null) {
                            match(s.getOptions(), v.getParentNode().getOptions());
                        }
                    } else if(s.isNotOptGroup()){
                        if(v.getParentNode().getOptions() != null) {
                            match(s.getOptions(), v.getParentNode().getOptions());
                        }
                    }
                } else if(v.getOptions() != null){
                    System.out.println("NAME GROUP v : "+ v.getValName().get());
                    if(s.getOptions() != null && s.isOption()){
                        System.out.println("1.RT: "+s.getValName().get()+ " LUA: " + v.getValName().get());
                        for(ValueData opt : s.getOptions()){
                            if(opt.getOptions() != null){
                                match(opt.getOptions(),v.getOptions());
                            }
                        }
                    } else if(s.isNotOptGroup() && s.getValName().get().equals(v.getValName().get())){
                        System.out.println("2.RT: "+s.getValName().get()+ " LUA: " + v.getValName().get());
                        for(ValueData tets : s.getOptions()){
                            System.out.println("s : "+tets.getValName().get());
                        }
                        for(ValueData tets2 : v.getOptions()){
                            System.out.println("v : "+tets2.getValName().get());
                        }
                        match(s.getOptions(),v.getOptions());
                    }
                }
            }
        }
    }

    public static void matchingValues(List<ValueData> runtimeSpec, List<ValueData> loadedLuaFileVals) {
        for (int i = 0; i < loadedLuaFileVals.size(); i++) {
            ValueData iLua = loadedLuaFileVals.get(i);
            for (int j = 0; j < runtimeSpec.size(); j++) {
                ValueData jrt = runtimeSpec.get(j);
                if (iLua.isAValue() || iLua.isOptValue()) {
                    System.out.println("RT: "+jrt.getValName().get()+ " LUA: "+iLua.getValName().get());
                    if (jrt.getValName().get().equals(iLua.getValName().get()) && (jrt.isAValue() || jrt.isOptValue())) {
                        // Wenn der Value-Name direkt matched
                        System.out.println("RT: "+jrt.getValName().get() + " Lua: "+ iLua.getValName().get());
                        if(jrt.getActData() != null && jrt.getActData().getValue() != null) {
                            jrt.getActData().setValue(iLua.getActData().getValue());
                        } else {
                            System.out.println("TEST : " + jrt.getValName().get());
                            ActualDataValue adv = new ActualDataValue();
                            adv.setType(jrt.getType().get());
                            adv.setValue(iLua.getActData().getValue());
                            jrt.setActData(adv);
                        }
                        if(jrt.getParentNode() != null && jrt.getParentNode().isOption()){
                            jrt.getParentNode().setSelectedNew(true);
                        } else if(jrt.isOptValue()){
                            jrt.setSelectedNew(true);
                        }
                    } else if(jrt.hasOptValue()){
                        // Falls beim Runtime-Objekt ein optionaler-Value vorhanden ist
                        searchOptions(jrt,iLua);
                    }
                } else if (iLua.getOptions() != null) {
                    if (jrt.getValName().get().equals(iLua.getValName().get())) {
                        // Falls das Objekt aus dem Lua-File eine Gruppe ist, die Optionen besitzt
                        // Eventuell muss man noch den Fall hinzufügen, dass diese Optionen im Runtime-Objekt
                        // in einer optionalen Gruppe geschachtelt sind ?!?!
                        searchSubparams(jrt,iLua);
                    }
                }
            }
        }
    }

    private static void searchOptions(ValueData rt, ValueData vLua) {
        // Hier wird nach optionalen Werten in Tiefe 1 gesucht, da
        // alle tieferen optionalen Werte nicht mehr zugeordnet werden können
        for(ValueData v : rt.getOptions()){
            if(v.isOptValue()){
                v.getActData().setValue(vLua.getActData().getValue());
                if(v.getParentNode() != null && v.getParentNode().isOption()){
                    v.getParentNode().setSelectedNew(true);
                } else if(v.isOptValue()){
                    v.setSelectedNew(true);
                }
            }
        }
    }

    private static void searchSubparams(ValueData rt, ValueData vLua) {
        // Hier werden alle Subparameter durchsucht
        for (ValueData v : vLua.getOptions()) {
            for (ValueData rtV : rt.getOptions()) {
                if (v.isAValue()) {
                    if(rtV.isOption()) {
                        // Hier wird der Fall überprüft, ob die Subparameter im Runtime-Objekt in einer
                        // optionalen Gruppe geschachtelt sind
                        for(ValueData vd : rtV.getOptions()){
                            searchSubparams(rtV,vLua);
                        }
                    } else {
                        if (v.getValName().get().equals(rtV.getValName().get())) {
                            // Hier wird geprüft, ob der Name direkt matched
                            rtV.getActData().setValue(v.getActData().getValue());
                            if(rtV.getParentNode() != null && (rtV.getParentNode().isOption()||rtV.getParentNode().isOptValue())){
                                rtV.getParentNode().setSelectedNew(true);
                            }
                        } else {
                            // Hier wird überprüft, ob ein optionaler Value in Tiefe 1 vorhanden ist.
                            searchOptions(rtV, v);
                        }
                    }
                } else if (v.getOptions() != null) {
                    // Subparameter werden gecheckt
                    if(v.getValName().get().equals(rtV.getValName().get())){
                        searchSubparams(rtV, v);
                    }
                }
            }
        }

    }
}
