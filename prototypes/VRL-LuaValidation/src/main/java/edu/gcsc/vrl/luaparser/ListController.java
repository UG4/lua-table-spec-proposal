package edu.gcsc.vrl.luaparser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class ListController {
    @FXML
    private TableView<ValueData> outputTable;
    @FXML
    private Button aButton;
    @FXML
    private TableColumn<ValueData, String> optionColumn;
    @FXML
    private TableColumn<ValueData, String> valueColumn;
    @FXML
    private ObservableList<ValueData> inputData = FXCollections.observableArrayList();

    public ListController(){
        ArrayList<ValueData> controllerData = ExtractionHelper.getData();
        for(ValueData e : controllerData){
            inputData.add(e);
        }
    }

    public void initialize() throws InterruptedException{
        optionColumn.setCellValueFactory(cellData -> cellData.getValue().getValNameProp());
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().getDefProp());

        valueColumn.setCellValueFactory(column -> {
            return new myValCell();
        });
    }
}
