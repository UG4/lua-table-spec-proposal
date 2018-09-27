package edu.gcsc.vrl.luaparser;

import java.util.ArrayList;
import java.util.List;
/*
* This class provides methods for formatting values and strings.
* Often needed in communication between UI and data model.
*
* */
public final class ConversionUtil {
    public ConversionUtil(){ throw new AssertionError(); }

    /*
    * Transforms a string to list of doubles.
    * Uses comma as seperation character.
    * @param s String to transform
    * @return <code>List<Double></code>
    * */
    public static List<Double> fromStringToDoubleList(String s) {
        System.out.println("DOUBLE STR2: "+s);
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

    /*
     * Transforms a string to list of integer.
     * Uses comma as seperation character.
     * @param s String to transform
     * @return <code>List<integer></code>
     * */
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

    /*
     * Transforms a string to list of booleans.
     * Uses comma as seperation character.
     * @param s String to transform
     * @return <code>List<Boolean></code>
     * */
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

    /*
     * Transforms a string to list of strings.
     * Uses comma as seperation character.
     * To escape comma, use \,
     * @param s String to transform
     * @return <code>List<String></code>
     * */
    public static List<String> fromStringtoStringList(String s){
        char[] cs = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        List<String> strings = new ArrayList<>();

        for(int i = 0; i < cs.length;i++){
            if(Character.toString(cs[i]).equals(",")){
                if(i > 0 && !Character.toString(cs[i-1]).equals("\\")) {
                    try {
                        String x = sb.toString();
                        if (!x.isEmpty()) {
                            strings.add(GenUtil.doString(x));
                        }
                        sb = new StringBuilder();
                    } catch (Exception e) {
                    }
                } else if(i > 0 && Character.toString(cs[i-1]).equals("\\")){
                    sb.append(",");
                }
            } else if(i == cs.length-1){
                try{
                    sb.append(cs[i]);
                    String x = sb.toString();
                    if(!x.isEmpty()) {
                        strings.add(GenUtil.doString(x));
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

    /*
    * Creates a string from a list of doubles.
    *
    * @param dl List of Doubles
    * @return String created string
    * */
    public static String fromDoubleListToString(List<Double> dl){
        StringBuilder sb = new StringBuilder();
        for(double d : dl){
            sb.append(d).append(",");
        }
        if(sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }


    /*
     * Creates a string from a list of integers.
     *
     * @param dl List of Integers
     * @return String created string
     * */
    public static String fromIntegerListToString(List<Integer> il){
        StringBuilder sb = new StringBuilder();
        for(int d : il){
            sb.append(d).append(",");
        }
        if(sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    /*
     * Creates a string from a list of booleans.
     *
     * @param dl List of Booleans
     * @return String created string
     * */
    public static String fromBooleanListToString(List<Boolean> bl){
        StringBuilder sb = new StringBuilder();
        for(boolean b : bl){
            sb.append(b).append(",");
        }
        if(sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    /*
     * Creates a string from a list of Strings.
     *
     * @param dl List of Strings
     * @return String created string
     * */
    public static String fromStringListToString(List<String> sl){
        StringBuilder sb = new StringBuilder();
        for(String s : sl){
            sb.append(s).append(",");
        }
        if(sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    /*
     * Creates a string from a list of strings.
     * Adds quote marks at the beginning and at
     * the end.
     *
     * @param dl List of trings
     * @return String created string
     * */
    public static String fromStringListToStringQuoteMarks(List<String> sl){
        StringBuilder sb = new StringBuilder();
        for(String s : sl){
            sb.append("\""+s+"\"").append(",");
        }
        if(sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }
}
