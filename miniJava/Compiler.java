package miniJava;

import miniJava.SyntacticAnalyzer.SourceFile;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.SyntacticAnalyzer.Parser;

public class Compiler {

    private static Scanner scanner;
    private static Parser parser;

    static void compileProgram(String sourceName) {
        SourceFile source = new SourceFile(sourceName);
        scanner = new Scanner(source);
        parser = new Parser(scanner);

        parser.parse();

        boolean successful = (reporter.numErrors == 0);

        if(successful)
            System.out.println("Compilation was successful.");
        else
            System.out.println("Compilation was unsuccessful.");

        return successful;
    }

    public static void main(String[] args) {
        boolean compiledOK;
        if(args.length != 1) {
            System.out.println("Usage: tc filename");
            System.exit(1);
        }

        String sourceName = args[0];

        compileOK = compileProgram(sourceName);

        if(compileOK)
            System.exit(0);
        else
            System.exit(4);
    }
}
