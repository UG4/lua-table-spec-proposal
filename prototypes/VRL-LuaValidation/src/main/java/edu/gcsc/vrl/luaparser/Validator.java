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
    private String validationFileName;
    private String validationFilePath;

    /*
    * GETTER / SETTER Methoden
    * */
    public List<ValueData> getData() {
        return this.myData;
    }

    public String getValidationFileName() { return validationFileName; }

    public String getValidationFilePath() { return validationFilePath; }

    public void setValidationFileName(String fileName){ this.validationFileName = fileName; }

    /*
    * Konstruktor
    * */
    public Validator(String filepath) throws IOException{
        // Set the filepath
        this.validationFilePath = filepath;
        // load lua code
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

    public void validate(){
        List<ValueData> dependingValidValues = GenUtil.getDependingValidateValues(getData());
        List<ValueData> allValues = GenUtil.getAllValues(getData());
        ValueData[][] dependings = new ValueData[allValues.size()][allValues.size()];
        boolean circle = false;

        for(int i = 0; i < allValues.size(); i++){
            dependings[i] = GenUtil.validateAValue(allValues.get(i), getData(),allValues.size());
        }

        int[][] dep = new int[allValues.size()][allValues.size()];

        for (int i = 0; i < allValues.size(); i++) {
            for(int j = 0; j < allValues.size(); j++){
                if(GenUtil.containsVD(dependings[i],allValues.get(j))){
                    dep[i][j] = 1;
                } else {
                    dep[i][j] = 0;
                }
            }
        }

        for (int i = 0; i < allValues.size(); i++) {
            for(int j = 0; j < allValues.size(); j++){
                if((dep[i][j] == 1) && (dep[j][i] == 1)){
                    circle = true;
                }
            }
        }

        System.out.println("CIRCLE: " + circle);


    }
}
