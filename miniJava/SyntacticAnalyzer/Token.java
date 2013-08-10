package miniJava.SyntacticAnalyzer;

import miniJava.SyntacticAnalyzer.SourcePosition;

final class Token extends Object {
    protected int kind;
    protected String spelling;
    protected SourcePosition position;
    
    public Token(int kind, String spelling, SourcePosition position) {
        this.kind = kind;
        this.spelling = spelling;
        this.position = position;

        if(kind == IDENTIFIER) {
            for(int k = firstReservedWord; k <= lastReservedWord; k++) {
                if(spelling.equals(spellings[k])) {
                    this.kind = k;
                    break;
                }
            }
        }
    }
    
    public static String spell(int kind) {
        return spellings[kind];
    }
    
    public String toString() {
        return "Kind=" + kind + ", spelling=" + spelling +
        ", position=" + position;
    }
    
    // Token classes
    public static final int INTLITERAL = 0;
    public static final int IDENTIFIER = 1;
    public static final int OPERATOR   = 2;

    // Operators
    public static final int GREATER    = 3;
    public static final int LESS       = 4;
    public static final int EQUAL      = 5;
    public static final int LEQUAL     = 6;
    public static final int GEQUAL     = 7;
    public static final int NOTEQUAL   = 8;
    public static final int AND        = 9;
    public static final int OR         = 10;
    public static final int NOT        = 11;
    public static final int PLUS       = 12;
    public static final int MINUS      = 13;
    public static final int TIMES      = 14;
    public static final int DIV        = 15;

    public static final int ASSIGN     = 16;

    // Keywords
    public static final int CLASS      = 17;
    public static final int RETURN     = 18;
    public static final int PUBLIC     = 19;
    public static final int PRIVATE    = 20;
    public static final int STATIC     = 21;
    public static final int INT        = 22;
    public static final int BOOLEAN    = 23;
    public static final int VOID       = 24;
    public static final int THIS       = 25;
    public static final int IF         = 26;
    public static final int ELSE       = 27;
    public static final int WHILE      = 28;
    public static final int TRUE       = 29;
    public static final int FALSE      = 30;
    public static final int NEW        = 31;

    // Punctuation
    public static final int DOT        = 32;
    public static final int COMMA      = 33;
    public static final int SEMICOLON  = 34;

    // Brackets
    public static final int LPAREN     = 35;
    public static final int RPAREN     = 36;
    public static final int LBRACKET   = 37;
    public static final int RBRACKET   = 38;
    public static final int LCURLY     = 39;
    public static final int RCURLY     = 40;

    // Special tokens
    public static final int EOT        = 41; // end of the input text
    public static final int ERROR      = 42;
    
    private static String[] spellings = {
        "<int>",
        "<identifier>",
        "<operator>",
        ">",
        "<",
        "==",
        "<=",
        ">=",
        "!=",
        "&&",
        "||",
        "!",
        "+",
        "-",
        "*",
        "/",
        "=",
        "class",
        "return",
        "public",
        "private",
        "static",
        "int",
        "boolean",
        "void",
        "this",
        "if",
        "else",
        "while",
        "true",
        "false",
        "new",
        ".",
        ",",
        ";",
        "(",
        ")",
        "[",
        "]",
        "{",
        "}",
        "",
        "<error>"
    };
    
    private final static int firstReservedWord = Token.CLASS;
    private final static int lastReservedWord  = Token.NEW;
}
