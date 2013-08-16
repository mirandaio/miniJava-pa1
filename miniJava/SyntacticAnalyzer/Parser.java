package miniJava.SyntacticAnalyzer;

import miniJava.ErrorReporter;
import miniJava.SyntacticAnalyzer.Scanner;

public class Parser {

    private Scanner lexicalAnalyzer;
    private Token currentToken;
    private ErrorReporter errorReporter;
    private SourcePosition previousTokenPosition;

    public Parser(Scanner lexer) {
        lexicalAnalyzer = lexer;
        errorReporter = reporter;
        previousTokenPosition = new SourcePosition();
    }

    void syntacticError(String messageTemplate, 
                        String tokenQuoted) throws SyntaxError {
        SourcePosition pos = currentToken.position;
        errorReporter.reportError(messageTemplate, tokenQuoted, pos);
        throw(new SyntaxError());
    }

    void accept(int tokenExpected) {
        if(currentToken.kind == tokenExpected) {
            previousTokenPosition = currentToken.position;
            currentToken = lexicalAnalyzer.scan();
        } else {
            syntacticError("\"%\" expected here", Token.spell(tokenExpected));
        }
    }

    void acceptIt() {
        previousTokenPosition = currentToken.position;
        currentToken = lexicalAnalyzer.scan();
    }

    // start records the position of the start of a phrase.
    // This is defined to be the position of the first
    // character of the first token of the phrase.
    void start(SourcePosition position) {
        position.start = currentToken.position.start;
    }

    // finish records the position of the end of a phrase.
    // This is defined to be the position of the last
    // character of the last token of the phrase.
    void finish(SourcePosition position) {
        position.finish = previousTokenPosition.finish;
    }

    private void parseProgram() {
        while(currentToken.kind == Token.CLASS) {
            parseClassDeclaration();
        }
        accept(Token.EOT);
    }
}
