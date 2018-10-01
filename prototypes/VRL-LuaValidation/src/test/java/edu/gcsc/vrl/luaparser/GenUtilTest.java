package edu.gcsc.vrl.luaparser;

import com.google.common.io.ByteStreams;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenUtilTest {

    @Test
    void doXPathOne() throws Exception{
        /*
         * Erstellen des zu testenden ValueData-Baums
         * */
        byte[] code2Data = ByteStreams.toByteArray(Main.class.getResourceAsStream("/validationtest04.lua"));
        String code = new String(code2Data, "UTF-8");
        Group importedCode = Group.toGroup(code);

        List<ValueData> programList = new ArrayList<>();
        VisitingValidatorSpec.visitOne(importedCode,programList);

        /*
        * Ausführen von XPath-Befehlen und überprüfen der Ergebnisse
        * */
        ValueData test1 = GenUtil.doXPath(programList,"/problem/valueOne/");
        assertEquals("valueOne",test1.getValName().get());

        ValueData test2 = GenUtil.doXPath(programList,"./valueOne/");
        if(test2 != null) {
            assertEquals("valueOne", test2.getValName().get());
        } else { System.out.println("Failed");}

        ValueData test3 = GenUtil.doXPath(programList,"./2/subParam2/");
        assertEquals("subParam2",test3.getValName().get());

        ValueData test4 = GenUtil.doXPath(programList,"/problem/valueOne/2/subParam2/");
        assertEquals("subParam2",test4.getValName().get());

        ValueData test5 = GenUtil.doXPath(programList,"./subParam2/");
        assertNotEquals("subParam1", test5.getValName().get());
    }

    @Test
    void doXPathTwo() throws Exception{
        /*
         * Erstellen des zu testenden ValueData-Baums
         * */
        byte[] code2Data = ByteStreams.toByteArray(Main.class.getResourceAsStream("/validationtest05.lua"));
        String code = new String(code2Data, "UTF-8");
        Group importedCode = Group.toGroup(code);

        List<ValueData> programList = new ArrayList<>();
        VisitingValidatorSpec.visitOne(importedCode,programList);

        /*
         * Ausführen von XPath-Befehlen und überprüfen der Ergebnisse
         * */
        ValueData test1 = GenUtil.doXPath(programList,"/problem/valueOne/2/1/1/subParam3/");
        assertEquals("subParam3",test1.getValName().get());

        ValueData test2 = GenUtil.doXPath(programList,"./2/subParam1/");
        if(test2 != null) {
            assertEquals("subParam1", test2.getValName().get());
        } else {
            fail("Failed");
        }
    }

    @Test
    void valueIsInRange(){
        /**
         * Setting up the test cases.
         * */
        ValueData v1 = new ValueData("valueOne");
        v1.setType("Integer");
        ActualDataValue adv = new ActualDataValue();
        adv.setType("Integer");
        adv.setValue(5);
        v1.setActData(adv);
        v1.setRange_min(0);
        v1.setRange_max(10);

        ValueData v2 = new ValueData("valueTwo");
        v2.setType("Double");
        ActualDataValue adv2 = new ActualDataValue();
        adv2.setType("Double");
        adv2.setValue(4.5);
        v2.setActData(adv2);
        v2.setRange_min(1.0);
        v2.setRange_max(3.9);

        ValueData v3 = new ValueData("valueThree");
        v3.setType("Integer[]");
        ActualDataValue adv3 = new ActualDataValue();
        adv3.setType("Integer[]");
        adv3.setValue("2, 3, 6");
        v3.setActData(adv3);
        v3.setRange_min(3);
        v3.setRange_max(10);

        ValueData v4 = new ValueData("valueFour");
        v4.setType("Double[]");
        ActualDataValue adv4 = new ActualDataValue();
        adv4.setType("Double[]");
        adv4.setValue("1.3, 3.76, 4.5");
        v4.setActData(adv4);
        v4.setRange_min(1.3);
        v4.setRange_max(4.5);

        /**
         * Compare results from test run with expected results
         * */
        assertTrue(GenUtil.valueIsInRange(v1));
        assertFalse(GenUtil.valueIsInRange(v2));
        assertFalse(GenUtil.valueIsInRange(v3));
        assertTrue(GenUtil.valueIsInRange(v4));
    }
}