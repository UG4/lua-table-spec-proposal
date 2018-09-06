package edu.gcsc.vrl.luaparser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/*
 * Der ListController interagiert zwischen Daten und Anzeige.
 * Er wird mit den Daten aus ExtractionHelper initialisiert.
 * */

public class ListController {
    private Validator runtimeObject;

    @FXML
    BorderPane bp;
    @FXML
    private MenuItem loadValSpec;
    @FXML
    private MenuItem loadLuaFile;
    @FXML
    private MenuItem validateLua;
    @FXML
    private MenuItem exportLua;
    @FXML
    private TextArea loggingField;
    @FXML
    private TreeTableView<ValueData> outputTable;
    @FXML
    private TreeTableColumn<ValueData, ValueData> optionColumn;
    @FXML
    private TreeTableColumn<ValueData, ValueData> valueColumn;
    @FXML
    private ObservableList<ValueData> inputData = FXCollections.observableArrayList();

    public List<ValueData> getActData() {
        return inputData;
    }

    public void setValidator(Validator v) {
        this.runtimeObject = v;
    }

    /*
     * Die Initialisierungsmethode für den Controller. Hier werden die
     * CellValue/cell- Factory's zugeordnet
     * */
    public void initialize() throws InterruptedException {
        optionColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().getValProp());
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().getValProp());

        valueColumn.setCellFactory(column -> {
            return new MyValCell();
        });

        optionColumn.setCellFactory(column -> {
            return new FirstColumnCell();
        });

        //outputTable.setItems(inputData);
    }

    /*
     * Hier werden die Daten für die TreeTableView initialisiert und die entsprechenden Knoten angelegt.
     * */
    public void initData(List<ValueData> dataset) {
        for (ValueData v : dataset) {
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

        for (int i = 0; i < inputData.size(); i++) {
            System.out.println("NAME: " + inputData.get(i).getValName().get());
            TreeItem<ValueData> actV = new TreeItem<ValueData>(inputData.get(i));
            root.getChildren().add(actV);

            if (inputData.get(i).getOptions() != null) {
                for (int j = 0; j < inputData.get(i).getOptions().size(); j++) {
                    System.out.println("OPTIONS: " + inputData.get(i).getValName().get());
                    setOptionsTreeElements(actV, inputData.get(i).getOptions().get(j));
                }
            }
        }
    }

    /*
     * Geschaltete Groups und Sub-Parameter werden hier an das entsprechende TreeItem angehängt.
     * */
    private void setOptionsTreeElements(TreeItem<ValueData> ti, ValueData vd) {
        TreeItem<ValueData> childNode = new TreeItem<ValueData>(vd);
        ti.getChildren().add(childNode);

        if (vd.getOptions().size() > 0) {
            for (ValueData v : vd.getOptions()) {

                if (v.getOptions().size() > 0) {
                    setOptionsTreeElements(childNode, v);
                } else {
                    TreeItem<ValueData> child = new TreeItem<ValueData>(v);
                    childNode.getChildren().add(child);
                }
            }
        }
    }

    public void setLoadValidation() {
        /*
         * File-Dialog einbauen!!!!
         * */
        loadValSpec.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    /*final FileChooser fc = new FileChooser();
                    File file = fc.showOpenDialog(bp.getScene().getWindow());
                    if(file != null){

                    }*/
                    String path = "";
                    final FileChooser fc = new FileChooser();
                    fc.setTitle("Select a Validator-File(*.lua)");
                    FileChooser.ExtensionFilter extLua = new FileChooser.ExtensionFilter("Lua Files (*.lua)","*.lua");
                    fc.getExtensionFilters().add(extLua);

                    final File selecDir = fc.showOpenDialog(bp.getScene().getWindow());
                    if(selecDir != null){
                        path = selecDir.getAbsolutePath();
                    }

                    //Validator v = new Validator("/validationtest02.lua");
                    if(!path.isEmpty()) {
                        Validator v = new Validator(path);
                        setValidator(v);
                        v.visiting();
                        initData(runtimeObject.getData());
                    }
                } catch(IOException io){UIHelper.logging("Cant find the file!", loggingField);}
            }
        });
    }

    public void setLoadLua() {
        loadLuaFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }

    public void setValidateLua() {
        validateLua.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }

    public void setExportLua() {
        exportLua.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }
}
