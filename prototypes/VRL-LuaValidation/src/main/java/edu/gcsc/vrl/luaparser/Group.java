/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.luaparser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

/**
 * Named lua table.
 * 
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class Group implements Entry {

    /**
     * group name
     */
    private final String name;
    
    /**
     * child entries
     */
    private final List<Entry> entries = new ArrayList<>();
    
    /**
     * group parent
     */
    private Group parent;

    /**
     * Creates a new group with the specified name.
     * 
     * @param name group name
     */
    Group(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Adds a child entry to this group.
     * @param e entry to add
     */
    void add(Entry e) {
        entries.add(e);
        e.setParent(this);
    }

    /**
     * Returns an unmodifiable list of all child elements of this group.
     * @return 
     */
    public List<Entry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Converts the specified lua code.
     * @param code lua code to convert
     * @return specified lua code as group element
     */
    public static Group toGroup(String code) {
        LuaValueToGroup luaValueToGroup = new LuaValueToGroup();
        return luaValueToGroup.parse(code);
    }

    @Override
    public void setParent(Group parent) {
        this.parent = parent;
    }

    @Override
    public Group getParent() {
        return parent;
    }
}

/**
 * Helper class for lua code conversion.
 * 
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
class LuaValueToGroup {

    LuaInterpreter interpreter;

    public LuaValueToGroup() {
        this.interpreter = new LuaInterpreter();
    }

    Group parse(String code) {
        interpreter.load(code);
        interpreter.run();

        LuaTable t = interpreter.getGlobals().checktable();

        return visitGroup(t, "root");
    }

    Value visitValue(LuaValue v, String name) {
        return new Value(v, name);
    }

    Group visitGroup(LuaTable table, String name) {
        Group g = new Group(name);
        for (LuaValue key : table.keys()) {
            LuaValue tEntry = table.get(key);
            if (tEntry.istable()) {
                LuaTable t = tEntry.checktable();
                g.add(visitGroup(t, key.tojstring()));
            } else {
                g.add(visitValue(tEntry, key.tojstring()));
            }
        }

        return g;
    }

}
