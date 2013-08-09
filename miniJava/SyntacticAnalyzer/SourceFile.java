package miniJava.SyntacticAnalyzer;

public class SourceFile {

    static final char eol = '\n';
    static final char eot = '\u0000';
    
    java.io.File sourceFile;
    java.io.FileInputStream source;
    int currentLine;

    public SourceFile(String filename) {
        try {
            sourceFile = new java.io.File(filename);
            source = new java.io.FileInputStream(sourceFile);
            currentLine = 1;
        }
        catch (java.io.IOException s) {
            sourceFile = null;
            source = null;
            currentLine = 0;
        }
    }
    
    char getSource() {
        try {
            int c = source.read();

            if (c == -1) {
                c = eot;
            } else if (c == eol) {
                currentLine++;
            }
            
            return (char) c;
        }
        catch (java.io.IOException s) {
            return eot;
        }
    }
    
    int getCurrentLine() {
        return currentLine;
    }
}
