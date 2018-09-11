package edu.gcsc.vrl.luaparser;

import java.io.*;
import java.util.ArrayList;
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
    private MenuItem setPrefs;
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
        inputData.clear();
        for (ValueData v : dataset) {
            inputData.add(v);
        }

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
     * Geschachtelte Groups und Sub-Parameter werden hier an das entsprechende TreeItem angehängt.
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
        loadValSpec.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String path = "";
                    final FileChooser fc = new FileChooser();
                    fc.setTitle("Select a Validator-File(*.lua)");
                    if(!PreferencesUtil.getInitValidationImportPath().isEmpty()) {
                        fc.setInitialDirectory(new File(PreferencesUtil.getInitValidationImportPath()));
                    } else {
                        fc.setInitialDirectory(new File("C:/Users/"));
                    }
                    // Funktioniert vllt nicht für jedes OS
                    FileChooser.ExtensionFilter extLua = new FileChooser.ExtensionFilter("Lua Files (*.lua)", "*.lua");
                    fc.getExtensionFilters().add(extLua);

                    final File selecDir = fc.showOpenDialog(bp.getScene().getWindow());
                    if (selecDir != null) {
                        path = selecDir.getAbsolutePath();
                    }

                    if (!path.isEmpty()) {
                        Validator v = new Validator(path);
                        setValidator(v);
                        v.visiting();
                        initData(runtimeObject.getData());
                    }
                } catch (IOException io) {
                    UIHelper.logging("Cant find the file!", loggingField);
                }
            }
        });
    }

    public void setLoadLua() {
        loadLuaFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String path = "";
                    final FileChooser fc = new FileChooser();
                    fc.setTitle("Select a Lua-File(*.lua)");
                    if(!PreferencesUtil.getInitialLuaLoadPath().isEmpty()) {
                        fc.setInitialDirectory(new File(PreferencesUtil.getInitialLuaLoadPath()));
                    } else {
                        fc.setInitialDirectory(new File("C:/Users/"));
                    }
                    // Funktioniert vllt nicht für jedes OS
                    FileChooser.ExtensionFilter extLua = new FileChooser.ExtensionFilter("Lua Files (*.lua)", "*.lua");
                    fc.getExtensionFilters().add(extLua);

                    final File selecDir = fc.showOpenDialog(bp.getScene().getWindow());
                    if (selecDir != null) {
                        path = selecDir.getAbsolutePath();
                    }

                    if (!path.isEmpty()) {
                        List<ValueData> data = new ArrayList<>();
                        Group loadedLua = LoadLua.parseLuaFile(path);
                        LoadLua.visitingLuaCode(loadedLua, data);
                        LoadLua.matchingValues(runtimeObject.getData(),data);
                        /*for(ValueData d : data){
                            System.out.println(d.getValName().get());
                            if(d.getActData() != null && d.getActData().getValue() != null){
                                System.out.println(d.getActData().getValue());
                            }
                            System.out.println("_______-");
                        }*/
                    }
                } catch (IOException io) {
                    UIHelper.logging("Cant find the file!", loggingField);
                }
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
                //String luaCode = ExportLua.doExport(runtimeObject.getData());
                String luaCode = ExportLuaAlternative.doExport(runtimeObject.getData());
                String path = "";
                try {
                    // Choose Path
                    final DirectoryChooser dc = new DirectoryChooser();
                    dc.setTitle("Select a .lua-File");

                    if(!PreferencesUtil.getInitValidationImportPath().isEmpty()) {
                        dc.setInitialDirectory(new File(PreferencesUtil.getInitialLuaExportPath()));
                    } else {
                        dc.setInitialDirectory(new File("C:/Users/"));
                    }

                    final File selecDir = dc.showDialog(bp.getScene().getWindow());
                    if (selecDir != null) {
                        path = selecDir.getAbsolutePath();
                    }

                    // Writing Lua-File
                    Writer filewriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path+"/luaexpo.lua"), "utf-8"));
                    filewriter.write(luaCode);
                    filewriter.close();

                } catch(IOException fnf){}
            }
        });
    }

    public void setPrefs() {
        setPrefs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                /*
                * Muss noch hinzugefügt werden.
                * Hier soll die Preferences.xml Datei geöffnet werde, damit man sie bearbeiten kann.
                * */
            }
        });
    }
}
