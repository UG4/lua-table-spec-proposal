package edu.gcsc.vrl.luaparser;

import com.google.common.io.ByteStreams;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExportLuaTest {

    @Test
    void doExport() throws Exception{
        /*
         * Parse file and create tree data
         * */
        byte[] code2Data = ByteStreams.toByteArray(Main.class.getResourceAsStream("/validationtest04.lua"));
        String code = new String(code2Data, "UTF-8");
        Group importedCode = Group.toGroup(code);

        List<ValueData> programList = new ArrayList<>();
        VisitingValidatorSpec.visitOne(importedCode,programList,ValueDataFX::new);

        /*
        * Imitating a user selection on GUI.
        * Selecting Option 2 from valueOne
        * */
        ValueData v = programList.get(0);
        v.getParam("2").setSelectedNew(true);
        /*
        * Do export-string with ExportLua.doExport()
        * */
        String stringToTest = ExportLua.doExport(programList,"validationtest04.lua");

        /*
        * Do expected string
        * */
        String expectedString = "--validation file: validationtest04.lua\nproblem = {\n  valueOne = {\n    subParam1 = \"lala\",\n    subParam2 = \"hshs\"\n  },\n  valueTwo = \"lala\"\n}";

        /*
        * Compare both strings
        * */
        assertTrue(stringToTest.equals(expectedString));

    }
}