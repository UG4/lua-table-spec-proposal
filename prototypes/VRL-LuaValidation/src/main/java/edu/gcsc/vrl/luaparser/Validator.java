package edu.gcsc.vrl.luaparser;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import com.google.common.io.ByteStreams;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
        //System.out.println(filepath);
        // load lua code from resources
        //byte[] code2Data = ByteStreams.toByteArray(Main.class.getResourceAsStream(filepath));
        byte[] code2Data = ByteStreams.toByteArray(new FileInputStream(filepath));
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
            VisitingValidatorSpec.visitOne(importedCode,dataList);
            this.myData = dataList;
        }
    }
}
