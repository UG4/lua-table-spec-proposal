package edu.gcsc.vrl.luaparser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

/**
* <code>TreeViewController</code> is the controller-class based on the MVC-concept.
* It handles user interaction and data manipulation. Furthermore it set ups the data,
* that is visualized in the UI.
* */
public class TreeViewController {
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
    /**
    * Setting the runtime-object <code>Validator</code>
    * @param v Validator object
    * */
    public void setValidator(Validator v) {
        this.runtimeObject = v;
    }

    /**
    * initializing method, that sets up the CellFactory and CellValueFactory
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
    }

    /**
    * initializes data for TreeTableView und creates <code>TreeItems</code>.
    * Sets the ChangeListeners for UI.
    *
    * @param dataset actual set of parameters
    * */
    public void initData(List<ValueData> dataset) {

        inputData.removeAll();
        inputData.clear();

        for (ValueData v : dataset) {
            inputData.add(v);
        }

        TreeItem<ValueData> root = new TreeItem<ValueData>();
        root.setExpanded(false);
        outputTable.setRoot(root);
        outputTable.setShowRoot(false);

        for (int i = 0; i < inputData.size(); i++) {
            inputData.get(i).getSelectedProp().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    outputTable.refresh();
                }
            });
            inputData.get(i).disabledProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    outputTable.refresh();
                }
            });
            System.out.println("NAME: " + inputData.get(i).getValName().get());
            TreeItem<ValueData> actV = new TreeItem<ValueData>(inputData.get(i));
            root.getChildren().add(actV);

            if (inputData.get(i).getOptions() != null) {
                for (int j = 0; j < inputData.get(i).getOptions().size(); j++) {
                    System.out.println("OPTIONS: " + inputData.get(i).getValName().get());
                    setOptionsTreeElements(actV, inputData.get(i).getOptions().get(j));

                    inputData.get(i).getOptions().get(j).getSelectedProp().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            outputTable.refresh();
                        }
                    });
                    inputData.get(i).getOptions().get(j).disabledProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            outputTable.refresh();
                        }
                    });
                }
            }
        }
    }

    /**
    * Helping method
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

    /**
    * Creates action for EventHandler
    * */
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
                    }
                    FileChooser.ExtensionFilter extLua = new FileChooser.ExtensionFilter("Lua Files (*.lua)", "*.lua");
                    fc.getExtensionFilters().add(extLua);

                    final File selecDir = fc.showOpenDialog(bp.getScene().getWindow());
                    if (selecDir != null) {
                        path = selecDir.getAbsolutePath();
                    }

                    if (!path.isEmpty()) {
                        Validator v = new Validator(path);
                        setValidator(v);
                        v.setValidationFileName(selecDir.getName());
                        v.visiting();
                        initData(runtimeObject.getData());
                    }
                } catch (IOException io) {
                    UIUtil.logging("Cant find the file!", loggingField);
                }
            }
        });
    }

    /**
     * Creates action for EventHandler
     * */
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
                    }
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
                        LoadLua.match(runtimeObject.getData(),data);
                    }
                } catch (IOException io) {
                    UIUtil.logging("Cant find the file!", loggingField);
                }
            }
        });
    }

    /**
     * Creates action for EventHandler
     * */
    public void setValidateLua() {
        validateLua.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<ErrorMessage> err = runtimeObject.validate();
                for(ErrorMessage e : err){
                    System.out.println(e.getParamName() + " : "+ e.getMsg());
                }
            }
        });
    }

    /**
     * Creates action for EventHandler
     * */
    public void setExportLua() {
        exportLua.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String luaCode = ExportLua.doExport(runtimeObject.getData(), runtimeObject.getValidationFileName());
                String path = "";
                try {
                    // Choose Path
                    final FileChooser fc = new FileChooser();
                    fc.setTitle("Select a .lua-File");
                    FileChooser.ExtensionFilter extLua = new FileChooser.ExtensionFilter("Lua Files (*.lua)", "*.lua");
                    fc.getExtensionFilters().add(extLua);

                    if(!PreferencesUtil.getInitValidationImportPath().isEmpty()) {
                        fc.setInitialDirectory(new File(PreferencesUtil.getInitialLuaExportPath()));
                    }

                    final File selecDir = fc.showSaveDialog(bp.getScene().getWindow());
                    if (selecDir != null) {
                        path = selecDir.getAbsolutePath();
                    }

                    // Writing Lua-File
                    Writer filewriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
                    filewriter.write(luaCode);
                    filewriter.close();

                } catch(IOException fnf){}
            }
        });
    }
}
