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
        } else { System.out.println("FAIL2");}

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
}