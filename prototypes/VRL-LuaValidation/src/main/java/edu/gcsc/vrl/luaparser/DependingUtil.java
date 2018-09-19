package edu.gcsc.vrl.luaparser;

import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

public final class DependingUtil {

    public DependingUtil() {
        throw new AssertionError();
    }

    public static List<ValueData> validateAValue(ValueData objectToValidate, List<ValueData> runtimeData) {
        List<ValueData> visibObjDependsOn = new ArrayList<>();


        if (objectToValidate.getVis_dependsOn() != null) {
            for (String dependsOnVal : objectToValidate.getVis_dependsOn()) {
                ValueData act = GenUtil.doXPath(runtimeData, dependsOnVal);
                if (act != null) {
                    visibObjDependsOn.add(act);
                }
            }
            // Was wird mit den Werten gemacht
        }
        return visibObjDependsOn;
    }

    // Alle Parameter mit einer dependsOn-Abhängigkeit herausfinden( bzgl. visible)
    public static List<ValueData> getAllDependingVisibleValues(List<ValueData> dataToSearch) {
        List<ValueData> dependingValues = new ArrayList<>();

        for (ValueData v : dataToSearch) {
            if (v.dependsOnVisible() && !dependingValues.contains(v)) {
                dependingValues.add(v);
            }
            if (v.getOptions() != null) {
                for (ValueData vd : v.getOptions()) {
                    searchDependingVisibleValues(dependingValues, vd);
                }
            }
        }

        return dependingValues;
    }

    // Hilfsfunktion für dependsOn-Funktion
    private static void searchDependingVisibleValues(List<ValueData> dependingValues, ValueData actObj) {
        if (actObj.dependsOnVisible() && !dependingValues.contains(actObj)) {
            dependingValues.add(actObj);
        }
        if (actObj.getOptions() != null) {
            for (ValueData v : actObj.getOptions()) {
                searchDependingVisibleValues(dependingValues, v);
            }
        }
    }

    public static boolean validateVisible(ValueData vd, List<ValueData> runtimeData) {
        // Als erstes wird überprüft, ob der Parameter eine eval-Funktion hat.
        // Wenn nicht, dann ist die Validation immer true, da ja kein 'falscher'
        // Fall eintreten kann.
        if (vd.getVis_eval() != null) {
            // Hier werden alle Parameter herausgesucht, von denen er abhängig ist
            List<ValueData> dependsOn = new ArrayList<>();
            if (vd.dependsOnVisible()) {
                dependsOn = validateAValue(vd, runtimeData);
            }
            boolean valid = true;
            // Die Parameter von denen er abhängig ist, werden validiert.
            // Erst wenn alle diese Parameter valide sind, kann der eigentliche
            // Parameter validiert werden.
            for (int i = 0; i < dependsOn.size(); i++) {
                boolean temp = validateVisible(dependsOn.get(i), runtimeData);
                if (temp == false) {
                    valid = false;
                }
            }
            // Hier wird der Parameter validiert
            if (valid) {

                List<ValueData> vals = new ArrayList<>();

                // Alle Werte der von denen der Parameter abhängig ist, werden herausgesucht
                for (ValueData v : dependsOn) {
                    vals.add(getActualData(v, runtimeData));
                }

                List<Value> valsForEval = new ArrayList<>();

                // Hier werden die Werte, die als Argument für die eval-Funktion dienen
                // in einer Liste gespeichert.
                if (vd.getActData() != null && vd.getActData().getValue() != null) {
                    System.out.println("Test " + vd.getValName().get());
                    doArgList(vd, valsForEval);
                }

                if (!vals.isEmpty() && !valsForEval.isEmpty()) {
                    doArgList(vals, valsForEval);
                }

                String resultOfEval = "";
                // Jetzt wird versucht die eval-Funktion mit den Argumenten aufzurufen
                try {
                    resultOfEval = vd.getVis_eval().asFunction().eval(valsForEval).getValueAsString();
                } catch (Exception ex) {
                    System.out.println("Cannot call f !");
                }

                boolean result = false;
                if (!resultOfEval.isEmpty()) {
                    result = Boolean.valueOf(resultOfEval);
                }

                return result;
            } else {
                // Falls einer der Parameter, von denen der eigentliche Parameter abhängig ist, nicht valide ist,
                // kann der zu überprüfende Parameter auch nicht valide sein -> return false
                return false;
            }

        } else {
            return true;
        }

    }

