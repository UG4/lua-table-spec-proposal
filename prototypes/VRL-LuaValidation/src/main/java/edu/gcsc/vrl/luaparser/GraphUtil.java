package edu.gcsc.vrl.luaparser;

import java.util.ArrayList;
import java.util.List;

public final class GraphUtil {
    public GraphUtil(){ throw new AssertionError(); }

    public static List<ValueData> checkForCycles(List<ValueData> runtimeData,List<ValueData> allValues){
        ValueData[][] dependings = new ValueData[allValues.size()][allValues.size()];

        // Matrix mit den Abhängigkeiten für jeden Parameter erstellen
        for (int i = 0; i < allValues.size(); i++) {
            List<ValueData> vals = GenUtil.validateAValue(allValues.get(i), runtimeData);
            for (int j = 0; j < vals.size(); j++) {
                dependings[i][j] = vals.get(j);
            }
        }

        int[][] dep = new int[allValues.size()][allValues.size()];


        // Erstellen der Adjazenzmatrix
        StringBuilder sh = new StringBuilder();
        for (int i = 0; i < allValues.size(); i++) {
            for (int j = 0; j < allValues.size(); j++) {
                if (GenUtil.containsVD(dependings[i], allValues.get(j))) {
                    dep[i][j] = 1;
                    sh.append(1);
                } else {
                    dep[i][j] = 0;
                    sh.append(0);
                }

            }
            sh.append("\n");
        }

        // Testzwecke
        //System.out.println(sh.toString());



        // Zyklen werden herausgefunden und in Liste abgespeichert
        List<ValueData> cycleNodes = new ArrayList<>();

        for (int x = 0; x < allValues.size(); x++) {
            int[] marking = new int[allValues.size()];
            for (int i = 0; i < allValues.size(); i++) {
                marking[i] = 0;
            }

            boolean cycle = GenUtil.cycle(x, marking, dep, allValues.size());
            if(cycle){
                cycleNodes.add(allValues.get(x));
            }
        }

        return cycleNodes;
    }
}
