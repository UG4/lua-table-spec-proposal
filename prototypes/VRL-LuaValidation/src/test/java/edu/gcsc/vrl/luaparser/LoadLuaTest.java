package edu.gcsc.vrl.luaparser;

import org.junit.jupiter.api.Test;
import com.google.common.io.ByteStreams;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoadLuaTest {

    @Test
    void visitingLuaCode() throws Exception {
        /**
         * Setting up the expected data
         * */
        List<ValueData> vdExpected = new ArrayList<>();

        ValueData valueOne = new ValueData("valueOne");
        ValueData subParam1 = new ValueData("subParam1");
        ValueData subParam2 = new ValueData("subParam2");
        ValueData valueTwo = new ValueData("valueTwo");

        ActualDataValue adv1 = new ActualDataValue();
        adv1.setType("String");
        adv1.setValue("d");
        subParam1.setActData(adv1);

        ActualDataValue adv2 = new ActualDataValue();
        adv2.setType("String");
        adv2.setValue("a");
        subParam2.setActData(adv2);

        ActualDataValue adv3 = new ActualDataValue();
        adv3.setType("String");
        adv3.setValue("b");
        valueTwo.setActData(adv3);

        valueOne.addSubParam(subParam1);
        valueOne.addSubParam(subParam2);
        subParam1.setParentNode(valueOne);
        subParam2.setParentNode(valueOne);

        vdExpected.add(valueOne);
        vdExpected.add(valueTwo);

        /*
         * Run the method to test
         * */
        byte[] code2Data = ByteStreams.toByteArray(Main.class.getResourceAsStream("/test.lua"));
        String code = new String(code2Data, "UTF-8");

        Group importedCode = Group.toGroup(code);
        List<ValueData> vdActual = new ArrayList<>();
        LoadLua.visitingLuaCode(importedCode,vdActual);

        /*
        * Comparing expected names with names from test run
        * */
        List<String> allNamesExpected =  GenUtil.getAllVDNames(vdExpected);
        List<String> allNamesActual = GenUtil.getAllVDNames(vdActual);

        assertEquals(allNamesExpected.size(),allNamesActual.size(),"Size must be equal");

        for(String s : allNamesExpected){
            assertTrue(allNamesActual.contains(s), "Parameter: " + s + " was not found");
        }

        /*
        * Compare expected values with values from test run
        * */
        List<ValueData> expect = GenUtil.getAllVD(vdExpected);
        List<ValueData> act = GenUtil.getAllVD(vdActual);

        for(int i = 0; i < expect.size(); i++){
            ValueData eAct = expect.get(i);
            ValueData aAct = act.get(i);
            if(eAct.getActData() != null && eAct.getActData().getValue() != null){
                assertTrue(eAct.getActData().getValue().toString().equals(aAct.getActData().getValue().toString()));
            }
        }

    }

    @Test
    void match() throws Exception {
        /*
         * Setting up the expected data
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
        adv2.setValue("d");
        subParam1.setActData(adv2);

        ActualDataValue adv3 = new ActualDataValue();
        adv3.setType("String");
        adv3.setValue("a");
        subParam2.setActData(adv3);

        ActualDataValue adv4 = new ActualDataValue();
        adv4.setType("String");
        adv4.setValue("b");
        valueTwo.setActData(adv4);

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
        * Load Lua-File with Params
        * */
        byte[] code2Data = ByteStreams.toByteArray(Main.class.getResourceAsStream("/test.lua"));
        String code = new String(code2Data, "UTF-8");

        Group importedCode = Group.toGroup(code);
        List<ValueData> vdLua = new ArrayList<>();
        LoadLua.visitingLuaCode(importedCode,vdLua);

        /*
        * Load validation-spec
        * */
        byte[] code2d = ByteStreams.toByteArray(Main.class.getResourceAsStream("/validationtest04.lua"));
        String valicode = new String(code2d, "UTF-8");
        Group importedSpec = Group.toGroup(valicode);

        List<ValueData> programList = new ArrayList<>();
        VisitingValidatorSpec.visitOne(importedSpec, programList);

        /*
        * Compare parameter names
        * */
        List<String> allNamesExpected = GenUtil.getAllVDNames(testList);
        List<String> allNamesActual = GenUtil.getAllVDNames(programList);

        assertEquals(allNamesExpected.size(),allNamesActual.size(),"Size must be equal");

        for(String s : allNamesExpected){
            assertTrue(allNamesActual.contains(s), "Parameter: " + s + " was not found");
        }

        /*
         * Do matching
         * */
        LoadLua.match(programList,vdLua);

        /*
        * Compare new parameter values
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