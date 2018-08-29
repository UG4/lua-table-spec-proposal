package edu.gcsc.vrl.luaparser;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableArray;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
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
        Validator v = new Validator("/validationtest03.lua");
        v.visiting();
        v.loadUI();
    }
}
