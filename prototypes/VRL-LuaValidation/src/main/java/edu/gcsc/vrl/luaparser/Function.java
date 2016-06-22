/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.luaparser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/**
 * Function which encapsulates a raw lua function.
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class Function {

    /**
     * lua function value
     */
    private final LuaFunction value;

    /**
     * Creates a function wrapper around the given lua function.
     *
     * @param value function to wrap
     */
    Function(LuaFunction value) {
        this.value = value;

//        System.out.println("v: " + value.toString());
    }

    /**
     * Evaluates this function.
     *
     * @param args function arguments
     * @return return value
     */
    public Value eval(Value... args) {
        return eval(Arrays.asList(args));
    }

    /**
     * Evaluates this function.
     *
     * @param args function arguments
     *
     * @return return value
     */
    public Value eval(List<Value> args) {

        // convert value args to lua varargs
        Varargs largs;
        if (args.isEmpty()) {
            largs = LuaValue.NONE;
        } else {
            largs = LuaValue.varargsOf(args.stream().map(v -> v.getValue()).
                    collect(Collectors.toList()).
                    toArray(new LuaValue[args.size()]));
        }

        Varargs result;

        // invoke the lua function
        result = value.invoke(largs);

        // return the first return value, if any
        if (result.isvalue(1)) {
            return new Value(result.arg1(), "");
        } else {
            return Value.NULL;
        }
    }

}
