package edu.gcsc.vrl.luaparser;

import java.util.ArrayList;
import java.util.List;
import com.google.common.io.ByteStreams;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Validator {
    /*
    * Klassenvariablen
    * */
    private List<ValueData> myData;
    private Group importedCode;
    private ListController lc;
    private List<ValueData> dataUi;

    public ListController getListController() {
        return this.lc;
    }

    public void setLc(ListController lc) {
        this.lc = lc;
    }

    public void setDataUi(List<ValueData> val){
        this.dataUi = val;
    }

    public List<ValueData> getDataUi() {
        return this.dataUi;
    }

    /*
    * GETTER / SETTER Methoden
    * */
    public List<ValueData> getData() {
        return this.myData;
    }

    /*
    * Konstruktor
    * */
    public Validator(String filepath) throws IOException{
        // load lua code from resources
        byte[] code2Data = ByteStreams.toByteArray(Main.class.getResourceAsStream(filepath));
        String code = new String(code2Data, "UTF-8");

        // convert code to group
        this.importedCode = Group.toGroup(code);

    }

    /*
    * Objekt - Methoden
    * */
    /*
    * Ist der Startpunkt, mit dem die Hilfsmethoden für das visiting aufgerufen werden
    * */
    public void visiting(){
        List<ValueData> dataList = new ArrayList<>();
        if(importedCode != null) {
            ExtractionHelper.visit(importedCode, dataList);
            this.myData = dataList;
        }
    }

    /*
    * Erstellt ein eigenständiges UI, das einen Controller zugeordnet bekommt.
    * Platform.runLater() muss benutzt werden, weil Daten für das UI in JavaFX
    * in einem speziellen JavaFX Thread bearbeitet werden.
    * !!!!!!!!! Ergänzende Infos hinzufügen in die Beschreibung
    * */
    public void loadUI(){


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try{
                    //setNewLoader(new FXMLLoader(Main.class.getResource("mainwindow.fxml")));
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("mainwindow.fxml"));


                    Platform.runLater(new Runnable() {
                        public void run() {
                            setLc(loader.<ListController>getController());
                        }
                    });


                    Stage stage = new Stage();
                    stage.setTitle("Parameter Eingabe");


                    TitledPane page = (TitledPane) loader.load();
                    Scene scene = new Scene(page);
                    stage.setScene(scene);

                    //ListController lCon = loader.<ListController>getController();
                    //ListController lCon = new ListController();
                    //newLoader.setController(lCon);


                    Platform.runLater(new Runnable() {
                        public void run() {
                            getListController().initData(getData());
                        }
                    });

                    stage.show();

                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    /*
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("mainwindow.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Parameter EIngabe");TitledPane page = (TitledPane) loader.load();
                    Scene scene = new Scene(page);
                    stage.setScene(scene);
                    ListController lCon = loader.<ListController>getController();
                    lCon.initData(getData());
                    stage.show();*/


                } catch(IOException e){}
            }
        });
    }

    public void getActDataFromUI(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                List<ValueData> data = getListController().getActData();
                                setDataUi(data);
                                List<ValueData> test = getDataUi();
                                for(ValueData g : test){
                                    System.out.println(g.getValName());
                                    System.out.println(g.getValprop().toString());
                                }
                            }
                        }
                );
            }
        });
    }
}
