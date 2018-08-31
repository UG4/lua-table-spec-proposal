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
            //ExtractionHelper.visit(importedCode, dataList);
            Visiting.visitOne(importedCode,dataList);
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
        Validator valReference = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try{
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("mainwindow.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Parameter Eingabe");
                    TitledPane page = (TitledPane) loader.load();
                    Scene scene = new Scene(page);
                    stage.setScene(scene);
                    ListController lCon = loader.<ListController>getController();
                    lCon.initData(getData());
                    lCon.setValidator(valReference);
                    lCon.setBtnAct();
                    stage.show();

                } catch(IOException e){}
            }
        });
    }

    public void printTree(){
        for(ValueData vd : getData()){
            print("Node: ");
            print(vd.getValName().get());
            if(vd.getOptions() != null){
                print("Size: ");
                print(vd.getOptions().size());
                for (ValueData xd : vd.getOptions()){
                    print("OPT: ");
                    print(xd.getValName().get());
                    if(xd.getOptions() != null){
                        print("Size: ");
                        print(xd.getOptions().size());
                        printOptions(xd);
                    }
                    print("----------");
                }
            }
        }
    }

    public void printSize(){
       for( ValueData vd : getData()){
           print("Node: ");
           print(vd.getValName().get());
           if(vd.getOptions() != null){
               print("Size: ");
               print(vd.getOptions().size());
               for (ValueData xd : vd.getOptions()){
                   print("Size: ");
                   print(xd.getOptions().size());
                   if(xd.getOptions() != null){
                       printOptions(xd);
                   }
                   print("----------");
               }
           }
        }
    }

    public void printOptions(ValueData v){
        if(v.getOptions() != null){
            print("Node: ");
            print(v.getValName().get());
            for(ValueData vd : v.getOptions()){
                print("Size: ");
                print(vd.getOptions().size());
                if(vd.getOptions() != null){
                    printOptions(vd);
                }
            }
            print("_______________");
        }
    }

    public void print(Object str){
        System.out.println(String.valueOf(str));
    }
}