    private static void doArgList(List<ValueData> vData, List<Value> vals) {
        // Hier wird die List von Values erstellt, die die Argumente für die eval-Funktion enthalten.
        // WEITERE DATENTYPEN HINZUFÜGEN: ARRAYS UND FUNCTION
        for (ValueData v : vData) {
            if (v.getActData() != null && v.getActData().getValue() != null) {
                Object temp = v.getActData().getValue();
                String name = v.getValName().get();

                if (v.getActData().getType().equals("String")) {
                    LuaValue lua = LuaValue.valueOf(String.valueOf(temp));
                    Value val = new Value(lua, name);
                    vals.add(val);
                } else if (v.getActData().getType().equals("Integer")) {
                    LuaValue lua = LuaValue.valueOf(Integer.parseInt(temp.toString()));
                    Value val = new Value(lua, name);
                    vals.add(val);
                } else if (v.getActData().getType().equals("Double")) {
                    LuaValue lua = LuaValue.valueOf(Double.parseDouble(temp.toString()));
                    Value val = new Value(lua, name);
                    vals.add(val);
                } else if (v.getActData().getType().equals("Boolean")) {
                    LuaValue lua = LuaValue.valueOf(Boolean.valueOf(temp.toString()));
                    Value val = new Value(lua, name);
                    vals.add(val);
                }
            }
        }
    }

    private static void doArgList(ValueData v, List<Value> vals) {
        // Hier kann ein einzelnes Argument der Argumentenliste hinzugefügt werden
        Object temp = v.getActData().getValue();
        String name = v.getValName().get();

        if (v.getActData().getType().equals("String")) {
            LuaValue lua = LuaValue.valueOf(String.valueOf(temp));
            Value val = new Value(lua, name);
            vals.add(val);
        } else if (v.getActData().getType().equals("Integer")) {
            LuaValue lua = LuaValue.valueOf(Integer.parseInt(temp.toString()));
            Value val = new Value(lua, name);
            vals.add(val);
        } else if (v.getActData().getType().equals("Double")) {
            LuaValue lua = LuaValue.valueOf(Double.parseDouble(temp.toString()));
            Value val = new Value(lua, name);
            vals.add(val);
        } else if (v.getActData().getType().equals("Boolean")) {
            LuaValue lua = LuaValue.valueOf(Boolean.valueOf(temp.toString()));
            Value val = new Value(lua, name);
            vals.add(val);
        }

    }

    private static ValueData getActualData(ValueData vd, List<ValueData> runtimeData) {
        // Hier werden die Werte der Parameter herausgesucht, die übergeben werden sollen
        if (!vd.dependsOnVisible()) {
            // Wenn der Parameter von keinem Anderen abhängig ist, kann direkt das ValueData-Objekt
            // mit dem Value zurückgegeben werden.(dafür muss dieses ValueData-Objekt natürlich auch einen
            // Value besitzen)
            if (vd.getActData() != null && vd.getActData().getValue() != null) {
                return vd;
            } else {
                System.out.println(vd.getValName().get() + " has no Value!");
                return null;
            }
        } else {
            // Falls der Parameter abbhängig ist, müssen die Parameter von denen der zu überprüfende
            // Parameter abhängig ist, natürlich auch erst überprüft werden.
            List<ValueData> dependsOn = validateAValue(vd, runtimeData);
            boolean valid = true;

            //Falls einer der Parameter nicht valide ist, wird null zurückgegeben.
            // EVENTUELL ÄNDERN
            // Wenn alle Parameter valide sind, wird der zu überprüfende Parameter zurückgegeben.

            for (int i = 0; i < dependsOn.size(); i++) {
                boolean temp = validateVisible(dependsOn.get(i), runtimeData);
                if (temp == false) {
                    valid = false;
                }
            }
            if (valid) {
                return vd;
            }

        }
        // Temporär
        return null;
    }
}

