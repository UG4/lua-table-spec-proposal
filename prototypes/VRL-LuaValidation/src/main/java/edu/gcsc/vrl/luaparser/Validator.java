package edu.gcsc.vrl.luaparser;

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
    public void visiting(){
        List<ValueData> dataList = new ArrayList<>();
        if(importedCode != null) {
            ExtractionHelper.visit(importedCode, dataList);
            this.myData = dataList;
        }
    }


}
