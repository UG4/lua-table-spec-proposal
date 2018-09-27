package edu.gcsc.vrl.luaparser;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.ByteStreams;
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
    * For testing purposes only
    * */
    public Validator(String filepath, boolean test) throws IOException {
        // Set the filepath
        //this.validationFilePath = filepath;
        // load lua code
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
    public void visiting() {
        List<ValueData> dataList = new ArrayList<>();
        if (importedCode != null) {
            VisitingValidatorSpec.visitOne(importedCode, dataList);
            this.myData = dataList;
        }
    }

    public List<ErrorMessage> validate() {
        List<ErrorMessage> allErrMsg = new ArrayList<>();
        List<ErrorMessage> validateErr = validateValidation();
        List<ErrorMessage> visibleErr = visibleValidation();
        allErrMsg.addAll(validateErr);
        allErrMsg.addAll(visibleErr);

        return allErrMsg;
    }
    public List<ErrorMessage> validateValidation(){
        List<ErrorMessage> errList = new ArrayList<>();
        // Alle Parameter herausfinden
        List<ValueData> allValues = GenUtil.getAllValues(getData());

        /*
         * Hier wird der dependsOn()-Parameter für 'validation' gecheckt
         * */

        // Diese Parameter auf Zyklen checken und an Zyklen beteiligte Nodes in einer Liste speichern
        List<ValueData> cycleNodesValid = GraphUtil.checkForCycles(getData(), allValues, true);
        for(ValueData v : cycleNodesValid){
            System.out.println("Cycle: "+ v.getValName().get());
            ErrorMessage e = new ErrorMessage("Validation-Knoten befindet sich in einem Zyklus",0,v.getValName().get());
            errList.add(e);
        }

        // Alle Parameter herausfinden, die von anderen Parametern abhängig sind
        List<ValueData> allDependingValuesValid = GenUtil.getAllDependingValidateValues(getData());
        for (ValueData v : allDependingValuesValid){
            System.out.println("Depending: "+ v.getValName().get());
        }

        for(ValueData v : allDependingValuesValid){
            if(!cycleNodesValid.contains(v)){
                boolean valid = GenUtil.validate(v,getData());
                System.out.println(v.getValName().get() + " validation: " + valid);
                v.setValidationIsValid(valid);
            }
        }
        return errList;
    }

    public List<ErrorMessage> visibleValidation(){
        List<ErrorMessage> errList = new ArrayList<>();
        // Alle Parameter herausfinden
        List<ValueData> allValues = GenUtil.getAllValues(getData());
        /*
         * Hier wird der dependsOn()-Parameter für 'visible' gecheckt
         * */
        List<ValueData> cycleNodesVisib = GraphUtil.checkForCycles(getData(), allValues, false);
        for(ValueData v : cycleNodesVisib){
            System.out.println("Cycle: "+ v.getValName().get());
            ErrorMessage e = new ErrorMessage("Visibility-Knoten befindet sich in einem Zyklus",0,v.getValName().get());
            errList.add(e);
        }

        List<ValueData> allDependingValuesVisib = DependingUtil.getAllDependingVisibleValues(getData());
        for (ValueData v : allDependingValuesVisib){
            System.out.println("Depending: "+ v.getValName().get());
        }

        for(ValueData v : allDependingValuesVisib){
            if(!cycleNodesVisib.contains(v)){
                boolean valid = DependingUtil.validateVisible(v,getData());
                System.out.println(v.getValName().get() + " visibility: " + valid);
                v.setVisibility(valid);
            }
        }
        return errList;
    }
}

