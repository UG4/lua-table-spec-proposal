package edu.gcsc.vrl.luaparser;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeTableCell;

/**
* This class renders the first row of the TreeTableView.
* */
public class FirstColumnCell extends TreeTableCell<ValueData, ValueData> {

    @Override
    public void updateItem(ValueData item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setGraphic(null);
            setText(null);
            setStyle(null);
        } else {
            if(!item.isDisabled()) {
                if (item.isOption() || item.isOptValue()) {
                    CheckBox cb = new CheckBox();
                    cb.setSelected(item.isSelected());

                    cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            item.setSelectedNew(newValue);
                        }
                    });

                    setText(item.getValName());
                    setGraphic(cb);
                } else {
                    setText(item.getValName());
                }
            } else if(item.isDisabled()|| !item.isVisible()) {
                if(item.isOption() || item.isOptValue()) {
                    CheckBox cb = new CheckBox();
                    cb.setDisable(true);
                    setText(item.getValName());
                    setGraphic(cb);
                } else {
                    setText(item.getValName());
                }
            } else if(!item.isValidationValid()){
                if(item.isOption() || item.isOptValue()) {
                    CheckBox cb = new CheckBox();
                    cb.setDisable(true);
                    setText(item.getValName());
                    setGraphic(cb);
                } else {
                    setText(item.getValName());
                }
            }
        }
    }
}
