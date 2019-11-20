package edu.gcsc.vrl.luaparser;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Window;
/**
 * This class renders the second row of the TreeTableView.
 * */
public class MyValCell extends TreeTableCell<ValueData, ValueData> {
    @Override
    protected void updateItem(ValueData item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
            setStyle(null);
            setGraphic(null);
        } else {
            if (!item.isDisabled() && item.isVisible() && item.isValidationValid() && item.isValInRange()) {
                if (item.getOptions().size() < 1) {
                    if (item.getStyle().equals("default")) {
                        if(!item.isTable()) {
                            TextField stringField;
                            if (item.getActData() != null) {
                                if (item.getActData().getValue() != null) {
                                    try {
                                        if (item.getActData().getType().equals("Integer")) {
                                            stringField = UIUtil.tfString(Integer.toString(Integer.parseInt(item.getActData().getValue().toString())), item);
                                            UIUtil.doTooltip(item, stringField);
                                            setGraphic(stringField);
                                            setStyle(null);
                                        } else if (item.getActData().getType().equals("Double")) {
                                            stringField = UIUtil.tfString(Double.toString(Double.parseDouble(item.getActData().getValue().toString())), item);
                                            UIUtil.doTooltip(item, stringField);
                                            setGraphic(stringField);
                                            setStyle(null);
                                        } else if (item.getActData().getType().equals("String")) {
                                            stringField = UIUtil.tfString(String.valueOf(item.getActData().getValue()), item);
                                            UIUtil.doTooltip(item, stringField);
                                            setGraphic(stringField);
                                            setStyle(null);
                                        } else if (item.getActData().getType().equals("Function")) {
                                            stringField = UIUtil.tfString(String.valueOf(item.getActData().getValue()), item);
                                            UIUtil.doTooltip(item, stringField);
                                            setGraphic(stringField);
                                            setStyle(null);
                                        } else if (item.getActData().getType().equals("Boolean")) {
                                            stringField = UIUtil.tfString(String.valueOf(item.getActData().getValue()), item);
                                            UIUtil.doTooltip(item, stringField);
                                            setGraphic(stringField);
                                            setStyle(null);
                                        } else if (item.getActData().getType().equals("Double[]") || item.getActData().getType().equals("Integer[]") ||
                                                item.getActData().getType().equals("String[]") || item.getActData().getType().equals("Boolean[]") ||
                                                item.getActData().getType().equals("Function[]")) {
                                            stringField = UIUtil.tfString(item.getActData().getValue(), item);
                                            UIUtil.doTooltip(item, stringField);
                                            setGraphic(stringField);
                                            setStyle(null);
                                        } else {
                                            System.out.println("UI Case: 12");
                                            stringField = UIUtil.tfString("", item);
                                            UIUtil.doTooltip(item, stringField);
                                            setGraphic(stringField);
                                            setStyle(null);
                                        }
                                    } catch (ClassCastException c) {
                                        System.out.println("UI Case: 11");
                                        stringField = UIUtil.tfString("", item);
                                        System.out.println(item.getActData().getValue().toString());
                                    }
                                } else {
                                    System.out.println("UI Case: 1");
                                    setStyle(null);
                                    setGraphic(null);
                                }
                            } else {
                                System.out.println("UI Case: 2");
                                setStyle(null);
                                setGraphic(null);
                            }
                        } else if (item.isTable()){
                            HBox tableCont = UIUtil.doTable(item);
                            setGraphic(tableCont);
                            setStyle(null);
                        }

                    } else if (item.getStyle().equals("selection")) {
                        switch (item.getType()) {
                            case "Double":
                                ComboBox doubleBox1 = UIUtil.cbNumber(item);
                                UIUtil.doTooltip(item, doubleBox1);
                                setGraphic(doubleBox1);
                                setStyle(null);
                                break;

                            case "Integer":
                                ComboBox doubleBox2 = UIUtil.cbNumber(item);
                                UIUtil.doTooltip(item, doubleBox2);
                                setGraphic(doubleBox2);
                                setStyle(null);
                                break;
                        }
                    } else if (item.getStyle().equals("load-file-dialog")) {
                        switch (item.getType()) {
                            case "String":
                                Window act = MyValCell.super.getTreeTableView().getScene().getWindow();
                                HBox master = UIUtil.doLoadFile(act, item);
                                setGraphic(master);
                                setStyle(null);
                                break;

                        }

                    } else if (item.getStyle().equals("save-file-dialog")) {
                        switch (item.getType()) {
                            case "String":
                                Window act = MyValCell.super.getTreeTableView().getScene().getWindow();
                                HBox master = UIUtil.doSaveFile(act, item);
                                setGraphic(master);
                                setStyle(null);
                                break;
                        }
                    } else {
                        TextField stringField;
                        if (item.getActData() != null) {
                            if (item.getActData().getValue() != null) {
                                try {
                                    if (item.getActData().getType().equals("Integer")) {
                                        stringField = UIUtil.tfString(Integer.toString(Integer.parseInt(item.getActData().getValue().toString())), item);
                                        UIUtil.doTooltip(item, stringField);
                                        setGraphic(stringField);
                                        setStyle(null);
                                    } else if (item.getActData().getType().equals("Double")) {
                                        stringField = UIUtil.tfString(Double.toString(Double.parseDouble(item.getActData().getValue().toString())), item);
                                        UIUtil.doTooltip(item, stringField);
                                        setGraphic(stringField);
                                        setStyle(null);
                                    } else if (item.getActData().getType().equals("String")) {
                                        stringField = UIUtil.tfString(String.valueOf(item.getActData().getValue()), item);
                                        UIUtil.doTooltip(item, stringField);
                                        setGraphic(stringField);
                                        setStyle(null);
                                    } else if (item.getActData().getType().equals("Function")) {
                                        stringField = UIUtil.tfString(String.valueOf(item.getActData().getValue()), item);
                                        UIUtil.doTooltip(item, stringField);
                                        setGraphic(stringField);
                                        setStyle(null);
                                    } else if (item.getActData().getType().equals("Boolean")) {
                                        stringField = UIUtil.tfString(String.valueOf(item.getActData().getValue()), item);
                                        UIUtil.doTooltip(item, stringField);
                                        setGraphic(stringField);
                                        setStyle(null);
                                    } else if (item.getActData().getType().equals("Double[]") || item.getActData().getType().equals("Integer[]") ||
                                            item.getActData().getType().equals("String[]") || item.getActData().getType().equals("Boolean[]") ||
                                            item.getActData().getType().equals("Function[]")) {
                                        stringField = UIUtil.tfString(item.getActData().getValue(), item);
                                        UIUtil.doTooltip(item, stringField);
                                        setGraphic(stringField);
                                        setStyle(null);
                                    } else {
                                        stringField = UIUtil.tfString("", item);
                                        UIUtil.doTooltip(item, stringField);
                                        setGraphic(stringField);
                                        setStyle(null);
                                    }

                                } catch (ClassCastException c) {
                                    stringField = UIUtil.tfString("", item);
                                    System.out.println(item.getActData().getValue().toString());
                                }
                            } else {
                                setStyle(null);
                                setGraphic(null);
                            }
                        } else {
                            setStyle(null);
                            setGraphic(null);
                        }
                    }
                } else {
                    setText(null);
                    setStyle(null);
                    setGraphic(null);
                }
            } else if (item.isDisabled() || !item.isVisible()) {
                if (item.getActData() != null && item.getActData().getValue() != null) {
                    TextField tf = UIUtil.tfString(item.getActData().getValue().toString(), item);
                    UIUtil.doTooltip(item, tf);
                    tf.setEditable(false);
                    setGraphic(tf);
                    setStyle("-fx-control-inner-background: orange;-fx-opacity: 0.5");
                } else {
                    setStyle(null);
                    setGraphic(null);
                }
            } else if(!item.isValidationValid() || !item.isValInRange()){
                /**
                 * If param is not valid or its value is not in the given value range,
                 * the cell renders default style with the value(s)
                 * */
                if (item.getActData() != null) {
                    if (item.getActData().getValue() != null) {
                        try {
                            if (item.getActData().getType().equals("Integer")) {
                                TextField stringField;
                                stringField = UIUtil.tfString(Integer.toString(Integer.parseInt(item.getActData().getValue().toString())), item);
                                UIUtil.doTooltip(item, stringField);
                                stringField.setEditable(true);
                                setGraphic(stringField);
                                setStyle("-fx-control-inner-background: red;-fx-opacity: 0.75");
                            } else if (item.getActData().getType().equals("Double")) {
                                TextField stringField;
                                stringField = UIUtil.tfString(Double.toString(Double.parseDouble(item.getActData().getValue().toString())), item);
                                UIUtil.doTooltip(item, stringField);
                                stringField.setEditable(true);
                                setGraphic(stringField);
                                setStyle("-fx-control-inner-background: red;-fx-opacity: 0.75");
                            } else if (item.getActData().getType().equals("String")) {
                                if(item.getStyle().equals("load-file-dialog")){
                                    Window act = MyValCell.super.getTreeTableView().getScene().getWindow();
                                    HBox master = UIUtil.doLoadFile(act, item);
                                    setGraphic(master);
                                    setStyle("-fx-control-inner-background: red;-fx-opacity: 0.75");

                                } else if(item.getStyle().equals("save-file-dialog")){
                                    Window act = MyValCell.super.getTreeTableView().getScene().getWindow();
                                    HBox master = UIUtil.doSaveFile(act, item);
                                    setGraphic(master);
                                    setStyle("-fx-control-inner-background: red;-fx-opacity: 0.75");
                                } else {
                                    TextField stringField;
                                    stringField = UIUtil.tfString(String.valueOf(item.getActData().getValue()), item);
                                    UIUtil.doTooltip(item, stringField);
                                    stringField.setEditable(true);
                                    setGraphic(stringField);
                                    setStyle("-fx-control-inner-background: red;-fx-opacity: 0.75");
                                }
                            } else if (item.getActData().getType().equals("Function")) {
                                TextField stringField;
                                stringField = UIUtil.tfString(String.valueOf(item.getActData().getValue()), item);
                                UIUtil.doTooltip(item, stringField);
                                stringField.setEditable(true);
                                setGraphic(stringField);
                                setStyle("-fx-control-inner-background: red;-fx-opacity: 0.75");
                            } else if (item.getActData().getType().equals("Boolean")) {
                                TextField stringField;
                                stringField = UIUtil.tfString(String.valueOf(item.getActData().getValue()), item);
                            } else if (item.getActData().getType().equals("Double[]") || item.getActData().getType().equals("Integer[]") ||
                                    item.getActData().getType().equals("String[]") || item.getActData().getType().equals("Boolean[]") ||
                                    item.getActData().getType().equals("Function[]")) {
                                TextField stringField;
                                stringField = UIUtil.tfString(item.getActData().getValue(), item);
                                UIUtil.doTooltip(item, stringField);
                                stringField.setEditable(true);
                                setGraphic(stringField);
                                setStyle("-fx-control-inner-background: red;-fx-opacity: 0.75");
                            } else {
                                TextField stringField;
                                stringField = UIUtil.tfString("", item);
                            }

                        } catch (ClassCastException c) {
                            TextField stringField;
                            stringField = UIUtil.tfString("", item);
                        }
                    } else {
                        setStyle(null);
                        setGraphic(null);
                    }
                } else {
                    setStyle(null);
                    setGraphic(null);
                }
            } else {
                setStyle(null);
                setGraphic(null);
            }
        }
    }
}
