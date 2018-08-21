package edu.gcsc.vrl.luaparser;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

/*
 * Der ListController interagiert zwischen Daten und Anzeige.
 * Er wird mit den Daten aus ExtractionHelper initialisiert.
 * */

public class ListController {
    private Validator runtimeObject;

    @FXML
    private TableView<ValueData> outputTable;
    @FXML
    private TableColumn<ValueData, String> optionColumn;
    @FXML
    private TableColumn<ValueData, ValProperty> valueColumn;
    @FXML
    private ObservableList<ValueData> inputData = FXCollections.observableArrayList();

    /*
    * Die Initialisierungsmethode für den Controller. Hier werden die
    * CellValue/cell- Factory's zugeordnet
    * */
    public void initialize() throws InterruptedException{
        optionColumn.setCellValueFactory(cellData -> cellData.getValue().getValNameProp());
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().getValprop());

        valueColumn.setCellFactory(column -> {
            return new MyValCell();
        });

        outputTable.setItems(inputData);
    }

    /*
    * Methode um einen Controller ein Datenset übergeben zu können
    * */
    public void initData(List<ValueData> dataset){
        for(ValueData v : dataset){
            inputData.add(v);
        }

        inputData.addListener(new ListChangeListener<ValueData>() {
            @Override
            public void onChanged(Change<? extends ValueData> c) {
                runtimeObject.setDataUi(inputData);
                System.out.println("Change detected");
                for(ValueData u : runtimeObject.getDataUi()){
                    System.out.println(u.getValName());
                }
            }
        });
    }

    public List<ValueData> getActData(){
        return inputData;
    }

    public void setValidator(Validator v){
        this.runtimeObject = v;
    }
}
