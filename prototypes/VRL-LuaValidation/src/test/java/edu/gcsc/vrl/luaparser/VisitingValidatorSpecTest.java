package edu.gcsc.vrl.luaparser;

import com.google.common.io.ByteStreams;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class VisitingValidatorSpecTest {

    @Test
    void visitOne() throws Exception {
        /*
        * Setting up expected data
        * */
        List<ValueData> testList = new ArrayList<>();
        ValueData valueOne = new ValueData("valueOne");
        ValueData opt1 = new ValueData("1");
        ValueData opt2 = new ValueData("2");
        ValueData  subParam1 = new ValueData("subParam1");
        ValueData  subParam2 = new ValueData("subParam2");
        ValueData  valueTwo = new ValueData("valueTwo");

        ActualDataValue adv1 = new ActualDataValue();
        adv1.setType("Integer");
        adv1.setValue(20);
        opt1.setActData(adv1);
        opt1.setDefaultVal(20);

        ActualDataValue adv2 = new ActualDataValue();
        adv2.setType("String");
        adv2.setValue("lala");
        subParam1.setActData(adv2);
        subParam1.setDefaultVal("lala");

        ActualDataValue adv3 = new ActualDataValue();
        adv3.setType("String");
        adv3.setValue("hshs");
        subParam2.setActData(adv3);
        subParam2.setDefaultVal("hshs");

        ActualDataValue adv4 = new ActualDataValue();
        adv4.setType("String");
        adv4.setValue("lala");
        valueTwo.setActData(adv4);
        valueTwo.setDefaultVal("lala");

        valueOne.addSubParam(opt1);
        opt1.setParentNode(valueOne);
        valueOne.addSubParam(opt2);
        opt2.setParentNode(valueOne);

        opt2.addSubParam(subParam1);
        subParam1.setParentNode(opt2);
        opt2.addSubParam(subParam2);
        subParam2.setParentNode(opt2);

        testList.add(valueOne);
        testList.add(valueTwo);

        /*
        * Run method to test
        * */
        byte[] code2Data = ByteStreams.toByteArray(Main.class.getResourceAsStream("/validationtest04.lua"));
        String code = new String(code2Data, "UTF-8");
        Group importedCode = Group.toGroup(code);

        List<ValueData> programList = new ArrayList<>();
        VisitingValidatorSpec.visitOne(importedCode,programList,ValueDataFX::new);

        /*
        * Check if all expected names are in the list
        * */
        List<String> allNamesExpected =  GenUtil.getAllVDNames(testList);
        List<String> allNamesActual = GenUtil.getAllVDNames(programList);

        assertEquals(allNamesExpected.size(),allNamesActual.size(),"Size must be equal");

        for(String s : allNamesExpected){
            assertTrue(allNamesActual.contains(s), "Parameter: " + s + " was not found");
        }

        /*
        * Compare all expected values with the values from the test run
        * */
        List<ValueData> vdExpected = GenUtil.getAllVD(testList);
        List<ValueData> vdActual = GenUtil.getAllVD(programList);

        for(int i = 0 ; i < vdExpected.size(); i++){
            ValueData actActual = vdActual.get(i);
            ValueData actExpected = vdExpected.get(i);

            if(actExpected.getActData() != null){
                assertTrue(actExpected.getActData().getValue().toString().equals(actActual.getActData().getValue().toString()),"Value is not equal!");
            }
        }

    }
}