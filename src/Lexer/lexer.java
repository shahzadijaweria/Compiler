package Lexer;
import java.io.*;
import java.util.ArrayList;


public class lexer {
    String[] keywords;  // already declared keywords ist
    int state;     // DFA state is initially 0. Keeps track of DFA states
    StringBuffer identifier ;   // to store variables
    StringBuffer literalValue;  // to store literal i.e. single quote letters/words
    StringBuffer commentValue;    // to store comments
    StringBuffer stringValue;    // to store  strings i.e. double quotes words/letters
    int lineNumber;
    int intVariable;          // to store value of any variable
    boolean isNextLine; // used to track new line
    ArrayList<String> pairToBeWritten = new ArrayList<>();
    int index;

    lexer(){
        keywords = new String[]{"int", "char", "if", "elif", "else", "while", "in", "print", "println",
                "func", "call", "for", "while", "str"};
        state=0;
        identifier = new StringBuffer();
        literalValue = new StringBuffer();
        commentValue = new StringBuffer();
        stringValue = new StringBuffer();
        lineNumber =0;
        intVariable=0;
        isNextLine=false;
        index=0;
    }

    boolean isKeyword(String str){        // checking if read string is a keyword
        for (int i=0; i< keywords.length;i++){
            if (keywords[i].equalsIgnoreCase( str))
                return true;

        }
        return false;
    }

    public void readFile(String file) throws IOException {
        FileReader fr = new FileReader(file);   // file to read input from
        FileWriter writer = new FileWriter("tokens_.txt");

        int character;  // var to read file char by char

        while ((character = fr.read()) !=-1){  // returns -1 at end of file

            if ((char)character=='\n' ) //taking in consider the new lineNumber
            {
                lineNumber ++;
                isNextLine =true;
            }
            generateTokenLexemePair((char)character);       // call tokenizer to generate pairs

        }
       // System.out.println("pairs in pairToBeWritten array:  "+ pairToBeWritten+ "  "+ "\n");

       int len =  pairToBeWritten.size();
        int i=0;
        String tokenLexPair;
        while(i!=len){
            tokenLexPair = pairToBeWritten.get(i);
            //out.println(tokenLexPair + "\n");
            writer.append(tokenLexPair + "\n");
            writer.flush();
            i++;
        }
        writer.close();
        fr.close();
    }

