package edu.gcsc.vrl.luaparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{

    }

    /**
     * Test program.
     *
     * @param args the command line arguments
     *
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // visit group 
        // TODO do something meaningful with it. This just proves that the 
        //      parsing, conversion and invocation works.
        //
        //      - validate the validation file (no pun intended)
        //      - validate actual parameter sets
        //      - automatically generate an api that implements validation checks
        //        -- needs clarification: decide whether we use run-time or
        //           compile-time metaprogramming 
        //      - generate vrl components swing/javafx)


        Validator v = new Validator("/validationtest02.lua");
        //v.visiting();
        //v.loadUI();


        //List<ValueData> test = v.getData();
        /*ValueData vd = test.get(test.size()-2).getParam("2");
        //v.printTree();
        ValueData testNode = vd.xpath(".\\.\\1\\1\\");

        if(testNode != null){
            print("valname " + testNode.getValName().get());
            if(testNode.getActData() != null) {
                if(testNode.getActData().getValue() != null) {
                    print("val " + testNode.getActData().getValue());
                }
            }
        } else {
            print("Test");
        }*/
        Group g = LoadLua.parseLuaFile("/test.lua");
        List<ValueData> li = new ArrayList();
        LoadLua.visitingLuaCode(g,li);
        for(ValueData vd : li){
            //System.out.println(vd.getValName().get());
            if(vd.getActData() != null && vd.getActData().getValue() != null){
                System.out.println(vd.getValName().get());
                System.out.println(vd.getActData().getValue().toString());
            }
            if(vd.getOptions() != null){
                for(ValueData a : vd.getOptions()){
                    printOpt(a);
                }
            }
        }
    }
    public static void print(Object str){
        System.out.println(String.valueOf(str));
    }

    private static void printOpt(ValueData v){
        //System.out.println(v.getValName().get()+"!!");
        if(v.getActData() != null && v.getActData().getValue() != null){
            //System.out.println("TEST");
            System.out.println(v.getValName().get());
            System.out.println(v.getActData().getValue().toString());
        }
        if(v.getOptions() != null){
            for(ValueData a : v.getOptions()){
                printOpt(a);
            }
        }
    }
}
