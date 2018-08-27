package edu.gcsc.vrl.luaparser;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

/*
 * Der ListController interagiert zwischen Daten und Anzeige.
 * Er wird mit den Daten aus ExtractionHelper initialisiert.
 * */

public class ListController {
    private Validator runtimeObject;

    @FXML
    private TreeTableView<ValueData> outputTable;
    @FXML
    private TreeTableColumn<ValueData, String> optionColumn;
    @FXML
    private TreeTableColumn<ValueData, ValueData> valueColumn;
    @FXML
    private ObservableList<ValueData> inputData = FXCollections.observableArrayList();

    public List<ValueData> getActData(){
        return inputData;
    }

    public void setValidator(Validator v){
        this.runtimeObject = v;
    }

    /*
    * Die Initialisierungsmethode für den Controller. Hier werden die
    * CellValue/cell- Factory's zugeordnet
    * */
    public void initialize() throws InterruptedException{
        optionColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().getValName());
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().getValProp());

        valueColumn.setCellFactory(column -> {
            return new MyValCell();
        });

        //outputTable.setItems(inputData);
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

        TreeItem<ValueData> root = new TreeItem<ValueData>();
        root.setExpanded(false);
        outputTable.setRoot(root);
        outputTable.setShowRoot(false);

        for(int i = 0; i < inputData.size(); i++){
            TreeItem<ValueData> actV = new TreeItem<ValueData>(inputData.get(i));
            root.getChildren().add(actV);

            if(inputData.get(i).getSubParams() != null){
                for(int j = 0; j < inputData.get(i).getSubParams().size(); j++){
                    actV.getChildren().add(new TreeItem<ValueData>(inputData.get(i).getSubParams().get(j)));
                }
            }
        }
    }


}
