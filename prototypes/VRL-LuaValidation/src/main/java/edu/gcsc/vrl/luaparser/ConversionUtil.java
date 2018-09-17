package edu.gcsc.vrl.luaparser;

import java.util.ArrayList;
import java.util.List;

public final class ConversionUtil {
    public ConversionUtil(){ throw new AssertionError(); }

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
        return bools;
    }

    public static List<String> fromStringStringList(String s){
        char[] cs = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        List<String> strings = new ArrayList<>();

        for(int i = 0; i < cs.length;i++){
            if(Character.toString(cs[i]).equals(",")){
                try{
                    String x = sb.toString();
                    strings.add(x);
                    sb = new StringBuilder();
                } catch(Exception e){}
            } else if(i == cs.length-1){
                try{
                    sb.append(cs[i]);
                    String x = sb.toString();
                    strings.add(x);
                    sb = new StringBuilder();
                } catch(Exception e){}
            } else {
                sb.append(cs[i]);
            }
        }
        return strings;
    }
}
