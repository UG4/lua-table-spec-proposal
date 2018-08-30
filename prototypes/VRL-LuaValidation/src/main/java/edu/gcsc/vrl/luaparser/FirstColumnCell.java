package edu.gcsc.vrl.luaparser;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeTableCell;

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
                    CheckBox option = new CheckBox();
                    option.setSelected(item.isSelected());
                    option.selectedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if (newValue) {
                                item.setSelected(true);

                                if (!item.getParentNode().isNestedGroup()) {
                                    for (ValueData vd : item.getParentNode().getOptions()) {
                                        if (!item.equals(vd)) {
                                            vd.setDisabled(true);
                                        }
                                    }
                                }
                            } else if (!newValue) {
                                item.setSelected(false);
                                for (ValueData vd : item.getParentNode().getOptions()) {
                                    if (!item.equals(vd)) {
                                        vd.setDisabled(false);
                                    }
                                }
                            }
                        }
                    });
                    setText(item.getValName().get());
                    setGraphic(option);

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
