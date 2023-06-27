package Lexer;

public class token {

    enum tokenType{
        endOfFile, GT, GE, LT, LE, EQ, NE ;
    }

    public String lexeme;
    public token.tokenType typeOfToken;
    public String nullToken;
    public int intToken;

    token(String lex, tokenType type){    // parametrized constructor
        lexeme = lex;
        typeOfToken = type;
    }

    token(String lex, String token){    // parametrized constructor
        lexeme = lex;
        nullToken = token;
    }

    token(String lex, int token){    // parametrized constructor
        lexeme = lex;
        intToken =token;
    }

    token(){              // default constructor
        lexeme = " ";
        typeOfToken = typeOfToken.endOfFile;
        nullToken = "^";
        intToken =0;
    }

    void printNull(String lexeme){
        System.out.println(" ( " + lexeme + ", ^ )" );
    }

    void printType(String lexeme, tokenType type){
        System.out.println(" ( " + lexeme + ", " + type + " )" );
    }

    void printStr(String lexeme, String str){
        System.out.println(" ( " + lexeme + ", " + str + " )" );
    }

    void printNum(String lexeme, int num){
        System.out.println(" ( " + lexeme + ", " + num + " )" );
    }


}
