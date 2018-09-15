package edu.gcsc.vrl.luaparser;

import javafx.scene.control.*;

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
                                    stringField = UIUtil.tfString(String.valueOf(item.getActData().getValue()), item);
                                } else {
                                    stringField = UIUtil.tfString("", item);
                                }
                                Tooltip tip = UIUtil.doTooltip(item);
                                stringField.setTooltip(tip);
                                setGraphic(stringField);
                                //setStyle("");
                                break;
                            case "Double":
                                TextField doubleField;
                                if (item.getActData() != null) {
                                    doubleField = UIUtil.tfString(item.getActData().getValue().toString(), item);
                                } else {
                                    doubleField = UIUtil.tfString("", item);
                                }
                                Tooltip tip2 = UIUtil.doTooltip(item);
                                doubleField.setTooltip(tip2);
                                setGraphic(doubleField);
                                //setStyle("");
                                break;
                            case "Integer":
                                TextField integerField;
                                if (item.getActData() != null) {
                                    integerField = UIUtil.tfString(item.getActData().getValue().toString(), item);
                                } else {
                                    integerField = UIUtil.tfString(" ", item);
                                }
                                Tooltip tip3 = UIUtil.doTooltip(item);
                                integerField.setTooltip(tip3);
                                setGraphic(integerField);
                                //setStyle("");
                                break;
                        }
                    } else if (item.getStyle().equals("selection")) {
                        switch (item.getType().get()) {
                            case "Double":
                                ComboBox doubleBox1 = UIUtil.cbNumber(item);
                                if(!item.getTooltip().isEmpty()) {
                                    Tooltip tip = new Tooltip();
                                    tip.setText(item.getTooltip());
                                    doubleBox1.setTooltip(tip);
                                }
                                setGraphic(doubleBox1);
                                setStyle("");

                            case "Integer":
                                /*
                                * Hier muss noch eine ComboBox für Integer eingebaut werden!
                                * */
                                ComboBox doubleBox2 = UIUtil.cbNumber(item);
                                if(!item.getTooltip().isEmpty()) {
                                    Tooltip tip = new Tooltip();
                                    tip.setText(item.getTooltip());
                                    doubleBox2.setTooltip(tip);
                                }
                                setGraphic(doubleBox2);
                                setStyle("");
                        }


                    } else {
                        TextField stringField;
                        if(item.getActData() != null) {
                            if(item.getActData().getValue() != null) {
                                try {
                                    if(item.getActData().getType().equals("Integer")) {
                                        stringField = UIUtil.tfString(Integer.toString(Integer.parseInt(item.getActData().getValue().toString())), item);
                                    } else if(item.getActData().getType().equals("Double")) {
                                        stringField = UIUtil.tfString(Double.toString(Double.parseDouble(item.getActData().getValue().toString())), item);
                                    } else if(item.getActData().getType().equals("String")){
                                        stringField = UIUtil.tfString(String.valueOf(item.getActData().getValue()), item);
                                    } else {
                                        stringField = UIUtil.tfString("", item);
                                    }

                                } catch (ClassCastException c) {
                                    stringField = UIUtil.tfString("", item);
                                    System.out.println(item.getActData().getValue().toString());
                                }
                            } else {stringField = UIUtil.tfString("", item);}
                        } else {
                            stringField = UIUtil.tfString("", item);
                        }
                        Tooltip tip = UIUtil.doTooltip(item);
                        stringField.setTooltip(tip);
                        setGraphic(stringField);
                        //setStyle("");
                    }

                } else {
                    setText(null);
                    setStyle(null);
                    setGraphic(null);
                }
            } else if(item.isDisabled()){
                if(item.getActData() != null && item.getActData().getValue() != null) {
                    TextField tf = UIUtil.tfString(item.getActData().getValue().toString(),item);
                    Tooltip tip = UIUtil.doTooltip(item);
                    tf.setTooltip(tip);
                    tf.setEditable(false);
                    tf.setDisable(true);
                    setGraphic(tf);
                } else {
                    //setText("Param: "+item.getValName().get());
                }
            }
        }

    }
}
