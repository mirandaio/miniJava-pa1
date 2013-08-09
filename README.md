COMP 520: Compilers
===================
PA1 - Syntactic Analysis
========================

The programming project in this class is the construction of a compiler for 
the miniJava language. The first assignment is to build a scanner and parser 
for miniJava to recognize syntactically correct programs.

miniJava
--------

The miniJava language is a subset of Java. Every miniJava program is a legal 
Java program with Java semantics. Following is an informal summary of the 
syntactic restrictions of Java that define miniJava. Later assignments will 
modify restrictions.

A miniJava program is a single file without a package declaration (hence 
corresponds to the default package) and without imports. It consists of zero 
or more Java classes. The classes are simple; there are no interface classes, 
subclasses, or nested classes.

The members of a class are fields and methods. Member declarations can specify 
**public** or **private** access, and can specify **static** instantiation. 
Fields can not have an initializing expression in their declaration. Methods 
have a parameter list and a body. There are no constructor methods.

The types of miniJava are primitive types, class types, and array types. The 
primitive types are limited to **void**, **int**, **boolean**, and the array 
types are limited to the integer array **int []** and the *class* [] array 
where *class* is any class type.

The statements of miniJava are limited to the statement block, the assignment 
statement, method invocation, the conditional statement (**if**), and the 
repetitive statement (**while**). A declaration of a local variable (with 
required initializing expression) can only appear as a statement within a 
statement block. The **return** statement, if present at all, can only appear 
as the last statement in a method and yields a result.

The expressions of miniJava consist of operations applied to literals, 
variables, (including indexed and qualified references), method invocation, 
and **new** arrays and objects. Expressions may be parenthesized to specify 
evaluation order. The operators in miniJava are limited to 

relational operations: >,    <,    ==,    <=,    >=,    !=

logical operations: &&,    ||,    !

arithmetic operations: +,    -,    *,    /

All operators are infix binary operators (binop) with the exceptions of the 
unary prefix operators (unop) logical negation (!), and arithmetic negation 
(-). The latter is both a unary and binary operator.

### Lexical Rules

The terminals in the miniJava grammar are the tokens produced by the scanner. 
The token id stands for any identifier formed from a sequence of letters, 
digits, and underscores, starting with a letter. Uppercase letters are 
distinguished from lowercase letters. The token *num* stands for any integer 
literal that is a sequence of decimal digits. Tokens *binop* and *unop* stand 
for the operators listed above, and the token *eot* stands for the end of the 
input text. The remaining tokens stand for themselves (i.e. for the sequence 
of characters that are used to spell them). Keywords of the language are shown 
in bold for readability only; they are written in regular lowercase text.

Whitespace and comments may appear before or after any token. Whitespace is 
limited to spaces, tabs (\t), newlines (\n) and carriage returns (\r). There 
are two forms of comments. One starts with /* and ends with */, while the 
other begins with // and extends to the end of the line.

The text of miniJava programs is written is ASCII. Any characters other than 
those that are part of a token, whitespace or a comment are erroneous.

### Grammar

The miniJava grammar is shown on the next page. Nonterminals are displayed in 
the normal font and start with a capital letter, while terminals are displayed 
in **this font**. Terminals *id*, *num*, *unop*, and *binop* represent a set of possible terminals. The remaining symbols are part of the BNF extensions for 
grouping, choice, and repetition. Besides these extensions the *option* 
construct is also used and is defined as follows: (a)? = (a | e). To help 
distinguish the parentheses used in grouping from the left and right 
parenthesis used as terminals, the latter are shown in bold. The start symbol 
of the grammar is "Program".

Syntactic analysis assignment
-----------------------------

The first task in the compiler project is to create a scanner and parser for 
miniJava starting from the lexical rules and the grammar is this document. 
Create a miniJava directory that holds a Compiler.java and a SyntacticAnalyzer 
subdirectory. You can follow the structure and classes of the Triangle 
compiler, but no need to replicate it unnecessarily since many details will be 
different or not needed.

Populate the SyntacticAnalyzer subdirectory with implementations for the 
Scanner, Parser, and Token classes. You may wish to include other classes; 
have a look at the classes defined in the syntactic analyzer in the Triangle 
distribution (e.g. SourceFile, SourcePosition, SyntaxError). You will not be 
building an AST yet, so you need not import AbstractSyntaxTree classes in the 
parser.

The Compiler.java in the miniJava directory should contain a main method and 
parse the miniJava program named as the first argument on the command line 
(the extension may be .java or .mjava). Execution must terminate using the 
method System.exit(rc) where rc = 0 if the input file was successfully parsed, 
and rc = 4 otherwise. No diagnostic message is needed in case the parse fails 
at this point, but it will be needed at later checkpoints.

miniJava Grammar
----------------

    Program ::= (ClassDeclaration)* eot

ClassDeclaration ::=  
      class id {  
        (FieldDeclaration | MethodDeclaration)*  
      }

FieldDeclaration ::= Declarators id;  

MethodDeclaration ::=   
      Declarators id (ParameterList?) {  
        Statement* (return Expression ;)?  
      }

Declarators ::= (public | private)? static? Type

Type ::= PrimType |  ClassType | ArrType

PrimType ::= int | boolean | void

ClassType ::= id

ArrType ::= ( int | ClassType ) []

ParameterList ::= Type id (, Type id)*

ArgumentList ::= Expression (, Expression)*

Reference ::= (this | id) (. id)*

Statement ::=  
        { Statement* }  
      | Type id = Expression ;  
      | Reference ([ Expression ])? = Expression ;  
      | Reference ( ArgumentList? ) ;  
      | if ( Expression ) Statement (else Statement)?  
      | while ( Expression ) Statement  

Expression ::=   
        Reference ( [ Expression ] ) ?  
      | Reference ( ArgumentList? )  
      | unop Expression  
      | Expression binop Expression  
      | ( Expression )  
      | num | true | false  
      | new (id() | int [ Expression ] | id [ Expression ] )  
