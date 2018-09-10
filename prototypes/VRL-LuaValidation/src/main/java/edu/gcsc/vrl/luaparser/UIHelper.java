package edu.gcsc.vrl.luaparser;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

public class UIHelper {
    // Erstellt ein Textfield für einen String
    public static TextField tfString(String text, ValueData v) {
        TextField stringField = new TextField();
        stringField.setText(text);
        stringField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (v.getActData() != null) {
                        v.getActData().setValue(newValue,stringField);
                    } else {
                        ActualDataValue adv = new ActualDataValue();
                        adv.setType(v.getType().get());
                        adv.setValue(newValue, stringField);
                        v.setActData(adv);
                    }
                }
        );
        return stringField;
    }

    public static ComboBox cbNumber(ValueData v) {

        ObservableList<Double> vals = FXCollections.observableArrayList();
        for (Double d : v.getValues()) {
            vals.add(d);
        }
        ComboBox doubleBox = new ComboBox(vals);

        if(v.getDefaultVal() != null) {
            doubleBox.getSelectionModel().select(v.getDefaultVal());
        }

        doubleBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                System.out.println(newValue);
                if (v.getActData() != null) {
                    v.getActData().setValue(newValue);
                } else {
                    ActualDataValue adv = new ActualDataValue();
                    adv.setType(v.getType().get());
                    System.out.println(v.getType().get());
                    adv.setValue(newValue);
                    v.setActData(adv);
                }
            }
        });
        return doubleBox;
    }

    public static void logging(String msg, TextArea ta){
        ta.appendText(msg+"\n");
    }
}
