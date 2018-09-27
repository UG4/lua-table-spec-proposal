package edu.gcsc.vrl.luaparser;

import java.util.ArrayList;
import java.util.List;

/**
* This class provides methods, which can operate on graph-like data structures
* */
public final class GraphUtil {
    public GraphUtil(){ throw new AssertionError(); }
    /**
    * Detects a cycle in the dependencies of all parameters
    *
    * @param runtimeData List of <code>ValueData</code>-objects from validationspec
    * @param allValues List of <code>ValueData</code>-object, which are parameters
    * @param valivisi decides which property will be checked
    * @return List<ValueData> contains all parameters with a cycle
    * */
    public static List<ValueData> checkForCycles(List<ValueData> runtimeData,List<ValueData> allValues, boolean valivisi){
        ValueData[][] dependings = new ValueData[allValues.size()][allValues.size()];


        // Creates matrix with dependencies for each parameter
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


        // Creates adjacency matrix
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

        // for testing purposes only
        //System.out.println(sh.toString());



        // Detects and saves cycles
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

    /**
    * Modified Depth-first search to detect cycles in graphs
    * 0 = not processed; 1 = currently processing; 2 = already done
    *
    * @param node number of node
    * @param markierung list with markings
    * @param adjazenz adjacency matrix
    * @param len length of column/row
    * @return <code>true</code> or <code>false</code>, depends on cycle in graph
    *
    * */
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

    /**
    * Helping method to get coordinates in a matrix
    *
    * @param nodeNum number of node
    * @param len length of column/row
    * @return int[] first entry is x, second is y
    * */
    private static int[] getCoor(int nodeNum, int len){
        int x = nodeNum%len;
        int y = nodeNum/len;
        int[] xy = {x,y};
        return xy;
    }
}
