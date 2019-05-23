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
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public final class ValidationGenerator {

    private int indent = 0;
    private Writer w;

    public ValidationGenerator() {

    }


    private String globalPath(ValueData vd) {

        ValueData parent = vd.getParentNode();

        var pathToRoot = new ArrayList<String>();
        pathToRoot.add(vd.getValName());

        while(parent!=null) {
            pathToRoot.add(parent.getValName());
            parent = parent.getParentNode();
        }

        // we need to reverse the path elements to get an xpath compatible path name
        Collections.reverse(pathToRoot);
        // TODO get rid of hard-coded 'problem' root node
        return "problem/"+String.join("/",pathToRoot);
    }




    private ValidationGenerator i() throws IOException {
        String indentStr = " ".repeat(indent);
        w.write(indentStr);
        return this;
    }

    private ValidationGenerator p(String str) throws IOException {
        w.write(str);
        return this;
    }


    private ValidationGenerator n() throws IOException {
        w.append("\n");
        return this;
    }


    private void inc() {
        indent+=4;
    }

    private void dec() {
        indent-=4;
        if(indent<0) indent=0;
    }


    public void generate(Writer w, List<ValueData> all) throws IOException {

        this.w = w;

        if(all==null||all.isEmpty()) return;

        var optionStack = new Stack<Integer>();

        for(ValueData vd : all) {

            String name = vd.getValName();

            if(vd.isOption()) {

                if(optionStack.isEmpty()) {
                    optionStack.push(0);
                }
                Integer optionNumber = optionStack.pop();


            } else {

                i().p("function validate_" + globalPath(vd).replace('/', '_') + "(all, validation)").n();

                inc();

                i().p("if ").p(globalPath(vd).replace('/', '.')).p(" == nil then error(\"required entry '").p(globalPath(vd).replace('/','.')).p("'").p(" not present.\")").n();

                dec();

                i().p("end").n();
            }


            System.out.println("> vd: " + vd.getValName());

            System.out.println(" -> opt      " + vd.isOption());

            if(!vd.getOptions().isEmpty()) System.out.println("--> subparams begin");

            generate(w, vd.getOptions());

            if(!vd.getOptions().isEmpty()) System.out.println("<-- subparams end");
        }

    }

    public static void main(String[] args) throws IOException {

        Group root = Group.toGroup(Files.readString(Paths.get("src/main/resources/sample_validation_test.lua")));

        var all = new ArrayList<ValueData>();

        VisitingValidatorSpec.visitOne(root, all, ValueData::new);

        StringWriter writer = new StringWriter();

        ValidationGenerator generator = new ValidationGenerator();

        generator.generate(writer, all);

        System.out.println(writer.getBuffer().toString());

    }


}
