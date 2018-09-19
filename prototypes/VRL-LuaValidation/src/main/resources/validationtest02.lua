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
        type = "Integer",
        default = "10",
        style = "default",

        visibility = {
            dependsOn = {"./myVal2/"},

            eval = function(myVal2) return 20 < myVal2 end
        }
    },

    mySubGroup1 = {
        -- specification for problem.myGroup.mySubGroup.mySubVal1
        mySubVal1 = {
            type = "Integer",
            default = 10,

            validation = {
                dependsOn = {"./mySubGroup1/mySubVal2/"},
                eval = function(mySubVal2) return 20 > mySubVal2 end
            }
        },
        
        -- specification for problem.myGroup.mySubGroup.mySubVal2
        mySubVal2 = {
            type = "Integer",
            default = 10,

            validation = {
                dependsOn = {"./mySubGroup1/mySubVal1/"},
                eval      = function(mySubVal11) return 8 < mySubVal11 end
            }
        }
    },

    mySubGroup2 = {
            -- specification for problem.myGroup.mySubGroup.mySubVal1
            mySubVal11 = {
                type = "Integer",
                default = 10,
                validation = {
                    dependsOn = {"./mySubGroup1/mySubVal2/"}
                }
            },

            -- specification for problem.myGroup.mySubGroup.mySubVal2
            mySubVal22 = {
                type = "Integer",
                default = 10
            }
        },

    myVal2 = {
        type = "Integer",
        default = "10",

        visibility = {
            dependsOn = {"./myVal/"},
            eval = function(myVal) return 8 < myVal end
        }
    }
  }
}
