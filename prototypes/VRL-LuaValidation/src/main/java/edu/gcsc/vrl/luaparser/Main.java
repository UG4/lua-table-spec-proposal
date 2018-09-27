package edu.gcsc.vrl.luaparser;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
* Main class, that starts the JavaFX-Application und loads user settings
* */
public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("mainwindow.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Parameter Eingabe");
        BorderPane page = (BorderPane) loader.load();
        Scene scene = new Scene(page);
        stage.setScene(scene);
        TreeViewController lCon = loader.<TreeViewController>getController();
        lCon.setLoadValidation();
        lCon.setExportLua();
        lCon.setPrefs();
        lCon.setLoadLua();
        lCon.setValidateLua();

        stage.show();
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
        PreferencesUtil.loadPreferences();
        launch(args);

    }
}
