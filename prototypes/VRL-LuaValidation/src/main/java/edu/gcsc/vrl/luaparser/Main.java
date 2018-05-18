package edu.gcsc.vrl.luaparser;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import javafx.application.Application;
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

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("mainwindow.fxml"));
        TitledPane page = (TitledPane) loader.load();
        Scene scene = new Scene(page);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Test program.
     *
     * @param args the command line arguments
     *
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        launch(args);

        // load lua code from resources
        byte[] code2Data = ByteStreams.toByteArray(
                Main.class.getResourceAsStream("/validationtest01.lua"));
        String code = new String(code2Data, "UTF-8");

        // convert code to group
        Group g = Group.toGroup(code);

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
        visit(g);
        ExtractionHelper.printElements();
    }

    /**
     * Helper function for output indentation.
     *
     * @param n number of indents
     * @return indentation string
     */
    private static String indent(int n) {
        String s = "  ";
        String result = "";

        for (int i = 0; i < n; i++) {
            result += s;
        }

        return result;
    }

    /**
     * Visits the specified entry and all of its sub-entries.
     *
     * @param e entry to visit
     */
    public static void visit(Entry e) {
        String indent;
        indent = indent(e.distanceToRoot());
        if (e instanceof Group) {
            System.out.println(indent + e.getName() + " = {");
            visit((Group) e);
            System.out.println(indent + "}");
        } else if (e instanceof Value) {

            System.out.println(indent + e.getName()
                    + " = " + ((Value) e).getValueAsString());

            Value v = (Value) e;

            if (v.isFunction() && "eval".equals(v.getName())) {
                try {
                    // invoke methods without arguments
                    System.out.println(indent + "eval: "
                            + v.asFunction().eval().getValueAsString());
                } catch (Exception ex) {
                    System.out.println(
                            indent + "-> ERROR: cannot call f!");
                }
            }

        } else {
            System.out.println(indent + "unknown type: " + e);
        }
    }

    /**
     * Visits the specified group and all of its sub-elements.
     *
     * @param g group to visit
     */
    private static void visit(Group g) {
        String indent = indent(g.distanceToRoot() + 1);
        String nameOfVal = "";
        //System.out.println(indent + "#entries: " + g.getEntries().size());

        for (Entry e : g.getEntries()) {
            ExtractionHelper.visitE(e);
        }
    }
}
