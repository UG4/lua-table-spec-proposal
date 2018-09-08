package edu.gcsc.vrl.luaparser;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeTableCell;
import org.apache.commons.lang.math.NumberUtils;

public class FirstColumnCell extends TreeTableCell<ValueData, ValueData> {

    @Override
    public void updateItem(ValueData item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setGraphic(null);
            setText(null);
            setStyle(null);
        } else {
            if (item.getParentNode() != null) {

                if (!item.isDisabled()) {
                    setDisable(false);
                    if (!item.getParentNode().isNestedGroup()) {
                        CheckBox option = new CheckBox();
                        option.setSelected(item.isSelected());
                        option.selectedProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                                item.setSelectedNew(newValue);

                            }
                        });
                        setText(item.getValName().get());
                        setGraphic(option);
                    } else {
                        //System.out.println("TESTING");
                        //System.out.println(item.getValName().get());
                        //if(item.isSelected()) {
                        //System.out.println("is selected");
                        //}
                        //System.out.println("__________");
                        //CheckBox l = new CheckBox();
                        //item.setSelectedNew(true);
                        //item.setSelectedNew(true);
                        item.setSelection(true);
                        setText(item.getValName().get());
                    }
                } else {
                    setDisable(true);
                    CheckBox cb = new CheckBox();
                    cb.setDisable(true);
                    setText(item.getValName().get());
                    setGraphic(cb);
                }

            } else {
                setText(item.getValName().get());
                setStyle("");
            }
        }
    }
}
