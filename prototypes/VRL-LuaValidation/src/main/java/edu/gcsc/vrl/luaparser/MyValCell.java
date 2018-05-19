package edu.gcsc.vrl.luaparser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

public class MyValCell extends TableCell<ValueData, ValProperty> {
    /*
    * Hier werden spezifische Zellen gerendert - je nach type des Wertes,
    * der dargestellt werden soll.
    * */
    @Override
    protected void updateItem(ValProperty item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
            setStyle("");
        } else {
            if(item.getStyle().equals("default")) {
                switch (item.getType()) {
                    case "String":
                        TextField stringField = new TextField();
                        stringField.setText(item.getType());
                        setStyle("");
                        break;
                    case "Double":
                        if(item.getDefaultVal() != null){
                            TextField doubleField = new TextField();
                            doubleField.setText(item.getDefaultVal());
                            setGraphic(doubleField);
                            setStyle("");
                        } else {
                            TextField doubleField = new TextField();
                            doubleField.setText("");
                            setGraphic(doubleField);
                            setStyle("");
                        }
                        break;
                    case "Integer":
                        if(item.getDefaultVal() != null){
                            TextField integerField = new TextField();
                            integerField.setText(item.getDefaultVal());
                            setGraphic(integerField);
                            setStyle("");
                        } else {
                            TextField integerField = new TextField();
                            integerField.setText("");
                            setGraphic(integerField);
                            setStyle("");
                        }
                        break;
                }
            } else if(item.getStyle().equals("selection")){
                switch(item.getType()){
                    case "Double":
                        if(item.getDefaultVal() != null){
                            ObservableList<Double> vals = FXCollections.observableArrayList();
                            for(Double d : item.getValues()){
                                vals.add(d);
                            }
                            ComboBox doubleBox = new ComboBox(vals);
                            doubleBox.getSelectionModel().select(item.getDefaultVal());
                            setGraphic(doubleBox);
                            setStyle("");
                        }
                }

            }
        }

    }
}
