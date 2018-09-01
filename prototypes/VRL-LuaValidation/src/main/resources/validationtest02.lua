-- the specification below allows tables of the form:
--
-- problem = {
--    myGroup = {
--        myVal = "I am a string",
--        mySubGroup = {
--            mySubVal1 = 7,
--            mySubVal2 = 3
--        }
--    }
-- }
--
--
-- specification with nested groups
problem = {
  myGroup = {
        
    -- specification for problem.myGroup.myVal
    myVal = {
        type = "String"
    },

    mySubGroup = {
        -- specification for problem.myGroup.mySubGroup.mySubVal1
        mySubVal1 = {
            type = "Integer"
        },
        
        -- specification for problem.myGroup.mySubGroup.mySubVal2
        mySubVal2 = {
            type = "Integer"
        }
    },

    myVal2 = {
        type = "String"
    }
  }
}