    public void generateTokenLexemePair(char character) throws IOException {
      String pair;

        int i;
        i=0;
        while (i != -1) {
            i++;
            switch (state) {
                case 0:
                    if (character == '=') {
                        state = 1;
                        i=-1;
                        break;
                    } else if (character == '>') {
                        state = 2;
                        i=-1;
                        break;
                    } else if (character == '<') {
                        state = 3;
                        i=-1;
                        break;
                    } else if (character == '~') {
                        state = 4;
                        i=-1;
                        break;
                    } else if (character == '\n') {
                        i=-1;
                        state=0;
                        break;
                    }else if (character == '\'') {  //single comma for character
                        state = 13;
                        literalValue.append(character);
                        i=-1;
                        break;
                    } else if (character == '\"') {
                        state = 17;
                        stringValue.append(character);
                        i=-1;
                        break;
                    } else if (character == '#') {
                        state = 20;
                        commentValue.append(character);
                        isNextLine = false;
                        i=-1;
                        break;
                    } else if (character == 'b') {
                        state = 16;
                        i=-1;
                        break;
                    } else if (character == 'e') {
                        state = 29;
                        i=-1;
                        break;
                    } else if (Character.isDigit(character)) {
                        state = 23;
                        intVariable += Character.getNumericValue(character);
                        i=-1;
                        break;
                    } else if (Character.isLetter(character)) {
                        state = 11;
                        identifier.append(character);
                        i=-1;
                        break;
                    } else if (character == ' ') {
                        i=-1;
                        state=0;
                        break;
                    } else if (character == ':') {
                        token tk15 = new token(":", "^");
                        tk15.printStr(tk15.lexeme, tk15.nullToken);
                        pair = "(" +tk15.lexeme + "," + tk15.nullToken + ")";
                        pairToBeWritten.add(pair);
                        state = 0; //resetting the DFA
                        i=-1;
                        break;
                    } else if (character == ';') {
                        token tk16 = new token(";", "^");
                        tk16.printStr(tk16.lexeme, tk16.nullToken);
                        pair = "(" +tk16.lexeme + "," + tk16.nullToken + ")";
                        pairToBeWritten.add(pair);

                        state = 0; //resetting the DFA
                        i=-1;
                        break;
                    }
                    else if (character == ',') {
                        state = 31;
                        i=-1;
                        break;
                    }
                    else if (character == '+') {
                        token op1 = new token("+","^");
                        op1.printStr(op1.lexeme, op1.nullToken);
                        pair = "(" +op1.lexeme + "," + op1.nullToken + ")";
                        pairToBeWritten.add(pair);

                        state = 0; //resetting the DFA
                        i=-1;
                        break;
                    }
                    else if (character == '-') {
                        token op1 = new token("-","^");
                        op1.printStr(op1.lexeme, op1.nullToken);
                        pair = "(" +op1.lexeme + "," + op1.nullToken + ")";
                        pairToBeWritten.add(pair);

                        state = 0; //resetting the DFA
                        i=-1;
                        break;
                    }
                    else if (character == '*') {
                        token op1 = new token("*","^");
                        op1.printStr(op1.lexeme, op1.nullToken);
                        pair = "(" +op1.lexeme + "," + op1.nullToken + ")";
                        pairToBeWritten.add(pair);

                        state = 0; //resetting the DFA
                        i=-1;
                        break;
                    }
                    else if (character == '/') {
                        token op1 = new token("/","^");
                        op1.printStr(op1.lexeme, op1.nullToken);
                        pair = "(" +op1.lexeme + "," + op1.nullToken + ")";
                        pairToBeWritten.add(pair);

                        state = 0; //resetting the DFA
                        i=-1;
                        break;
                    }
                    else if (character == '%') {
                        token op1 = new token("%","^");
                        op1.printStr(op1.lexeme, op1.nullToken);
                        pair = "(" +op1.lexeme + "," + op1.nullToken + ")";
                        pairToBeWritten.add(pair);

                        state = 0; //resetting the DFA
                        i=-1;
                        break;
                    }
                    else {
                        //error statement
                        return;     //terminate program
                    }
                case 1:
                    token tk = new token("RO", token.tokenType.EQ);
                    tk.printType(tk.lexeme, tk.typeOfToken);
                    pair= "(" +tk.lexeme + "," + tk.typeOfToken + ")";
                    pairToBeWritten.add(pair);
                    state = 0; //resetting the DFA
                    break;

                case 2:
                    if (character == '=') {
                        state = 5;
                        i=-1;
                        break;
                    } else {
                        state = 6;
                        i=-1;
                        break;
                    }
                case 3:
                    if (character == '=') {  // if it is <= in comparison
                        state = 7;
                        i=-1;
                        break;
                    } else if (character == '-') { // if it is assignment operator as <-
                        state = 8;
                        i = -1;
                        break;
                    }
//                    } else if (Character.isLetter(character)) { // if it is a digit after < operator
//                        state = 9;
////                        identifier.append(character);
////                        i = -1;
//                        break;
//                    }
                    else {                      // go to state 9 for output token,lex pair of RO
                        state = 9;
                       // i=-1;
                        break;
                    }
                case 4:
                    if (character == '=') {
                        state = 10;
                        i=-1;
                        break;
                    }
                case 5:
                    token tk1 = new token("RO", token.tokenType.GE);
                    tk1.printType(tk1.lexeme, tk1.typeOfToken);
                    pair="(" +tk1.lexeme + "," + tk1.typeOfToken + ")";
                    pairToBeWritten.add(pair);
                    state = 0; //resetting the DFA
                    break;
                case 6:
                    token tk2 = new token("RO", token.tokenType.GT);
                    tk2.printType(tk2.lexeme, tk2.typeOfToken);
                    pair = "(" +tk2.lexeme + "," + tk2.typeOfToken + ")";
                    pairToBeWritten.add(pair);
                    state = 0; //resetting the DFA
                    break;
                case 7:
                    token tk3 = new token("RO", token.tokenType.LE);
                    tk3.printType(tk3.lexeme, tk3.typeOfToken);
                    pair = "(" +tk3.lexeme + "," + tk3.typeOfToken + ")";
                    pairToBeWritten.add(pair);
                    state = 0; //resetting the DFA
                    break;
                case 8:
                    token tk4 = new token("<-", "^");
                    tk4.printStr(tk4.lexeme, tk4.nullToken);
                    pair = "(" +tk4.lexeme + "," + tk4.nullToken + ")";
                    pairToBeWritten.add(pair);
                    state = 0; //resetting the DFA
                    break;
                case 9:
                    token tk5 = new token("RO", token.tokenType.LT);
                    tk5.printType(tk5.lexeme, tk5.typeOfToken);
                    pair = "(" +tk5.lexeme + "," + tk5.typeOfToken + ")";
                    pairToBeWritten.add(pair);
                    state = 0; //resetting the DFA
                    break;
                case 10:
                    token tk6 = new token("RO", token.tokenType.NE);
                    tk6.printType(tk6.lexeme, tk6.typeOfToken);
                    pair = "(" +tk6.lexeme + "," + tk6.typeOfToken + ")";
                    pairToBeWritten.add(pair);
                    state = 0; //resetting the DFA
                    break;
                case 11:
                    if (Character.isLetter(character)) {
                        identifier.append(character);
                        i=-1;
                        break;
                    } else {//state = 12;
                        if (isKeyword(identifier.toString()) == true) {
                            token tk7 = new token(identifier.toString(), "^");
                            tk7.printStr(tk7.lexeme, tk7.nullToken);
                            pair = "(" +tk7.lexeme + "," + tk7.nullToken + ")";
                            pairToBeWritten.add(pair);
                            state = 0;                          //resetting the DFA
                            identifier.setLength(0);          //resetting the string builder
                        } else {
                            token tk8 = new token("ID", identifier.toString());
                            tk8.printStr(tk8.lexeme, tk8.nullToken);
                            pair = "(" +tk8.lexeme + "," + tk8.nullToken + ")";
                            pairToBeWritten.add(pair);
                            state = 0; //resetting the DFA
                            identifier.setLength(0); //resetting the string builder
                        }
                        break;
                    }
                case 13:
                    if (Character.isLetter(character)) {
                        state = 14;
                        literalValue.append(character); //forming the literal
                        i=-1;
                        break;
                    } else {
                        //error statement
                        System.out.println("Invalid symbol following ' for literal at lineNumber " + lineNumber);
                        System.out.println("Check your code file and try again!");
                        return; //terminate program
                    }
                case 14:
                    if (character == '\'') {
                        state = 15;
                        literalValue.append(character); //forming the literal
                        i=-1;
                        break;
                    } else {
                        //error statement
                        System.out.println("Literal not enclosed properly at lineNumber " + lineNumber);
                        System.out.println("Check your code file and try again!");
                        return; //terminate program
                    }
                case 15:
                    token tk9 = new token("CHAR", "^");//CHAR is returned as code for char lilteral
                    tk9.printStr(tk9.lexeme, tk9.nullToken);
                    pair = "(" +tk9.lexeme + "," + tk9.nullToken + ")";
                    pairToBeWritten.add(pair);

                    state = 0; //resetting the DFA
                    literalValue.setLength(0); //resetting the string builder
                    i=-1;
                    break;
                case 16:
                    if (character == 'e') {
                        state = 19;
                        i=-1;
                        break;
                    }
                case 17:
                    stringValue.append(character);
                    i=-1;
                    if(character=='\"')
                        state = 18;
                    if(character=='\n'){
                        //error statement
                        System.out.println("String not enclosed properly at lineNumber " + lineNumber);
                        System.out.println("Check your code file and try again!");
                        return; //terminate program
                    }
                    break;
                case 18:
                    token tk10 = new token("STR", stringValue.toString());
                    tk10.printStr(tk10.lexeme, tk10.nullToken);
                    pair = "(" +tk10.lexeme + "," + tk10.nullToken + ")";
                    pairToBeWritten.add(pair);
                    state = 0; //resetting the DFA
                    stringValue.setLength(0); //resetting the string builder
                    break;


                case 19:
                    if (character == 'g') {
                        state = 25;
                        i=-1;
                        break;
                    }
                case 20:
                    commentValue.append(character); //forming the comment
                    if (isNextLine)    // if next line comes and comment ends, then print
                    {
                       // token tk11 = new token("COMMENT", commentValue.toString().trim());
                        token tk11 = new token("COMMENT", "^");
                        tk11.printStr(tk11.lexeme, tk11.nullToken);
                        pair = "(" +tk11.lexeme + "," + tk11.nullToken + ")";
                        pairToBeWritten.add(pair);

                        state = 0; //resetting the DFA
                        commentValue.setLength(0); //resetting the string builder
                        isNextLine = false;
                    }

                    i=-1;
                    break;
                case 23:
                    if (Character.isDigit(character)) {
                        //forming the appropriate integer
                        intVariable *= 10;
                        intVariable += Character.getNumericValue(character);
                        i=-1;
                        break;
                    } else {
                        token tk12 = new token("INT", intVariable); //INT is returned as code for numeric lilteral
                        tk12.printNum(tk12.lexeme, tk12.intToken);
                        pair = "(" +tk12.lexeme + "," + tk12.intToken + ")";
                        pairToBeWritten.add(pair);
                        state = 0; //resetting the DFA
                        intVariable = 0;
                        break;
                    }
                case 25:
                    if (character == 'i') {
                        state = 26;
                        i=-1;
                        break;
                    }
                case 26:
                    if (character == 'n') {
                        state = 27;
                        i=-1;
                        break;
                    }
                case 27:
                    token tk13 = new token("BEGIN", "^");
                    tk13.printStr(tk13.lexeme, tk13.nullToken);
                    pair = "(" +tk13.lexeme + "," + tk13.nullToken + ")";
                    pairToBeWritten.add(pair);

                    state = 0; //resetting the DFA
                    identifier.setLength(0); //resetting the string builder
                    i=-1;
                    break;
                case 29:
                    if(character == 'n') {
                        state = 30;
                        i=-1;
                        break;
                    }
                    else
                    {
                        state=11;
                        i=-1;
                        break;

                    }
                case 30:
                    if (character == 'd') {
                        token tk14 = new token("END", "^");
                        tk14.printStr(tk14.lexeme, tk14.nullToken);
                        pair = "(" +tk14.lexeme + "," + tk14.nullToken + ")";
                        pairToBeWritten.add(pair);
                        state = 0; //resetting the DFA
                        identifier.setLength(0); //resetting the string builder
                        i=-1;
                        break;
                    }
                case 31:
                    token com = new token("COMMA","^");
                    com.printStr(com.lexeme, com.nullToken);
                    pair = "(" +com.lexeme + "," + com.nullToken + ")";
                    pairToBeWritten.add(pair);
                    state = 0; //resetting the DFA
                    i=-1;
                    break;

            }




        }
    }


}
