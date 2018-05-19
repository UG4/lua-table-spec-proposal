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
            /*if (item.getType().equals("String")) {
                setText(item.getType());
                setStyle("");
            } else if(item.getType().equals("Integer")){
                setText("Integer");
                CheckBox checkBox = new CheckBox();
                setGraphic(checkBox);
            }*/
            switch(item.getType()){
                case "String":
                    setText(item.getType());
                    setStyle("");
                    break;
                case "Double":
                    setText("Integer");
                    CheckBox checkBox = new CheckBox();
                    setGraphic(checkBox);
                    break;
                case "Integer":
                    setText("Integer");
                    CheckBox checkBox1 = new CheckBox();
                    setGraphic(checkBox1);
                    break;
            }
        }

    }
}
