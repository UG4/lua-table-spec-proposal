package edu.gcsc.vrl.luaparser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

import javax.xml.soap.Text;

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
                        TextField stringField = UIHelper.tfString(item.getDefaultVal());
                        setGraphic(stringField);
                        setStyle("");
                        break;
                    case "Double":
                        if(item.getDefaultVal() != null){
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
                        if(item.getDefaultVal() != null){
                            TextField integerField = UIHelper.tfInteger(item.getDefaultVal());
                            setGraphic(integerField);
                            setStyle("");
                        } else {
                            TextField integerField = UIHelper.tfInteger("");
                            setGraphic(integerField);
                            setStyle("");
                        }
                        break;
                }
            } else if(item.getStyle().equals("selection")){
                switch(item.getType()){
                    case "Double":
                        if(item.getDefaultVal() != null){
                            ComboBox doubleBox = UIHelper.cbDouble(item.getValues(),item.getDefaultVal());
                            setGraphic(doubleBox);
                            setStyle("");
                        }
                }

            }
        }

    }
}
