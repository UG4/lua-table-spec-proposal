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
            if (!item.isDisabled()) {
                if (item.getOptions().size() < 1) {
                    if (item.getStyle().equals("default")) {
                        switch (item.getType().get()) {
                            case "String":
                                TextField stringField;
                                if(item.getActData() != null) {
                                    stringField = UIHelper.tfString(String.valueOf(item.getActData().getValue()), item);
                                } else {
                                    stringField = UIHelper.tfString("", item);
                                }
                                setGraphic(stringField);
                                setStyle("");
                                break;
                            case "Double":
                                if (item.getActData() != null) {
                                    TextField doubleField = UIHelper.tfString(item.getActData().getValue().toString(), item);
                                    setGraphic(doubleField);
                                    setStyle("");
                                } else {
                                    TextField doubleField = UIHelper.tfString("", item);
                                    setGraphic(doubleField);
                                    setStyle("");
                                }
                                break;
                            case "Integer":
                                if (item.getActData() != null) {
                                    TextField integerField = UIHelper.tfString(item.getActData().getValue().toString(), item);
                                    setGraphic(integerField);
                                    setStyle("");
                                } else {
                                    TextField integerField = UIHelper.tfString(" ", item);
                                    setGraphic(integerField);
                                    setStyle("");
                                }
                                break;
                        }
                    } else if (item.getStyle().equals("selection")) {
                        switch (item.getType().get()) {
                            case "Double":
                                ComboBox doubleBox1 = UIHelper.cbNumber(item);
                                setGraphic(doubleBox1);
                                setStyle("");

                            case "Integer":
                                /*
                                * Hier muss noch eine ComboBox für Integer eingebaut werden!
                                * */
                                ComboBox doubleBox2 = UIHelper.cbNumber(item);
                                setGraphic(doubleBox2);
                                setStyle("");
                        }


                    } else {
                        TextField stringField;
                        if(item.getActData() != null) {
                            if(item.getActData().getValue() != null) {
                                try {
                                    if(item.getActData().getType().equals("Integer")) {
                                        stringField = UIHelper.tfString(Integer.toString((Integer) item.getActData().getValue()), item);
                                    } else if(item.getActData().getType().equals("Double")) {
                                        stringField = UIHelper.tfString(Double.toString((Double) item.getActData().getValue()), item);
                                    } else if(item.getActData().getType().equals("String")){
                                        stringField = UIHelper.tfString(String.valueOf(item.getActData().getValue()), item);
                                    } else {
                                        stringField = UIHelper.tfString("", item);
                                    }

                                } catch (ClassCastException c) {
                                    stringField = UIHelper.tfString("", item);
                                    System.out.println(item.getActData().getValue().toString());
                                }
                            } else {stringField = UIHelper.tfString("", item);}
                        } else {
                            stringField = UIHelper.tfString("", item);
                        }
                        setGraphic(stringField);
                        setStyle("");
                    }

                } else {
                    setText(null);
                    setStyle(null);
                    setGraphic(null);
                }
            } else if(item.isDisabled()){
                setText("Disabled");
            }
        }

    }
}
