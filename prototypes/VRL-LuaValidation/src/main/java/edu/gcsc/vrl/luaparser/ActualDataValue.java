package edu.gcsc.vrl.luaparser;

import javafx.scene.control.TextField;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

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
    *
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
                    this.value = Integer.parseInt(String.valueOf(value));
                }
            } catch(ClassCastException | NumberFormatException n){ System.out.println("Not a Integer!" + " " + value + " " + getType());}
        } else if(getType().equals("Double")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = Double.parseDouble(String.valueOf(value));
                }
            } catch(ClassCastException | NumberFormatException c){ System.out.println("Not a Double!");}
        } else if(getType().equals("Boolean")){
            try{
                if(!value.toString().isEmpty()) {
                    this.value = Boolean.valueOf(String.valueOf(value));
                }
            } catch(ClassCastException c){ System.out.println("Not a Boolean!");}
        } else if(getType().equals("Function")){
            try {
                if(!value.toString().isEmpty()){
                    this.value = String.valueOf(value);
                }
            } catch (Exception e){System.out.println("Not a Function!");}

        } else if(getType().equals("String[]")){
            try{
                if(!value.toString().isEmpty()) {
                    List<String> temp = ConversionUtil.fromStringtoStringList(String.valueOf(value));
                    this.value = temp;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Strings!");}
        } else if(getType().equals("Integer[]")){
            try{
                if(!value.toString().isEmpty()) {
                    List<Integer> temp = ConversionUtil.fromStringToIntegerList(String.valueOf(value));
                    this.value = temp;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Integer!");}
        } else if(getType().equals("Double[]")){
            try{
                if(!value.toString().isEmpty()) {
                    List<Double> temp = ConversionUtil.fromStringToDoubleList(String.valueOf(value));
                    this.value = temp;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Doubles!");}
        } else if(getType().equals("Boolean[]")){
            try{
                if(!value.toString().isEmpty()) {
                    List<Boolean> temp = ConversionUtil.fromStringToBooleanList(String.valueOf(value));
                    this.value = temp;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Booleans!");}
        } else if(getType().equals("Function[]")){
            try{
              if(!value.toString().isEmpty()){
                  this.value = ConversionUtil.fromStringtoStringList(String.valueOf(value));
              }
            } catch(Exception e){}
        }
    }
    // Eingeführt, damit die Stringformatierung beim Laden von LUA-Files passt
    public void setValueLoad(Object value) {
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
                if(!value.toString().isEmpty()){
                    this.value = String.valueOf(value);
                }
            } catch (Exception e){System.out.println("Not a Function!");}

        } else if(getType().equals("String[]")){
            try{
                if(!value.toString().isEmpty()) {
                    List<String> temp = (List<String>)value;
                    this.value = temp;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Strings!");}
        } else if(getType().equals("Integer[]")){
            try{
                if(!value.toString().isEmpty()) {
                    List<Integer> temp = (List<Integer>)value;
                    this.value = temp;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Integer!");}
        } else if(getType().equals("Double[]")){
            try{
                if(!value.toString().isEmpty()) {
                    List<Double> temp = (List<Double>)value;
                    this.value = temp;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Doubles!");}
        } else if(getType().equals("Boolean[]")){
            try{
                if(!value.toString().isEmpty()) {
                    List<Boolean> temp = (List<Boolean>) value;
                    this.value = temp;
                }
            } catch(ClassCastException c){ System.out.println("Not a List of Booleans!");}
        } else if(getType().equals("Function[]")){
            try{
                this.value = (List<String>) value;
            } catch(Exception e){System.out.println("Not a List of Functions!");}
        }
    }

    // Falls der Wert über die GUI gesetzt wird. Färbt dann je nach Richtigkeit der Eingabe das Textfeld entsprechend
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
                if(!value.toString().isEmpty()){
                    this.value = String.valueOf(value);
                    tf.setStyle("-fx-control-inner-background: green; -fx-opacity: 0.5");
                }
            } catch (Exception e){tf.setStyle("-fx-control-inner-background: red; -fx-opacity: 0.5");}

        } else if(getType().equals("String[]")){
            try{
                if(!value.toString().isEmpty()) {
                    List<String> temp = ConversionUtil.fromStringtoStringList(String.valueOf(value));
                    this.value = temp;
                    tf.setStyle("-fx-control-inner-background: green; -fx-opacity: 0.5");
                }
            } catch(ClassCastException c){ tf.setStyle("-fx-control-inner-background: red; -fx-opacity: 0.5");}
        } else if(getType().equals("Integer[]")){
            try{
                if(!value.toString().isEmpty()) {
                    List<Integer> temp = ConversionUtil.fromStringToIntegerList(String.valueOf(value));
                    this.value = temp;
                    tf.setStyle("-fx-control-inner-background: green; -fx-opacity: 0.5");
                }
            } catch(ClassCastException c){ tf.setStyle("-fx-control-inner-background: red; -fx-opacity: 0.5");}
        } else if(getType().equals("Double[]")){
            try{
                if(!value.toString().isEmpty()) {
                    List<Double> temp = ConversionUtil.fromStringToDoubleList(String.valueOf(value));
                    this.value = temp;
                    tf.setStyle("-fx-control-inner-background: green; -fx-opacity: 0.5");
                }
            } catch(ClassCastException c){ tf.setStyle("-fx-control-inner-background: red; -fx-opacity: 0.5");}
        } else if(getType().equals("Boolean[]")){
            try{
                if(!value.toString().isEmpty()) {
                    List<Boolean> temp = ConversionUtil.fromStringToBooleanList(String.valueOf(value));
                    this.value = temp;
                    tf.setStyle("-fx-control-inner-background: green; -fx-opacity: 0.5");
                }
            } catch(ClassCastException c){ tf.setStyle("-fx-control-inner-background: red; -fx-opacity: 0.5");}
        } else if(getType().equals("Function[]")){
            try {
                if (!value.toString().isEmpty()) {
                    // Hier kann man je nach Bedarf die Liste der Function in einen String umwandeln
                    this.value = ConversionUtil.fromStringtoStringList(String.valueOf(value));
                    tf.setStyle("-fx-control-inner-background: green; -fx-opacity: 0.5");
                }
            } catch (Exception e){ tf.setStyle("-fx-control-inner-background: red; -fx-opacity: 0.5");}
        }
    }
}
