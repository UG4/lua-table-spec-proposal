package edu.gcsc.vrl.luaparser;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class UIUtil {
    // Erstellt ein Textfield für einen String
    public static TextField tfString(String text, ValueData v) {
        TextField stringField = new TextField();
        stringField.setText(text);
        stringField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (v.getActData() != null) {
                        v.getActData().setValue(newValue, stringField);
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

    public static TextField tfString(Object o, ValueData v) {
        TextField stringField = new TextField();
        if(v.getActData().getType().equals("Double[]")){
            List<Double> temp = (List<Double>) o;
            String s = ConversionUtil.fromDoubleListToString(temp);
            stringField.setText(s);
        } else if(v.getActData().getType().equals("Integer[]")){
            List<Integer> temp = (List<Integer>) o;
            String s = ConversionUtil.fromIntegerListToString(temp);
            stringField.setText(s);
        } else if(v.getActData().getType().equals("Boolean[]")){
            List<Boolean> temp = (List<Boolean>) o;
            String s = ConversionUtil.fromBooleanListToString(temp);
            stringField.setText(s);
        } else if(v.getActData().getType().equals("String[]")){
            List<String> temp = (List<String>) o;
            String s = ConversionUtil.fromStringListToString(temp);
            stringField.setText(s);
        }
        stringField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (v.getActData() != null) {
                        v.getActData().setValue(newValue, stringField);
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

        if (v.getDefaultVal() != null) {
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

    public static void doTooltip(ValueData item, TextField t) {
        Tooltip tip = new Tooltip();
        if (!item.getTooltip().isEmpty()) {
            if (item.getActData() != null && item.getActData().getType() != null) {
                tip.setText("Type: " + item.getActData().getType() + "\n" + item.getTooltip());
            } else {
                tip.setText(item.getTooltip());
            }
        } else {
            if (item.getActData() != null && item.getActData().getType() != null) {
                tip.setText("Type: " + item.getActData().getType());
            } else {
                tip.setText("");
            }
        }
        t.setTooltip(tip);
    }

    public static void doTooltip(ValueData item, ComboBox t) {
        Tooltip tip = new Tooltip();
        if (!item.getTooltip().isEmpty()) {
            if (item.getActData() != null && item.getActData().getType() != null) {
                tip.setText("Type: " + item.getActData().getType() + "\n" + item.getTooltip());
            } else {
                tip.setText(item.getTooltip());
            }
        } else {
            if (item.getActData() != null && item.getActData().getType() != null) {
                tip.setText("Type: " + item.getActData().getType());
            } else {
                tip.setText("");
            }
        }
        t.setTooltip(tip);
    }
    public static HBox doLoadFile(Window actWindow){
        HBox master = new HBox();
        master.setSpacing(2);
        HBox content = new HBox();

        TextField tf = new TextField();
        Button bt = new Button();
        content.getChildren().add(tf);
        content.getChildren().add(bt);
        master.getChildren().add(content);

        bt.setText("...");
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String path = "";
                final FileChooser fc = new FileChooser();
                fc.setTitle("Select a .lua-File to open");
                FileChooser.ExtensionFilter extLua = new FileChooser.ExtensionFilter("Lua Files (*.lua)", "*.lua");
                fc.getExtensionFilters().add(extLua);
                final File selecDir = fc.showOpenDialog(actWindow);
                if (selecDir != null) {
                    path = selecDir.getAbsolutePath();
                }
                tf.setText(path);
            }
        });
        return master;
    }
    public static HBox doSaveFile(Window actWindow){
        HBox master = new HBox();
        master.setSpacing(2);
        HBox content = new HBox();

        TextField tf = new TextField();
        Button bt = new Button();
        content.getChildren().add(tf);
        content.getChildren().add(bt);
        master.getChildren().add(content);

        bt.setText("...");
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String path = "";
                final FileChooser fc = new FileChooser();
                fc.setTitle("Select a .lua-File to save");
                FileChooser.ExtensionFilter extLua = new FileChooser.ExtensionFilter("Lua Files (*.lua)", "*.lua");
                fc.getExtensionFilters().add(extLua);
                final File selecDir = fc.showSaveDialog(actWindow);
                if (selecDir != null) {
                    path = selecDir.getAbsolutePath();
                }
                tf.setText(path);
            }
        });
        return master;
    }

    public static void logging(String msg, TextArea ta) {
        ta.appendText(msg + "\n");
    }
}
