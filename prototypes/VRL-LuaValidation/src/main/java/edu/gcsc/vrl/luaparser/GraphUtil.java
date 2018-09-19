package edu.gcsc.vrl.luaparser;

import java.util.ArrayList;
import java.util.List;

public final class GraphUtil {
    public GraphUtil(){ throw new AssertionError(); }
    /*
    * Die folgenden Funktionen sind dazu da, Zyklen in den Parametern zu erkennen.
    * */
    public static List<ValueData> checkForCycles(List<ValueData> runtimeData,List<ValueData> allValues, boolean valivisi){
        ValueData[][] dependings = new ValueData[allValues.size()][allValues.size()];


        // Matrix mit den Abhängigkeiten für jeden Parameter erstellen
        for (int i = 0; i < allValues.size(); i++) {
            List<ValueData> vals;
            if(valivisi){
                 vals = GenUtil.validateAValue(allValues.get(i), runtimeData);
                for (int j = 0; j < vals.size(); j++) {
                    dependings[i][j] = vals.get(j);
                }
            } else {
                vals = DependingUtil.validateAValue(allValues.get(i),runtimeData);
                for (int j = 0; j < vals.size(); j++) {
                    dependings[i][j] = vals.get(j);
                }
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

            boolean cycle = cycle(x, marking, dep, allValues.size());
            if(cycle){
                cycleNodes.add(allValues.get(x));
            }
        }

        return cycleNodes;
    }

    // Erweiterte Tiefensuche mit Zyklenerkennung
    // 0 = noch nicht bearbeitet; 1 = in Bearbeitung; 2 = bereits bearbeitet
    private static boolean cycle(int node, int[] markierung,int[][] adjazenz, int len){
        int[] initCoord = getCoor(node,len);
        int x = initCoord[0];
        int y = initCoord[1];
        boolean cyc = false;

        if(markierung[node] == 1){
            cyc = true;
        } else if(markierung[node] == 0){
            markierung[node] = 1;
            for(int i = 0; i < len; i++){
                if(adjazenz[node][i] == 1) {
                    cyc = cycle(i,markierung,adjazenz,len);
                    markierung[node] = 2;
                }
            }
        }
        return cyc;
    }

    // Hilfsfunktion, um für einen bestimmten Knoten die Koordinaten in der Adjazenzmatrix heraus zu finden
    private static int[] getCoor(int nodeNum, int len){
        int x = nodeNum%len;
        int y = nodeNum/len;
        int[] xy = {x,y};
        return xy;
    }
}
