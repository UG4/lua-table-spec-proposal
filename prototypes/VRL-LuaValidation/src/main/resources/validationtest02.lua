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
        type = "String",
        default = "zz"
    },

    mySubGroup1 = {
        -- specification for problem.myGroup.mySubGroup.mySubVal1
        mySubVal1 = {
            type = "Integer",
            default = 10,

            validation = {
                dependsOn = {"./mySubGroup1/mySubVal2/"}
            }
        },
        
        -- specification for problem.myGroup.mySubGroup.mySubVal2
        mySubVal2 = {
            type = "Integer",
            default = 10,

            validation = {
                dependsOn = {"./mySubGroup1/mySubVal1/"}
            }
        }
    },

    mySubGroup2 = {
            -- specification for problem.myGroup.mySubGroup.mySubVal1
            mySubVal11 = {
                type = "Integer",
                default = 10
            },

            -- specification for problem.myGroup.mySubGroup.mySubVal2
            mySubVal22 = {
                type = "Integer",
                default = 10
            }
        },

    myVal2 = {
        type = "String",
        default = "xx"
    }
  }
}
