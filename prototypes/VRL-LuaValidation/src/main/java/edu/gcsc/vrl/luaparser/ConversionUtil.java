package edu.gcsc.vrl.luaparser;

import java.util.ArrayList;
import java.util.List;

public final class ConversionUtil {
    public ConversionUtil(){ throw new AssertionError(); }

    // In dieser Klasse werden Strings zu Listen von verschiedenen Datentypen umgewandelt und umgekehrt.
    // Wird vorallem für die richtige Kommunikation zwischen GUI und Datenmodell benötigt.
    public static List<Double> fromStringToDoubleList(String s) {
        char[] cS = s.toCharArray();
        List<Double> doubles = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cS.length; i++) {
            if (Character.toString(cS[i]).equals(",")) {
                try {
                    double x = Double.parseDouble(sb.toString());
                    doubles.add(x);
                    sb = new StringBuilder();
                } catch (Exception e) {
                }
            } else if (i == cS.length - 1) {
                try {
                    sb.append(cS[i]);
                    double x = Double.parseDouble(sb.toString());
                    doubles.add(x);
                    sb = new StringBuilder();
                } catch (Exception e) {
                }
            } else {
                sb.append(cS[i]);
            }
        }

        System.out.println("___");
        for (double a : doubles) {
            System.out.println("TEST: " + a);
        }
        System.out.println("___");
        return doubles;
    }

    public static List<Integer> fromStringToIntegerList(String s) {
        char[] cS = s.toCharArray();
        List<Integer> integers = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cS.length; i++) {
            if (Character.toString(cS[i]).equals(",")) {
                try {
                    int x = Integer.parseInt(sb.toString());
                    integers.add(x);
                    sb = new StringBuilder();
                } catch (Exception e) {
                }
            } else if (i == cS.length - 1) {
                try {
                    sb.append(cS[i]);
                    int x = Integer.parseInt(sb.toString());
                    integers.add(x);
                    sb = new StringBuilder();
                } catch (Exception e) {
                }
            } else {
                sb.append(cS[i]);
            }
        }

        System.out.println("___");
        for (int a : integers) {
            System.out.println("TEST: " + a);
        }
        System.out.println("___");
        return integers;
    }

    public static List<Boolean> fromStringToBooleanList(String s){
        char[] cs = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        List<Boolean> bools = new ArrayList<>();

        for(int i = 0; i < cs.length;i++){
            if(Character.toString(cs[i]).equals(",")){
                try{
                    boolean x = Boolean.valueOf(sb.toString());
                    bools.add(x);
                    sb = new StringBuilder();
                } catch(Exception e){}
            } else if(i == cs.length-1){
                try{
                    sb.append(cs[i]);
                    boolean x = Boolean.valueOf(sb.toString());
                    bools.add(x);
                    sb = new StringBuilder();
                } catch(Exception e){}
            } else {
                sb.append(cs[i]);
            }
        }

        System.out.println("___");
        for(boolean test : bools){
            System.out.println("TEST: "+test);
        }
        System.out.println("___");
        return bools;
    }

    public static List<String> fromStringtoStringList(String s){
        char[] cs = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        List<String> strings = new ArrayList<>();

        for(int i = 0; i < cs.length;i++){
            if(Character.toString(cs[i]).equals(",")){
                try{
                    String x = sb.toString();
                    if(!x.isEmpty()) {
                        strings.add(x);
                    }
                    sb = new StringBuilder();
                } catch(Exception e){}
            } else if(i == cs.length-1){
                try{
                    sb.append(cs[i]);
                    String x = sb.toString();
                    if(!x.isEmpty()) {
                        strings.add(x);
                    }
                    sb = new StringBuilder();
                } catch(Exception e){}
            } else {
                sb.append(cs[i]);
            }
        }
        System.out.println("___");
        for(String s1 : strings){
            System.out.println("TEST: " +s1);
        }
        System.out.println("___");
        return strings;
    }

    public static String fromDoubleListToString(List<Double> dl){
        StringBuilder sb = new StringBuilder();
        for(double d : dl){
            sb.append(d).append(",");
        }
        sb.setLength(sb.length()-1);

        return sb.toString();
    }

    public static String fromIntegerListToString(List<Integer> il){
        StringBuilder sb = new StringBuilder();
        for(int d : il){
            sb.append(d).append(",");
        }
        sb.setLength(sb.length()-1);

        return sb.toString();
    }

    public static String fromBooleanListToString(List<Boolean> bl){
        StringBuilder sb = new StringBuilder();
        for(boolean b : bl){
            sb.append(b).append(",");
        }
        sb.setLength(sb.length()-1);

        return sb.toString();
    }

    public static String fromStringListToString(List<String> sl){
        StringBuilder sb = new StringBuilder();
        for(String s : sl){
            sb.append(s).append(",");
        }
        sb.setLength(sb.length()-1);

        return sb.toString();
    }

    public static String fromStringListToStringQuoteMarks(List<String> sl){
        StringBuilder sb = new StringBuilder();
        for(String s : sl){
            sb.append("\""+s+"\"").append(",");
        }
        sb.setLength(sb.length()-1);

        return sb.toString();
    }
}
