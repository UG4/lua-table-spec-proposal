package edu.gcsc.vrl.luaparser;

import java.awt.event.ActionEvent;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
    private Button doLuaBut;
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
    * Hier werden die Daten für die TreeTableView initialisiert und die entsprechenden Knoten angelegt
    * (Auch SubParameter)
    * */
    public void initData(List<ValueData> dataset){
        for(ValueData v : dataset){
            inputData.add(v);
        }

        inputData.addListener(new ListChangeListener<ValueData>() {
            @Override
            public void onChanged(Change<? extends ValueData> c) {
                /*
                * Zu einem späterem Zeitpunkt überprüfen, ob nötig
                * */
            }
        });

        TreeItem<ValueData> root = new TreeItem<ValueData>();
        root.setExpanded(false);
        outputTable.setRoot(root);
        outputTable.setShowRoot(false);

        for(int i = 0; i < inputData.size(); i++){
            System.out.println("SIZE: "+ inputData.size());
            TreeItem<ValueData> actV = new TreeItem<ValueData>(inputData.get(i));
            root.getChildren().add(actV);

            if(inputData.get(i).getOptions() != null){
                for(int j = 0; j < inputData.get(i).getOptions().size(); j++){
                    //TreeItem<ValueData> childNode = new TreeItem<ValueData>(inputData.get(i).getOptions().get(j));
                    //actV.getChildren().add(childNode);
                    System.out.println("OPTIONS: " + inputData.get(i).getOptions().get(j).getValName().get());
                    setOptionsTreeElements(actV,inputData.get(i).getOptions().get(j));
                }
            }
        }
    }

    private void setOptionsTreeElements(TreeItem<ValueData> ti, ValueData vd){
        TreeItem<ValueData> childNode = new TreeItem<ValueData>(vd);
        ti.getChildren().add(childNode);
        //System.out.println("LALA: " + vd.getValName().get());
        if(vd.getOptions() != null) {
            for (ValueData v : vd.getOptions()) {
                TreeItem<ValueData> child = new TreeItem<ValueData>(v);
                System.out.println("CHILDNAME: " + child.getValue().getValName().get());
                System.out.println("PARENTNAME: " + ti.getValue().getValName().get());
                childNode.getChildren().add(child);
                if (v.getOptions() != null) {
                    //System.out.println("TEST :" + v.getValName().get());
                    setOptionsTreeElements(child, v);
                }
            }
        }
    }

    public void setBtnAct(){
        doLuaBut.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                List<ValueData> dat = runtimeObject.getData();
                for(ValueData v : getActData()){
                    System.out.println(v.getValName().get());
                    if(v.getActData() != null){
                        System.out.println("Act-Val: " + v.getActData());
                    }
                    if(v.getOptions() != null){
                        for(ValueData x : v.getOptions()){
                            System.out.println(x.getValName().get() + " : " + x.getType().get());
                            if(x.getActData() != null){
                                System.out.println("Act-Val: " + v.getActData());
                            }
                        }
                    }
                }
            }
        });
    }
}
