package edu.gcsc.vrl.luaparser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void validate() throws Exception{
        /*
        * Test data
        * */
        List<String> namesOfCycleParams = new ArrayList<>();
        namesOfCycleParams.add("1");
        namesOfCycleParams.add("subParam1");

        /*
        * Setup test, load validationtest05.lua
        * */
        Validator vali = new Validator("/validationtest05.lua",true);
        vali.visiting();
        List<ErrorMessage> err =  vali.validate();

        /*
        * Compare names of cycle-params
        * */
        for(int i = 0; i < namesOfCycleParams.size(); i++){
            assertTrue(namesOfCycleParams.contains(err.get(i).getParamName()), "wrong cycle node");
        }

    }
}