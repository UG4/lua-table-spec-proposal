package edu.gcsc.vrl.luaparser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

public class UIHelper {
    // Erstellt ein Textfield für einen String
    public static TextField tfString(String text, ValueData v){
        TextField stringField = new TextField();
        stringField.setText(text);
        stringField.textProperty().addListener((observable, oldValue, newValue) -> {
                    System.out.println(v.getValName().get());
                    System.out.println("TEST: " + newValue);
                    if (v.getActData() != null) {
                        v.getActData().setValue(newValue);
                    } else {
                        ActualDataValue adv = new ActualDataValue();
                        adv.setType(v.getType().get());
                        adv.setValue(newValue);
                        v.setActData(adv);
                    }
                }
            );
        return stringField;
    }

    public static ComboBox cbNumber(double[] values, String defaultVal, ValueData v){
        ObservableList<Double> vals = FXCollections.observableArrayList();
        for(Double d : values){
            vals.add(d);
        }
        ComboBox doubleBox = new ComboBox(vals);
        doubleBox.getSelectionModel().select(defaultVal);
        return doubleBox;
    }

    public static ComboBox cbNumber(double[] values, ValueData v){
        ObservableList<Double> vals = FXCollections.observableArrayList();
        for(Double d : values){
            vals.add(d);
        }
        ComboBox doubleBox = new ComboBox(vals);
        return doubleBox;
    }
}
