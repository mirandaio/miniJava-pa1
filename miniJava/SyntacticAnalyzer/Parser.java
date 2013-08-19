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

    public void parse() {
        currentToken = lexicalAnalyzer.scan();

        try {
            parseProgram();
        }
        catch (SyntaxError s) {
            System.out.println("The syntax error has been catched...");
        }
    }

    private void parseProgram() throws SyntaxError {
        while(currentToken.kind == Token.CLASS) {
            parseClassDeclaration();
        }
        accept(Token.EOT);
    }

    private void parseClassDeclaration() throws SyntaxError {
        accept(Token.CLASS);
        parseIdentifier();
        accept(Token.LCURLY);

        while(isStarterDeclarators(currentToken.kind)) {
            parseDeclarators();
            parseIdentifier();

            switch(currentToken.kind) {
            case Token.SEMICOLON:
                acceptIt();
                break;

            case Token.LPAREN:
                acceptIt();

                if(isStarterParameterList(currentToken.kind))
                    parseParameterList();

                accept(Token.RPAREN);
                accept(Token.LCURLY);

                while(isStarterStatement(currentToken.kind))
                    parseStatement();

                if(currentToken.kind == Token.RETURN) {
                    acceptIt();
                    parseExpression();
                    accept(Token.SEMICOLON);
                }

                accept(Token.RCURLY);
                break;

            default:
                syntacticError("\"%\" cannot be used here. You need a ; or (", 
                    currentToken.spelling);
                break;

            }
        }
        accept(Token.RCURLY);
    }

    private void parseDeclarators() throws SyntaxError {
        if(currentToken.kind == Token.PUBLIC 
            || currentToken.kind == Token.PRIVATE)
            acceptit();

        if(currentToken.kind == Token.STATIC)
            acceptIt();

        parsetType();
    }

    private void parseType() throws SyntaxError {
        switch(currentToken.kind) {
        case Token.BOOLEAN:
        case Token.VOID:
            acceptIt();
            break;

        case Token.INT:
        case Token.IDENTIFIER:
            acceptIt();
            if(currentToken.kind == Token.LBRACKET) {
                acceptIt();
                accept(Token.RBRACKET);
            }
            break;

        default:
            syntacticError("\"%\" cannot start a type", currentToken.spelling);
            break;
        }
    }

    private void parseParameterList() throws SyntaxError {
        parseType();
        parseIdentifier();

        while(currentToken.kind == Token.COMMA) {
            acceptIt();
            parseType();
            parseIdentifier();
        }
    }

    private void parseArgumentList() throws SyntaxError {
        parseExpression();

        while(currentToken.kind == Token.COMMA) {
            acceptIt();
            parseExpression();
        }
    }

    private void parseReference() throws SyntaxError {
        if(currentToken.kind == Token.THIS)
            acceptIt();
        else if(currentToken.kind == Token.IDENTIFIER)
            acceptIt();
        else
            syntacticError("\"%\" cannot start a reference", 
                currentToken.spelling);

        while(currentToken.kind == Token.DOT) {
            acceptIt();
            parseIdentifier();
        }
    }

    private void parseExpression() {
        switch(currentToken.kind) {
        case Token.THIS: // Reference
        case Token.IDENTIFIER:
            parseReference();
            if(currentToken.kind == Token.LBRACKET) {
                acceptIt();
                parseExpression();
                accept(Token.RBRACKET);
            } else if(currentToken.kind == Token.LPAREN) {
                acceptIt();
                if(isStarterArgumentList(currentToken.kind))
                    parseArgumentList();
                accept(Token.RPAREN);
            }
            break;

        case Token.NOT:
        case Token.MINUS:
            acceptIt();
            parseExpression();
            break;

        case Token.LPAREN:
            acceptIt();
            parseExpression();
            accept(Token.RPAREN);
            break;

        case Token.INTLITERAL:
        case Token.TRUE:
        case Token.FALSE:
            acceptIt();
            break;

        case Token.NEW:
            acceptIt();
            if(currentToken.kind == Token.INT) {
                acceptIt();
                accept(Token.LBRACKET);
                parseExpression();
                accept(RPAREN);
            } else if(currentToken.kind == Token.IDENTIFIER) {
                acceptIt();
                if(currentToken.kind == Token.LBRACKET) {
                    acceptIt();
                    parseExpression();
                    accept(Token.RBRACKET);
                } else if(currentToken.kind == Token.LPAREN) {
                    acceptIt();
                    accept(Token.RPAREN);
                }
            }
            break;

        default:
            syntacticError("\"%\" cannot start an expression", 
                currentToken.spelling);
        }

        while(isStarterBinop(currentToken.kind)) {
            acceptIt();
            parseExpression();
        }
    }

    private boolean isStarterDeclarators(int kind) {
        return kind == Token.PUBLIC
                || kind == Token.PRIVATE
                || kind == Token.STATIC
                || isStarterType(kind);
    }

    private boolean isStarterType(int kind) {
        return kind == Token.BOOLEAN
                || kind == Token.VOID
                || kind == Token.INT
                || kind == Token.IDENTIFIER;
    }


}
