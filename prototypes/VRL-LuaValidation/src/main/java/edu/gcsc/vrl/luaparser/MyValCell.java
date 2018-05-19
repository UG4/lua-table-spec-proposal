package edu.gcsc.vrl.luaparser;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

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
            if (item.toString().equals("Integer")) {
                setText(item.toString());
                setStyle("");
            } else if(item.toString().equals("String")){
                CheckBox checkBox = new CheckBox();
                setGraphic(checkBox);
            }
        }

    }
}
