package edu.gcsc.vrl.luaparser;

import edu.gcsc.vrl.luaparser.ValueData;

import java.util.Map;

public final class Match {
    public Match() { throw new AssertionError(); }

    public static void matchLuaCode(Map<Integer, ValueData> spec, Map<Integer, ValueData> luaImport) {
        for(Map.Entry<Integer,ValueData> pair : luaImport.entrySet()) {
            if(!pair.getKey().equals("problem") && !pair.getKey().equals("root")) {
                if (pair.getValue().isAValue()) {
                    if(pair.getValue().getParentNode() != null) {
                        if (!spec.get(pair.getValue().getParentNode().getHash()).isTable()) {
                            if (spec.get(pair.getKey()) != null) {
                                if (pair.getValue().isTimeTable()) {
                                    // Import-Param is a TimeTable
                                    if (!spec.get(pair.getKey()).getTable().isEmpty()) {
                                        for (Map.Entry<String, String> ent : pair.getValue().getTable().entrySet()) {
                                            spec.get(pair.getKey()).getTable().put(ent.getKey(), ent.getValue());
                                        }
                                        System.out.println("Set all vals!");
                                    }
                                } else {
                                    if (pair.getValue().getActData().getValue() != null) {
                                        spec.get(pair.getKey()).getActData().setValueLoad(pair.getValue().getActData().getValue());
                                    }
                                }
                            } else {
                                System.out.println("Entry: " + pair.getValue().getValName() + " not found!");
                            }
                        } else {
                            if (!spec.get(pair.getValue().getParentNode().getHash()).getTable().isEmpty()) {
                                spec.get(pair.getValue().getParentNode().getHash()).getTable().put(pair.getValue().getValName(), pair.getValue().getActData().getValue().toString());
                            }
                        }
                    }
                }
            }
        }
    }
}
