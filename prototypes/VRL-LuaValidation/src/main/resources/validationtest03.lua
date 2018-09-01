problem = {
  valueOne = {
    {
        type = "Integer"
    },
    {
        subParam1 = {
        	type = "Double",

        	default = 1.5,

        	style    = "selection",

        	range    = {
                  values = {0.5,1.0,1.5,2.0,2.5,3.0}
                }
        },
        subParam2 = {
        	type = "String"
        },
        {
            {
                type = "Double",
                default = 1.5
            },
            {
                type = "Integer",
                default = 2
            },
            {
                {
                    type = "String"
                },
                {
                    type = "Integer",
                    default = 40
                }
            }
        }
    }
  },
  valueTwo = {
    type = "String"
  }
}