package edu.gcsc.vrl.luaparser;

import javafx.scene.control.TableCell;

public class MyValCell extends TableCell<ValueData, String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
            setStyle("");
        } else {
            if (true){

            }
        }

    }
}
