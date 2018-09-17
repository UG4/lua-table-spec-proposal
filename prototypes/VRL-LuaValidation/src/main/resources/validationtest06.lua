problem = {
    valueOne = {
        {
            type = "Integer"
        },
        {
			mySubGroup1 = {
				subParam1 = {
					type = "Double[]",
					default = {1.5,2.0}
				},
				subParam2 = {
					type = "String",
					default = "Hallo"
				},
				subParam3 = {
				    type = "String[]",
                    default = {"Hallo","Hallo2"}
				},
				subParam4 = {
				    type = "Boolean[]",
				    default = {"true","false","true"}
				},
				subParam5 = {
				    type = "String",
				    style = "load-file-dialog"
				},
				subParam6 = {
				    type = "String",
				    style = "save-file-dialog"
				}
			}
        }
    }
}