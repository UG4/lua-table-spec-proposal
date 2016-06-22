-- # why specifications?
--
-- specification solves two main probles:
--
-- 1. validation
-- 2. ui generation
--
-- the specification below allows tables of the form:
--
-- problem = {
--    valueOne = 2.0,
--    valueTwo = 1.5
-- }
--
-- The following table is invalid:
--
-- problem = {
--    valueOne = 2.0,
--    myValue = 1.5  <-- myValue is not specified and therefore invalid
-- }
--
-- A structurally correct table with invalid value is listed below:
--
-- problem = {
--    valueOne = 1.3,
--    valueTwo = 1.5 -- -> valueTwo.validation = false
-- }
--
--
-- finally, the specification
problem = {

  -- validation for problem.valueOne
  valueOne = {
    -- data type (Integer, Double, String, Boolean, Function, Array of I, D, S, B, F)
    -- TODO which data types do we need? Are the listed ones enough?
    type     = "Double",
    -- default value
    default  = 123.4,
    -- style (only relevant for ui)
    style    = "default",
    -- tooltip (relevant for ui and potentially commandline, e.g., --help)
    tooltip  = "...",

    -- supported value range
    range = {
      min = 1,
      max = 10
    },

    -- validation
    validation = {
      -- validation can depend on other values
      dependsOn = {},
      -- validation function
      eval      = function(v,range) return v >= range.min and v <= range.max end
    },

    --value visibility (only relevant for ui)
    visibility = {
      -- just like validation, visibility can depend on other values
      dependsOn = {},
      -- function that controls visibility
      -- (in this case the value is always visible)
      eval      = function() return true end
    },
  },

  -- validation for valueTwo
  valueTwo = {
    -- data type (Integer, Double, String, Boolean, Function)
    type     = "Double",
    -- default value
    default  = 1.5,
    -- style requests selection, e.g., drop-down (only relevant for ui)
    style    = "selection",
    -- tooltip (relevant for ui and potentially commandline, e.g., --help)
    tooltip  = "...",

    -- supported value range:
    -- in addition to min, max, ranges can also be explicitly specified
    -- which is useful for drop-down
    -- TODO think about necessary range specifications (multiple intervals etc.)
    range    = {
      values = {0.5,1.0,1.5,2.0,2.5,3.0}
    },

    -- validation
    validation = {
      -- whether this value is valid, depends on valueOne
      dependsOn = {"problem.valueOne"},
      -- validation function:
      -- arguments are: function(this value, range of this value, other value)
      -- TODO check whether thatis flexible enough
      eval      = function(v, range, other1) return v < other1 end
    },

    --value visibility (only relevant for ui)
    visibility = {
      -- just like validation, visibility can depend on other values
      dependsOn = {},
      -- function that controls visibility
      -- (in this case the value is always visible)
      eval      = function() return true end
    }
  }
}
