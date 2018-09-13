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

    public String getValidationFileName() {
        return validationFileName;
    }

    public String getValidationFilePath() {
        return validationFilePath;
    }

    public void setValidationFileName(String fileName) {
        this.validationFileName = fileName;
    }

    /*
     * Konstruktor
     * */
    public Validator(String filepath) throws IOException {
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
    public void visiting() {
        List<ValueData> dataList = new ArrayList<>();
        if (importedCode != null) {
            VisitingValidatorSpec.visitOne(importedCode, dataList);
            this.myData = dataList;
        }
    }

    public void validate() {
        List<ValueData> allValues = GenUtil.getAllValues(getData());
        ValueData[][] dependings = new ValueData[allValues.size()][allValues.size()];

        for (int i = 0; i < allValues.size(); i++) {
            List<ValueData> vals = GenUtil.validateAValue(allValues.get(i), getData());
            ValueData[] kl = new ValueData[allValues.size()];
            for (int j = 0; j < vals.size(); j++) {
                dependings[i][j] = vals.get(j);
            }
        }

        int[][] dep = new int[allValues.size()][allValues.size()];


        // Erstellen der Adjazenzmatrix
        StringBuilder sh = new StringBuilder();
        for (int i = 0; i < allValues.size(); i++) {
            for (int j = 0; j < allValues.size(); j++) {
                if (GenUtil.containsVD(dependings[i], allValues.get(j))) {
                    dep[i][j] = 1;
                    sh.append(1);
                } else {
                    dep[i][j] = 0;
                    sh.append(0);
                }

            }
            sh.append("\n");
        }

        // Testzwecke
        System.out.println(sh.toString());


        // 0 = noch nicht bearbeitet
        // 1 = in bearbeitung
        // 2 = bereits bearbeitet

        for (int x = 0; x < allValues.size(); x++) {
            int[] marking = new int[allValues.size()];
            for (int i = 0; i < allValues.size(); i++) {
                marking[i] = 0;
            }

            boolean cycle = GenUtil.cycle(x, marking, dep, allValues.size());
            System.out.println(allValues.get(x).getValName().get());
            System.out.println("CYCLE: " + cycle);
        }
    }
}

