-- GICON Projekt Sample LUA File 
problem = {
	--	Specifications for REACTOR GEOMETRY
-----------------------------------------------------------------
	geometry = {
		type = "String",
		tooltip = "Name der Subsets innerhalb der Geometrie"
		grid_name = "Mandy.ugx", -- hier evtl. noch Gasabfluss adden
		subsets = {
			["reactorVolSubset"] = "Inner",
			["reactorUpperBnd"] = "Top",
			["reactorLowerBnd"] = "Bot",
			["reactorLeftBnd"] = "Side",
			["reactorRightBnd"] = "Side"
		},
		
	reactor_size = {
		type = "Double",
		default = {
		setup_dim = 2,
		reactorHeight = 15.0,
		reactorWidth = 15.0,
		},
		tooltip = "Größe des Methanreaktors und Dimension des Modells",
		range = { 	setup_dim = {min = 1, max = 3}
					reactorHeight = {min = 0.5, max = 200}
					reactorWidth = {min = 0.5, max = 200}
				},
				
		reactor_size = {
		setup_dim = 2,
		reactorHeight = 80.0,	-- Height of fermenter content subset (in dm)
		reactorWidth = 85.0,	-- Width of reactor (in dm)
		}
	},
	
	operatingTemperature = {
		type = "Double",
		default = 311.15,
		tooltip ="Temperatur bleibt konstant beim GICON Modell"
	},
	
	reactorType = {
		type = "String",
		range = { values = {"Downflow", "CSTR"} },
		default = "Downflow"
	},
	
	thermodynModell = {
		type = "String",
		range = { values = {"true", "false"} },
		default = "true"
	},
	

-- Numerical SPECS
	sim_endtime = {
		type = "Integer",
		range = { min = 1, max = 10.000 },
		default = 500
	},
	
	inital_dt = {
		type = "Integer",
		range = { min = 1, max = 100 },
		default = 1
	},
	
	numPreRefs = {
		type = "Integer",
		range = { min = 1, max 100 },
		default = 1
	},
	
	numRefs = {
		type = "Integer",
		range = { min = 1, max 100 },
		default = 3
	}