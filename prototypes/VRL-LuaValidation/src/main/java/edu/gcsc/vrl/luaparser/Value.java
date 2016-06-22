/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.luaparser;

import com.google.common.base.Objects;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

/**
 * Named value. Currently, this implementation suppports int, double, String and
 * functions.
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Value implements Entry {

    /**
     * {@code null} value (equivalent to Lua {@code nil}).
     */
    public static Value NULL = new Value(LuaValue.NIL, "");

    /**
     * internal lua value
     */
    private final LuaValue value;
    /**
     * name if this value
     */
    private final String name;

    /**
     * parent group
     */
    private Group parent;

    /**
     * Creates a new value with thespecified lua value.
     *
     * @param value lua value
     * @param name value name
     */
    Value(LuaValue value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * Returns the lua value.
     *
     * @return the value
     */
    LuaValue getValue() {
        return value;
    }

    /**
     * Determines, whether this value is null.
     *
     * @return {@code true} if this value is null; {@code false} otherwise
     */
    public boolean isNull() {
        return getValue() == null || Objects.equal(getValue(), LuaValue.NIL);
    }

    /**
     * Determines, whether this value is a string.
     *
     * @return {@code true} if this value is a string; {@code false} otherwise
     */
    public boolean isString() {
        return getValue().isstring();
    }

    /**
     * Determines, whether this value is a boolean.
     *
     * @return {@code true} if this value is a boolean; {@code false} otherwise
     */
    public boolean isBoolean() {
        return getValue().isboolean();
    }

    /**
     * Determines, whether this value is an integer.
     *
     * @return {@code true} if this value is an integer; {@code false} otherwise
     */
    public boolean isInteger() {
        return getValue().islong();
    }

    /**
     * Determines, whether this value is a double.
     *
     * @return {@code true} if this value is a double; {@code false} otherwise
     */
    public boolean isDouble() {
        try {
            getValue().checkdouble();
        } catch (LuaError ex) {
            return false;
        }

        return true;
    }

    /**
     * Determines, whether this value is a function.
     *
     * @return {@code true} if this value is a function; {@code false} otherwise
     */
    public boolean isFunction() {
        return getValue().isfunction();
    }

    /**
     * Returns this value as double.
     *
     * @return double value
     * @throws org.luaj.vm2.LuaError if this value cannot be returned as
     * requested
     */
    public double asDouble() {
        return getValue().checkdouble();
    }

    /**
     * Returns this value as integer.
     *
     * @return integer value
     * @throws org.luaj.vm2.LuaError if this value cannot be returned as
     * requested
     */
    public long asInt() {
        return getValue().checklong();
    }

    /**
     * Returns this value as boolean.
     *
     * @return boolean value
     * @throws org.luaj.vm2.LuaError if this value cannot be returned as
     * requested
     */
    public boolean asBoolean() {
        return getValue().checkboolean();
    }

    /**
     * Returns this value as string.
     *
     * @return string value
     * @throws org.luaj.vm2.LuaError if this value cannot be returned as
     * requested
     */
    public String asString() {
        return getValue().checkjstring();
    }

    /**
     * Returns this value as function.
     *
     * @return function value
     * @throws org.luaj.vm2.LuaError if this value cannot be returned as
     * requested
     */
    public Function asFunction() {
        return new Function(value.checkfunction());
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    public String getValueAsString() {
        if (isFunction()) {
            return getValue().typename();
        } else if (isBoolean()) {
            return "" + getValue().checkboolean();
        } else {
            return getValue().checkstring().tojstring();
        }
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
