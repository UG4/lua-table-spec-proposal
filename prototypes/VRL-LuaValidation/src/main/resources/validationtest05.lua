problem = {
    valueOne = {
        {
            type = "Integer",

            validation = {
                dependsOn = {"./subParam1/"}
            }
        },
        {
            subParam1 = {
                type = "Double",
                default = 1.5,

                validation = {
                    dependsOn = {"./valueOne/1/"}
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
                        default = 4.5
                        }
                }
            }
        }
    }
}