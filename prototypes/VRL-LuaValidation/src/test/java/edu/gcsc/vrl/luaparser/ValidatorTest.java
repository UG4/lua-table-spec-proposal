package edu.gcsc.vrl.luaparser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void validate() throws Exception{
        /*
        * expected data
        * */
        List<String> namesOfCycleParams = new ArrayList<>();
        namesOfCycleParams.add("1");
        namesOfCycleParams.add("subParam1");

        /*
        * Setup test, load validationtest05.lua
        * */
        Validator vali = new Validator("/validationtest05.lua",true);
        vali.visiting(ValueDataFX::new);
        List<ErrorMessage> err =  vali.validate();

        /*
        * Compare names of cycle-params
        * */
        for(int i = 0; i < namesOfCycleParams.size(); i++){
            assertTrue(namesOfCycleParams.contains(err.get(i).getParamName()), "wrong cycle node");
        }

    }

    @Test
    void validateValidation() throws Exception{
        /*
         * Creating expected data.
         * HashMap represents name of parameter and his isValidationValid()-property
         * -> validation causing changed property
         * */
        Map<String, Boolean> testData = new HashMap<>();
        testData.put("1",false);
        testData.put("subParam1",true);

        /*
         * Setup test, load valitest.lua
         * */
        Validator vali = new Validator("/valitest.lua",true);
        vali.visiting(ValueDataFX::new);
        List<ErrorMessage> err =  vali.validateValidation();

        /*
        * Compare validation-boolean & size of lists
        * */
        List<ValueData> actualVals = GenUtil.getAllDependingValidateValues(vali.getData());

        assertEquals(actualVals.size(),testData.size());

        for(ValueData v :  actualVals){
            assertTrue(testData.containsKey(v.getValName()));
            assertTrue(testData.get(v.getValName()).booleanValue() == v.isValidationValid());
        }
    }

    @Test
    void validateVisibility() throws Exception{
        /*
         * Create expected data.
         * HashMap represents name of parameter and his isVisible()-property
         * -> validation causing changed property
         * */
        Map<String, Boolean> testData = new HashMap<>();
        testData.put("1",true);
        testData.put("subParam4", false);

        /*
         * Setup test, load valitest.lua
         * */
        Validator vali = new Validator("/valitest.lua",true);
        vali.visiting(ValueDataFX::new);
        List<ErrorMessage> err =  vali.visibleValidation();

        /*
         * Compare visibility-boolean & size of lists
         * */
        List<ValueData> actualVals = DependingUtil.getAllDependingVisibleValues(vali.getData());

        assertEquals(actualVals.size(),testData.size());

        for(ValueData v :  actualVals){
            assertTrue(testData.containsKey(v.getValName()));
            assertTrue(testData.get(v.getValName()).booleanValue() == v.isVisible());
        }

    }
}