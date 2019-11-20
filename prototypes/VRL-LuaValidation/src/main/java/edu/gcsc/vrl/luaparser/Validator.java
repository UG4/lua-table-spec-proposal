package edu.gcsc.vrl.luaparser;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.ByteStreams;
import java.io.IOException;

/**
* This class represents the run-time model of the application. It contains the dataset with
* <code>ValueData</code>-objects, which were imported from the validation-spec.
* This dataset will be visualized in the UI and manipulated by the user.
* */
public class Validator {
    /**
     * Class variables
     * */
    private List<ValueData> myData;
    private Group importedCode;
    private String validationFileName;
    private String validationFilePath;

    /*
     * GETTER/SETTER methods
     * */
    /**
    * returns the actual dataset
    *
    * @return List<ValueData>
    * */
    public List<ValueData> getData() {
        return this.myData;
    }

    /**
    * returns the file-name of the validation-file
    *
    * @return String file name
    * */
    public String getValidationFileName() {
        return validationFileName;
    }

    /**
    * returs the path of the validation-file
    *
    * @return String path
    * */
    public String getValidationFilePath() {
        return validationFilePath;
    }


    /**
    * Sets the validation-filename
    *
    * @param fileName file name
    * */
    public void setValidationFileName(String fileName) {
        this.validationFileName = fileName;
    }

    /**
     * Constructor method
     *
     * @param filepath file path
     * */
    public Validator(String filepath) throws IOException {
        // Set the filepath
        this.validationFilePath = filepath;
        // load lua code
        byte[] code2Data = ByteStreams.toByteArray(new FileInputStream(filepath));
        String code = new String(code2Data, "UTF-8");

        // convert code to group
        this.importedCode = Group.toGroup(code);
        //GenUtil.printGroupTree(this.importedCode);
    }
    /**
    * For testing purposes only
    * */
    public Validator(String filepath, boolean test) throws IOException {
        // load lua code
        byte[] code2Data = ByteStreams.toByteArray(Main.class.getResourceAsStream(filepath));
        String code = new String(code2Data, "UTF-8");

        // convert code to group
        this.importedCode = Group.toGroup(code);
    }


    /**
    * Coordinates the creation of the data model objects.
    * */
    public void visiting(ValueDataFactory vdFactory) {
        List<ValueData> dataList = new ArrayList<>();
        if (importedCode != null) {
            VisitingValidatorSpec.visitOne(importedCode, dataList, vdFactory);
            this.myData = dataList;
        }
    }

    /**
    * This method checks, whether all parameters are valid.
     * First it checks, whether the value is in the value range.
    * Then it validates the <code>validate</code>-property and the
    * <code>visibility</code>-property. If an error occurs, it
    * will be saved in a message.
    *
    * @return allErrMsg a List of error messages
    * */
    public List<ErrorMessage> validate() {
        List<ErrorMessage> allErrMsg = new ArrayList<>();
        List<ErrorMessage> rangeErr = validateRange();
        List<ErrorMessage> validateErr = validateValidation();
        List<ErrorMessage> visibleErr = visibleValidation();
        allErrMsg.addAll(rangeErr);
        allErrMsg.addAll(validateErr);
        allErrMsg.addAll(visibleErr);

        return allErrMsg;
    }

    /**
     * Validate whether the values of all parameters are in their given value range.
     * If a value is not in its range, a <code>ErrorMessage</code> will be created.
     *
     * @return ErrorMessages
     * */
    public List<ErrorMessage> validateRange(){
        List<ErrorMessage> err = new ArrayList<>();
        List<ValueData> allValues = GenUtil.getAllValues(getData());

        for(ValueData v : allValues) {
            if (v.getRangeOfStrings() == null) {
                boolean isInRange = GenUtil.valueIsInRange(v);
                //System.out.println(v.getValName() + " range: " + isInRange);
                v.setValInRange(isInRange);
                if (isInRange == false) {
                    ErrorMessage notInRange = new ErrorMessage("Value: " + v.getValName() + " not in range!", 0, v.getValName());
                    err.add(notInRange);
                }
            } else {
                boolean isInRange = GenUtil.stringValIsInRange(v);

                v.setValInRange(isInRange);

                if (isInRange == false) {
                    ErrorMessage notInRange = new ErrorMessage("Value: " + v.getValName() + " not in range!", 0, v.getValName());
                    err.add(notInRange);
                }
            }
        }
        return err;
    }

    /**
    * Helping method for <code>validate()</code>.
    * It validates the <code>validate</code>-property of all parameters
    *
    * @return errList list with errors
    * */
    public List<ErrorMessage> validateValidation(){
        List<ErrorMessage> errList = new ArrayList<>();
        // Get all parameters
        List<ValueData> allValues = GenUtil.getAllValues(getData());

        // Check if parameters includes cycles
        List<ValueData> cycleNodesValid = GraphUtil.checkForCycles(getData(), allValues, true);
        for(ValueData v : cycleNodesValid){
            System.out.println("Cycle: "+ v.getValName());
            ErrorMessage e = new ErrorMessage("Validation-Knoten befindet sich in einem Zyklus",0,v.getValName());
            errList.add(e);
        }

        // get all dependent parameters
        List<ValueData> allDependingValuesValid = GenUtil.getAllDependingValidateValues(getData());
        for (ValueData v : allDependingValuesValid){
            System.out.println("Depending: "+ v.getValName());
        }

        for(ValueData v : allDependingValuesValid){
            if(!cycleNodesValid.contains(v)){
                boolean valid = GenUtil.validate(v,getData());
                System.out.println(v.getValName() + " validation: " + valid);
                v.setValidationIsValid(valid);
            }
        }
        return errList;
    }

    /**
     * Helping method for <code>validate</code>.
     * It validates the <code>visibility</code>-property of all parameters
     *
     * @return errList list with errors
     * */
    public List<ErrorMessage> visibleValidation(){
        List<ErrorMessage> errList = new ArrayList<>();
        // Alle Parameter herausfinden
        List<ValueData> allValues = GenUtil.getAllValues(getData());

        List<ValueData> cycleNodesVisib = GraphUtil.checkForCycles(getData(), allValues, false);
        for(ValueData v : cycleNodesVisib){
            System.out.println("Cycle: "+ v.getValName());
            ErrorMessage e = new ErrorMessage("Visibility-Knoten befindet sich in einem Zyklus",0,v.getValName());
            errList.add(e);
        }

        List<ValueData> allDependingValuesVisib = DependingUtil.getAllDependingVisibleValues(getData());
        for (ValueData v : allDependingValuesVisib){
            System.out.println("Depending: "+ v.getValName());
        }

        for(ValueData v : allDependingValuesVisib){
            if(!cycleNodesVisib.contains(v)){
                boolean valid = DependingUtil.validateVisible(v,getData());
                System.out.println(v.getValName() + " visibility: " + valid);
                v.setVisibility(valid);
            }
        }
        return errList;
    }
}

