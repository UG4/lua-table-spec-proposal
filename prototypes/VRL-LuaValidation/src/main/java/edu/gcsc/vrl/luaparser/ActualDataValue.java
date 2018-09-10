package edu.gcsc.vrl.luaparser;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.scene.control.TextField;
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


    /*
    * Hier müssen noch ein paar Funktionen überarbeitet/hinzugefügt werden. Siehe 'Functions', 'Functions[]'
    * Ausserdem sowas wie der Cast von Booleans usw.
    * */
    public void setValue(Object value) {
        if (getType().equals("String")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = String.valueOf(value);
                }
            } catch(ClassCastException c){ System.out.println("Not a String!");}
        } else if(getType().equals("Integer")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = Integer.parseInt(value.toString());
                }
            } catch(ClassCastException | NumberFormatException n){ System.out.println("Not a Integer!" + " " + value + " " + getType());}
        } else if(getType().equals("Double")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = Double.parseDouble(value.toString());
                }
            } catch(ClassCastException | NumberFormatException c){ System.out.println("Not a Double!");}
        } else if(getType().equals("Boolean")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = Boolean.valueOf(value.toString());
                }
            } catch(ClassCastException c){ System.out.println("Not a Boolean!");}
        } else if(getType().equals("Function")){
            try {
                // Muss noch hinzugefügt werden!
            } catch (ClassCastException c){System.out.println("Not a Function!");}

        } else if(getType().equals("String[]")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = (List<String>) value;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Strings!");}
        } else if(getType().equals("Integer[]")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = (List<Integer>) value;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Integer!");}
        } else if(getType().equals("Double[]")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = (List<Double>) value;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Doubles!");}
        } else if(getType().equals("Boolean[]")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = (List<Boolean>) value;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Booleans!");}
        }
    }


    ///TEST

    public void setValue(Object value, TextField tf) {
        if (getType().equals("String")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = String.valueOf(value);
                    tf.setStyle("-fx-control-inner-background: green; -fx-opacity: 0.5");
                }
            } catch(ClassCastException c){ System.out.println("Not a String!");
                tf.setStyle("-fx-control-inner-background: red; -fx-opacity: 0.5");
            }
        } else if(getType().equals("Integer")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = Integer.parseInt(value.toString());
                    tf.setStyle("-fx-control-inner-background: green; -fx-opacity: 0.5");
                }
            } catch(ClassCastException | NumberFormatException n){ System.out.println("Not a Integer!" + " " + value + " " + getType());
                tf.setStyle("-fx-control-inner-background: red; -fx-opacity: 0.5");
            }
        } else if(getType().equals("Double")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = Double.parseDouble(value.toString());
                    tf.setStyle("-fx-control-inner-background: green; -fx-opacity: 0.5");
                }
            } catch(ClassCastException | NumberFormatException c){ System.out.println("Not a Double!");
                tf.setStyle("-fx-control-inner-background: red; -fx-opacity: 0.5");
            }
        } else if(getType().equals("Boolean")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = Boolean.valueOf(value.toString());
                    tf.setStyle("-fx-control-inner-background: green; -fx-opacity: 0.5");
                }
            } catch(ClassCastException c){ System.out.println("Not a Boolean!");
                tf.setStyle("-fx-control-inner-background: red; -fx-opacity: 0.5");
            }
        } else if(getType().equals("Function")){
            try {
                // Muss noch hinzugefügt werden!
            } catch (ClassCastException c){System.out.println("Not a Function!");}

        } else if(getType().equals("String[]")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = (List<String>) value;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Strings!");}
        } else if(getType().equals("Integer[]")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = (List<Integer>) value;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Integer!");}
        } else if(getType().equals("Double[]")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = (List<Double>) value;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Doubles!");}
        } else if(getType().equals("Boolean[]")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = (List<Boolean>) value;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Booleans!");}
        }
    }
}
