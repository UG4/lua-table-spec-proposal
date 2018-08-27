package edu.gcsc.vrl.luaparser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import javax.xml.soap.Text;

public class MyValCell extends TreeTableCell<ValueData, ValueData> {
    /*
    * Hier werden spezifische Zellen gerendert - je nach type des Wertes,
    * der dargestellt werden soll.
    * */
    @Override
    protected void updateItem(ValueData item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
            setStyle(null);
            setGraphic(null);
        } else {
            if (item.getSubParams().size() < 1) {
                if (item.getStyle().equals("default")) {
                    switch (item.getType().get()) {
                        case "String":
                            TextField stringField = UIHelper.tfString(item.getDefaultVal());
                            setGraphic(stringField);
                            setStyle("");
                            break;
                        case "Double":
                            if (item.getDefaultVal() != null) {
                                TextField doubleField = UIHelper.tfDouble(item.getDefaultVal());
                                setGraphic(doubleField);
                                setStyle("");
                            } else {
                                TextField doubleField = UIHelper.tfDouble("");
                                setGraphic(doubleField);
                                setStyle("");
                            }
                            break;
                        case "Integer":
                            if (item.getDefaultVal() != null) {
                                TextField integerField = UIHelper.tfInteger(item.getDefaultVal());
                                setGraphic(integerField);
                                setStyle("");
                            } else {
                                TextField integerField = UIHelper.tfInteger(" ");
                                setGraphic(integerField);
                                setStyle("");
                            }
                            break;
                    }
                } else if (item.getStyle().equals("selection")) {
                    switch (item.getType().get()) {
                        case "Double":
                            if (item.getDefaultVal() != null) {
                                ComboBox doubleBox = UIHelper.cbNumber(item.getValues(), item.getDefaultVal());
                                setGraphic(doubleBox);
                                setStyle("");
                            } else {
                                ComboBox doubleBox = UIHelper.cbNumber(item.getValues());
                                setGraphic(doubleBox);
                                setStyle("");
                            }
                        case "Integer":
                            if (item.getDefaultVal() != null) {
                                ComboBox doubleBox = UIHelper.cbNumber(item.getValues(), item.getDefaultVal());
                                setGraphic(doubleBox);
                                setStyle("");
                            } else {
                                ComboBox doubleBox = UIHelper.cbNumber(item.getValues());
                                setGraphic(doubleBox);
                                setStyle("");
                            }
                    }

                } else {
                    TextField stringField = UIHelper.tfString(item.getDefaultVal());
                    setGraphic(stringField);
                    setStyle("");
                }
            } else {
                setText(null);
                setStyle(null);
                setGraphic(null);
            }
        }

    }
}
