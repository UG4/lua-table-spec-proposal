package edu.gcsc.vrl.luaparser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SampleFileTest {

    @Test
    void validateSampleFile() throws Exception{
        /*
        * Set up the test. Load lua-File and validate it.
        * Save the ErrorMessages for comparing with the expected ones
        * */
        Validator validator = new Validator("/sample_validation_test.lua",true);
        validator.visiting(ValueDataFX::new);
        /*
        * Load .lua-File with specific parameters
        * */
        List<ValueData> data = new ArrayList<>();
        Group loadedLua = LoadLua.parseLuaFile("/sample_file.lua", true);

        LoadLua.visitingLuaCode(loadedLua,data);
        LoadLua.match(validator.getData(),data);

        /*
        * Validate the file and check for errors
        * */
        List<ErrorMessage> errors = validator.validate();


        /*
        * Compare the generated data with the expected
        * */

        //value reactorType
        boolean reactorName = false;
        boolean reactorLvl = false;
        boolean reactorMsg = false;

        for(ErrorMessage e : errors){
            if(e.getParamName().equals("reactorType")){
                reactorName = true;
                if(e.getErrorLevel() == 0){
                    reactorLvl = true;
                }
                if(e.getMsg().equals("Value: " + "reactorType" + " not in range!")){
                    reactorMsg = true;
                }
            }
        }
        assertTrue(reactorName);
        assertTrue(reactorLvl);
        assertTrue(reactorMsg);

        //value sim_endtime
        boolean simName = false;
        boolean simLvl = false;
        boolean simMsg = false;

        for(ErrorMessage e : errors){
            if(e.getParamName().equals("sim_endtime")){
                simName = true;
                if(e.getErrorLevel() == 0){
                    simLvl = true;
                }
                if(e.getMsg().equals("Value: " + "sim_endtime" + " not in range!")){
                    simMsg = true;
                }
            }
        }
        assertTrue(simName);
        assertTrue(simLvl);
        assertTrue(simMsg);

    }
}
