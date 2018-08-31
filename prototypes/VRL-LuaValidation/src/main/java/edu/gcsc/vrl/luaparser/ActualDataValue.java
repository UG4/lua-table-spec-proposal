package edu.gcsc.vrl.luaparser;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.commons.lang.math.NumberUtils;

import java.util.List;

public class ActualDataValue {
    private String type;
    private Object value = null;

    public ActualDataValue() {
    }

    // GETTER
    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    // SETTER

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(Object value) {
        if (getType().equals("String")){
            try{
                System.out.println("HALLO");
                this.value = String.valueOf(value);
            } catch(ClassCastException c){ System.out.println("Not a String!");}
        } else if(getType().equals("Integer")){
            try{
                this.value = Integer.parseInt((String)value);
            } catch(ClassCastException | NumberFormatException n){ System.out.println("Not a Integer!" + " " + value + " " + getType());}
        } else if(getType().equals("Double")){
            try{
                this.value = Double.parseDouble((String)value);
            } catch(ClassCastException c){ System.out.println("Not a Double!");}
        } else if(getType().equals("Boolean")){
            try{
                this.value = Boolean.valueOf((String)value);
            } catch(ClassCastException c){ System.out.println("Not a Boolean!");}
        } else if(getType().equals("String[]")){
            try{
                this.value = (List<String>)value;
            } catch(ClassCastException c){ System.out.println("Not a List of Strings!");}
        } else if(getType().equals("Integer[]")){
            try{
                this.value = (List<Integer>)value;
            } catch(ClassCastException c){ System.out.println("Not a List of Integer!");}
        } else if(getType().equals("Double[]")){
            try{
                this.value = (List<Double>)value;
            } catch(ClassCastException c){ System.out.println("Not a List of Doubles!");}
        } else if(getType().equals("String[]")){
            try{
                this.value = (List<Boolean>)value;
            } catch(ClassCastException c){ System.out.println("Not a List of Strings!");}
        }
    }
}
