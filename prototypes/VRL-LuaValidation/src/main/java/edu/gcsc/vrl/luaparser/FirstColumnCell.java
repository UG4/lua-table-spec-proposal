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
            //TEST
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

                    setText(item.getValName().get());
                    setGraphic(cb);
                } else {
                    setText(item.getValName().get());
                }
            } else {
                if(item.isOption() || item.isOptValue()) {
                    //setDisable(true);
                    CheckBox cb = new CheckBox();
                    cb.setDisable(true);
                    setText(item.getValName().get());
                    setGraphic(cb);
                } else {
                    //setDisable(true);
                    setText(item.getValName().get());
                }
            }
            ///TEST
        }
    }
}
