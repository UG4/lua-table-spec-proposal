package edu.gcsc.vrl.luaparser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

public class UIHelper {
    // Erstellt ein Textfield für einen String
    public static TextField tfString(String text){
        TextField stringField = new TextField();
        stringField.setText(text);
        return stringField;
    }

    public static TextField tfDouble(String val){
        TextField doubleField = new TextField();
        doubleField.setText(val);
        return doubleField;
    }

    public static TextField tfInteger(String val){
        TextField integerField = new TextField();
        integerField.setText(val);
        return integerField;
    }

    public static ComboBox cbDouble(double[] values, String defaultVal){
        ObservableList<Double> vals = FXCollections.observableArrayList();
        for(Double d : values){
            vals.add(d);
        }
        ComboBox doubleBox = new ComboBox(vals);
        doubleBox.getSelectionModel().select(defaultVal);
        return doubleBox;
    }
}
