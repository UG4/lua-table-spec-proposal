problem = {
    valueOne = {
        {
            type = "Integer",

            validation = {
                dependsOn = {"./subParam1/"},
                eval = function() return false end
            },
            visibility = {
                dependsOn = {"./subParam2/"},
                eval = function() return true end
            }
        },
        {
            subParam1 = {
                type = "Double",
                default = 1.5,

                validation = {
                    dependsOn = {"./subParam3/"},
                    eval = function(subParam2,subParam3) return subParam2 < subParam3 end
                }
            },
            subParam2 = {
                type = "String",
                default = "Hallo"
            },
            {
                {
                    subParam3 = {
                        type = "Double",
                        default = 3.0
                        }
                },
                {
                    subParam4 = {
                        type = "Double",
                        default = 4.5,

                        visibility = {
                            dependsOn = {"./subParam2/"},
                            eval = function() return false end
                        }
                    }
                }
            }
        }
    }
}