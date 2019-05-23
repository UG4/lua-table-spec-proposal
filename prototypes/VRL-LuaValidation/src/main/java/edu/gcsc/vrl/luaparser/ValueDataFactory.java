package edu.gcsc.vrl.luaparser;

@FunctionalInterface
public interface ValueDataFactory {

    ValueData newInstance(String name);

}
