problem = {
    geometry = {
        gridname = {
            type = "String",
            default = "Mandy.ugx"
        }
    },


    reactor_size = {
        setup_dim = {
            type = "Integer",
            default = 2,
            range = {
                min = 1,
                max = 3
            }
        },
        reactorHeight = {
            type = "Double",
            default = 15.0,
            range = {
                min = 0.5,
                max = 200
            }
        },
        reactorWidth = {
            type = "Double",
            default = 15.0,
            range = {
                min = 0.5,
                max = 200
            }
        }
    },

    operatingTemperature = {
        type = "Double",
        default = 311.15,
        tooltip = "Temperatur bleibt konstant beim GICON Modell"
    },

    reactorType = {
        type = "String",
        default = "Downflow",
        range = { 
            values = {"Downflow", "CSTR"} 
            },
    },

    thermodynModell = {
        type = "Boolean",
        default = "true"
    },

    -- Numerical SPECS
    sim_endtime = {
        type = "Integer",
        range = { 
            min = 1, 
            max = 10000 
            },
        default = 500
    },

    inital_dt = {
        type = "Integer",
        range = { min = 1, max = 100 },
        default = 1
    },
        
    numPreRefs = {
        type = "Integer",
        range = { min = 1, max = 100 },
        default = 1
    },
        
    numRefs = {
        type = "Integer",
        range = { min = 1, max = 100 },
        default = 3
    }
}