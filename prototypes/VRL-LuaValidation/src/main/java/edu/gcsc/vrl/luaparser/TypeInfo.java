package edu.gcsc.vrl.luaparser;

public class TypeInfo {
    private String type_name;

    public TypeInfo(String name){
        this.type_name = name;
    }

    public String get_name(){
        return this.type_name;
    }
}
