package edu.gcsc.vrl.luaparser.genluacode;

import edu.gcsc.vrl.luaparser.Group;
import edu.gcsc.vrl.luaparser.Validator;
import edu.gcsc.vrl.luaparser.ValueData;
import edu.gcsc.vrl.luaparser.VisitingValidatorSpec;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ValidationGenerator {

    private ValidationGenerator() {
        throw new AssertionError("Don't instantiate me!");
    }



    public static void generate(Writer w, List<ValueData> all) {

        if(all==null||all.isEmpty()) return;

        System.out.println("---");
        for(ValueData vd : all) {

            w.write("function validate_");

            System.out.println("> vd: " + vd.getValName().get());

            System.out.println(" ->opt      " + vd.isOption());
            System.out.println(" ->opt-val  " + vd.isOptValue());

            if(!vd.getOptions().isEmpty()) System.out.println("--> subparams begin");
            vd.getOptions().forEach(opt->generate(w, vd.getOptions()));
            if(!vd.getOptions().isEmpty()) System.out.println("<-- subparams end");
        }


    }

    public static void main(String[] args) throws IOException {

        Group root = Group.toGroup(Files.readString(Paths.get("src/main/resources/sample_validation_test.lua")));

        var all = new ArrayList<ValueData>();

        VisitingValidatorSpec.visitOne(root, all);

        StringWriter writer = new StringWriter();

        generate(writer, all);

    }


}
