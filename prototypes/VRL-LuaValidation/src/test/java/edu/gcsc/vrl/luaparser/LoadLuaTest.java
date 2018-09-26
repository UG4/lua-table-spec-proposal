package edu.gcsc.vrl.luaparser;

import org.junit.jupiter.api.Test;
import com.google.common.io.ByteStreams;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoadLuaTest {

    @Test
    void visitingLuaCode() throws Exception {
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
         * Aufruf
         * */
        byte[] code2Data = ByteStreams.toByteArray(Main.class.getResourceAsStream("/test.lua"));
        String code = new String(code2Data, "UTF-8");

        Group importedCode = Group.toGroup(code);
        List<ValueData> vdActual = new ArrayList<>();
        LoadLua.visitingLuaCode(importedCode,vdActual);

        /*
        * Vergleich der Namen
        * */
        List<String> allNamesExpected =  GenUtil.getAllVDNames(vdExpected);
        List<String> allNamesActual = GenUtil.getAllVDNames(vdActual);

        assertEquals(allNamesExpected.size(),allNamesActual.size(),"Size must be equal");

        for(String s : allNamesExpected){
            assertTrue(allNamesActual.contains(s), "Parameter: " + s + " was not found");
        }

        /*
        * Vergleich der Werte
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
    void match() {
    }
}