/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.luaparser;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;

/**
 * A naive lua interpreter.
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
class LuaInterpreter {

    private Globals globals;
    private LuaValue luaProgram;

    /**
     * Loads/parses the specified lua program.
     *
     * @param code the source code to load/parse
     */
    public void load(String code) {
        luaProgram = getGlobals().load(code);
    }

    /**
     * Runs previously loaded lua programs.
     */
    public void run() {

        if (luaProgram == null) {
            throw new RuntimeException(
                    "No lua program defined."
                    + " Load a program before running it.");
        }
        luaProgram.call();
    }

    /**
     * Sets the specified string value.
     *
     * @param name name of the value to set
     * @param value value
     */
    public void set(String name, String value) {
        getGlobals().set(name, value);
    }

    /**
     * Sets the specified integer value.
     *
     * @param name name of the value to set
     * @param value value
     */
    public void set(String name, int value) {
        getGlobals().set(name, value);
    }

    /**
     * Sets the specified double value.
     *
     * @param name name of the value to set
     * @param value value
     */
    public void set(String name, double value) {
        getGlobals().set(name, value);
    }

    /**
     * Returns the specified string value.
     *
     * @param name name of the value to return
     * @return the specified string value
     * @throws LuaError if the requested value does not exist or if it cannot be
     * casted to string
     */
    public String getString(String name) {
        return getGlobals().get(name).checkstring().tojstring();
    }

    /**
     * Returns the specified integer value.
     *
     * @param name name of the value to return
     * @return the specified integer value
     * @throws LuaError if the requested value does not exist or if it cannot be
     * casted to int
     */
    public int getInt(String name) {
        return getGlobals().get(name).checkint();
    }

    /**
     * Returns the specified double value.
     *
     * @param name name of the value to return
     * @return the specified double value
     * @throws LuaError if the requested value does not exist or if it cannot be
     * casted to double
     */
    public double getDouble(String name) {
        return getGlobals().get(name).checkdouble();
    }

    /**
     * Returns the lua environment used by this interpreter.
     *
     * @return the lua environment used by this parser
     */
    public Globals getGlobals() {

        if (globals == null) {
            globals = new Globals();
// TODO only add necessary dependencies, we should specify which dependencies we 
//      need for the validation format

//            globals = new Globals();
//            globals.load(new JseBaseLib());
//            globals.load(new PackageLib());
//            globals.load(new Bit32Lib());
//            globals.load(new TableLib());
//            globals.load(new StringLib());
//            globals.load(new CoroutineLib());
//            globals.load(new JseMathLib());
//            globals.load(new JseIoLib());
//            globals.load(new JseOsLib());
//            globals.load(new LuajavaLib());
            LoadState.install(globals);
            LuaC.install(globals);
            return globals;
        }

        return globals;
    }

}
