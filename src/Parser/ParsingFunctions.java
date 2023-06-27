
package Parser;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ParsingFunctions {
    static boolean forflag;
    static boolean forflag2;
    static boolean forflag3;
    static boolean forflag4;
    static boolean expressionflag;
    static boolean startFlag=true;
    static int n = 1;                //used to track line number for three address code
    static int t = 1;                //used to track number for temporary variable generation


    static String generateTempVar()
    {
        String tempVar = "t"+t;
        t++;
        return tempVar;
    }

    static void writeToSymbolTable(String s) throws IOException
    {
        FileWriter writer = new FileWriter("parser-symboltable.txt",true);
        writer.append(s+"\n");
        writer.close();
    }

    static String[] nextToken(Scanner reader) throws FileNotFoundException
    {
        String[] token = new String[2];
        String data = "";

        if (reader.hasNext()) {
            data = reader.nextLine(); //reading (token,lexeme) from file
        } else {
            SyntaxError();
        }

        String tempToken = data.substring(1, data.length() - 1); //extracting inner part
        if (tempToken.startsWith(",")) {
            token[0] = ",";
            token[1] = "^";
        } else {
            String[] tokenList = tempToken.split(",");
            token[0] = tokenList[0];
            token[1] = tokenList[1];
        }
        return  token;
    }

    static void SyntaxError()
    {
        System.out.println("Syntax Error !!!!!!");
        System.exit(0);
    }

    static void outputTac(String str) throws IOException
    {
        FileWriter writer = new FileWriter("tac.txt", true);
        writer.append(n+"\t"+str+"\n");
        n++;
        writer.close();
    }

    static void backPatch(int line, int patch) throws IOException
    {
        Path path = Paths.get("tac.txt");
        List<String> data = Files.readAllLines(path);
        String str = data.get(line - 1) + patch;
        data.set(line - 1, str);
        Files.write(path, data);
    }

    static void Start(String[] token, Scanner reader) throws IOException
    {
        if(token[0].equals("func") && startFlag==true){
            while(!token[0].equals("func"))
            {
                token=nextToken(reader);
            }
        }
        if(token[0].equals("func") && startFlag==true)
        {
            System.out.println(token[0]);
            token=nextToken(reader);
            if(token[0].equals("INT") )
            {
                System.out.println(token[0]);
                token=nextToken(reader);
                if(token[1].equals("main"))
                {
                    System.out.println(token[1]);
                    token=nextToken(reader);
                    if(token[0].equals(":"))
                    {
                        System.out.println(token[0]);
                        token=nextToken(reader);
                        if(token[0].equals("BEGIN"))
                        {
                            System.out.println(token[0]);
                            token=nextToken(reader);
                            startFlag=false;
                        }
                    }
                }
            }
        }
        if(startFlag==false){
            if(token[0].equals("if") || token[0].equals("for") || token[0].equals("print")
                    || token[0].equals("println") || token[0].equals("in") || token[0].equals("ID")
                    || token[0].equals("call") || token[0].equals("COMMENT"))
            {
                System.out.println(token[0]);
                Statements(token, reader);
            }
            else
            {
                SyntaxError();
            }
        }
    }


    static void Statements(String[] token, Scanner reader) throws IOException
    {
        Statement(token, reader);

        if(reader.hasNext(Pattern.quote("(if,^)")) || reader.hasNext(Pattern.quote("(for,^)"))
                || reader.hasNext("\\(print.*") ||
                reader.hasNext(Pattern.quote("(println,^)")) || reader.hasNext(Pattern.quote("(in,^)"))
                || reader.hasNext(Pattern.quote("(INT,^)")) || reader.hasNext(Pattern.quote("(CHAR,^)"))
                || reader.hasNext("\\(ID.*") || reader.hasNext(Pattern.quote("(call,^)")) || reader.hasNext(Pattern.quote("(COMMENT,^)")))
        {
            token = nextToken(reader);
            System.out.println(token[0]);
            System.out.println(token[1]);
            Statements(token, reader);
        }
        else
        {
        }
    }

    static void Statement(String[] token, Scanner reader) throws IOException
    {
        String tacStr;

        //IF case
        if(token[0].equals("if"))
        {
            boolean ifFlag = false;
            if(token[0].equals("if"))
            {
                ifFlag = true;
            }

            token = nextToken(reader);

            tacStr = "if " + Condition(token, reader) + " goto ";
            int trackLine1 = n;
            outputTac(tacStr);
            tacStr = "goto ";
            int trackLine2 = n;
            outputTac(tacStr);

            token = nextToken(reader);

            if(token[0].contains(":"))
            {
            }
            else
            {
                SyntaxError();
            }

            backPatch(trackLine1, n);

            token = nextToken(reader);
            if(token[0].contains("BEGIN"))
            {
                writeToSymbolTable("SCOPE START");
            }
            else
            {
                SyntaxError();
            }

            token = nextToken(reader);
            Statements(token, reader);

            tacStr = "goto ";
            int trackLine3 = n;
            outputTac(tacStr);

            token = nextToken(reader);
            if(token[0].contains("END"))
            {
                writeToSymbolTable("SCOPE END");
            }
            else
            {
                SyntaxError();
            }

            backPatch(trackLine2, n);

            if(ifFlag)
            {
                if(reader.hasNext(Pattern.quote("(else,^)")))
                {
                    token = nextToken(reader);
                }
                ElifOrElse(token, reader);
                backPatch(trackLine3, n);
            }
            else
            {
                backPatch(trackLine3, trackLine1);
            }
        }
        //PRINT and PRINTLN case
        else if(token[0].equals("print") || token[0].equals("println"))
        {
            boolean lnFlag = false;
            if(token[0].equals("println"))
            {
                lnFlag = true;
            }
            String option = printData(token, reader);

            if(option.length() == 0)
            {
                tacStr = "out ";
                outputTac(tacStr);
            }
            else
            {
                tacStr = "out "+option;
                outputTac(tacStr);
            }

            token = nextToken(reader);
            if(token[0].contains(";"))
            {
            }
            else
            {
                SyntaxError();
            }

            if(lnFlag)
            {
                tacStr = "out ";
                outputTac(tacStr);
            }
        }

        //FOR case
        else if(token[0].equals("for"))
        {
            forflag=true;
            token = nextToken(reader);

            if(token[0].equals("ID"))
            {
                Variable(token, reader);
            }

            tacStr = "if " + Condition(token, reader) + " goto ";
            int trackLine1 = n;
            System.out.println(tacStr);
            outputTac(tacStr);
            tacStr = "goto ";
            int trackLine2 = n;
            outputTac(tacStr);
            System.out.println(tacStr);

            token = nextToken(reader);

            if(token[0].equals("ID"))
            {
                forflag4=true;
                System.out.println(" forfalg4 is true now "+token[1]);
                AssignmentStatement(token,reader);
            }

            token=nextToken(reader);

            backPatch(trackLine1, n);

            if(token[0].contains("BEGIN"))
            {
                writeToSymbolTable("SCOPE START");
            }
            else
            {
                SyntaxError();
            }

            token = nextToken(reader);
            Statements(token, reader);

            Path fileName = Path.of("temp.txt");
            List<String> str=Files.readAllLines(fileName);
            for(String line: str) {
                outputTac(line);
            }


            tacStr = "goto "+trackLine1;
            int trackLine3 = n;
            outputTac(tacStr);

            token = nextToken(reader);
            if(token[0].contains("END"))
            {
                writeToSymbolTable("SCOPE END");
            }
            else
            {
                SyntaxError();
            }
            backPatch(trackLine2, n);
        }

        //IN case
        else if(token[0].equals("in"))
        {
            if(token[0].equals("in"))
            {
            }
            else
            {
                SyntaxError();
            }

            token = nextToken(reader);
            if(token[0].equals("ID"))
            {
                tacStr = "in "+token[1];
                outputTac(tacStr);
            }
            else
            {
                SyntaxError();
            }

            token = nextToken(reader);
            inputDelimiter(token, reader);
        }

        //Variable case
        else if(token[0].equals("ID"))
        {
            Variable(token, reader);
        }
        //Comments case
        else if(token[0].equals("COMMENT"))
        {
        }
        else
        {
            SyntaxError();
        }
    }

    static void ElifOrElse(String[] token, Scanner reader) throws IOException
    {
        String tacStr;

        if(token[0].equals("elif"))
        {

            token = nextToken(reader);

            tacStr = "if " + Condition(token, reader) + " goto ";
            int trackLine1 = n;
            outputTac(tacStr);
            tacStr = "goto ";
            int trackLine2 = n;
            outputTac(tacStr);

            token = nextToken(reader);

            if(token[0].contains(":"))
            {
            }
            else
            {
                SyntaxError();
            }

            backPatch(trackLine1, n);

            token = nextToken(reader);
            if(token[0].contains("BEGIN"))
            {
                writeToSymbolTable("SCOPE START");
            }
            else
            {
                SyntaxError();
            }

            token = nextToken(reader);
            Statements(token, reader);

            tacStr = "goto ";
            int trackLine3 = n;
            outputTac(tacStr);

            token = nextToken(reader);
            if(token[0].contains("END"))
            {
                writeToSymbolTable("SCOPE END");
            }
            else
            {
                SyntaxError();
            }

            backPatch(trackLine2, n);

            if(reader.hasNext(Pattern.quote("(elif,^)")) || reader.hasNext(Pattern.quote("(else,^)")))
            {
                token = nextToken(reader);
            }
            ElifOrElse(token, reader);

            backPatch(trackLine3, n);
        }
        else
        {
            Else(token, reader);
        }
    }

    static void Else(String[] token, Scanner reader) throws IOException
    {
        if(token[0].equals("else"))
        {
            token = nextToken(reader);
            if(token[0].contains("BEGIN"))
            {
                writeToSymbolTable("SCOPE START");
            }
            else
            {
                SyntaxError();
            }

            token = nextToken(reader);
            Statements(token, reader);

            token = nextToken(reader);
            if(token[0].contains("END"))
            {
                writeToSymbolTable("SCOPE END");
            }
            else
            {
                SyntaxError();
            }
        }
        else
        {
        }
    }

    static String Expression(String[] token, Scanner reader) throws IOException
    {
        String getValue;
        String temp = Term(token, reader);

        if(reader.hasNext(Pattern.quote("(+,^)")) || reader.hasNext(Pattern.quote("(-,^)")))
        {
            token = nextToken(reader);
            getValue = E_(token, reader, temp);
        }
        else
        {
            getValue = temp;
        }
        return getValue;
    }

    static String Term(String[] token, Scanner reader) throws IOException
    {
        String getValue;

        String temp = Factor(token, reader);

        if(reader.hasNext(Pattern.quote("(*,^)")) || reader.hasNext(Pattern.quote("(/,^)"))
                || reader.hasNext(Pattern.quote("(%,^)")))
        {
            token = nextToken(reader);
            getValue = T_(token, reader, temp);
        }
        else
        {
            getValue = temp;
        }
        return getValue;
    }

    static String E_(String[] token, Scanner reader, String tacVar) throws IOException
    {
        String getValue = "";
        String tacStr;

        String tempTac = generateTempVar();
        getValue = tempTac;
        tacStr = tempTac + " = " + tacVar;

        if(token[0].contains("+"))
        {
            expressionflag=true;
            tacStr = tacStr + " + ";
        }
        else if(token[0].contains("-"))
        {
            tacStr = tacStr + " - ";
        }
        else
        {
            SyntaxError();
        }

        token = nextToken(reader);

        String temp = Term(token, reader);
        tacStr = tacStr + temp;
        if(forflag4==false){
            outputTac(tacStr);
        }
        else if(forflag4==true){
            FileWriter fw=new FileWriter("temp.txt");
            fw.write(tacStr);
            fw.close();
            forflag4=false;
        }

        if(reader.hasNext(Pattern.quote("(+,^)")) || reader.hasNext(Pattern.quote("(-,^)")))
        {
            token = nextToken(reader);
            getValue = E_(token, reader, tempTac);
        }
        else
        {
        }
        return getValue;
    }

    static String Factor(String[] token, Scanner reader) throws IOException
    {
        String getValue = "";

        if(token[0].equals("ID"))
        {
            getValue = token[1];
        }
        else if(token[0].equals("INT"))
        {
            getValue = token[1];
        }
        else if(token[0].equals("'('"))
        {
            token = nextToken(reader);
            getValue = Expression(token, reader);

            token = nextToken(reader);
            if(token[0].equals("')'"))
            {
            }
            else
            {
                SyntaxError();
            }
        }
        else
        {
            SyntaxError();
        }
        return getValue;
    }

    static String T_(String[] token, Scanner reader, String tacVar) throws IOException
    {
        String getValue = "";
        String tacStr;

        String tempTac = generateTempVar();
        getValue = tempTac;
        tacStr = tempTac + " = " + tacVar;

        if(token[0].contains("*"))
        {
            tacStr = tacStr + " * ";
        }
        else if(token[0].contains("/"))
        {
            tacStr = tacStr + " / ";
        }
        else if(token[0].contains("%"))
        {
            tacStr = tacStr + " % ";
        }
        else
        {
            SyntaxError();
        }
        token = nextToken(reader);

        String temp = Factor(token, reader);
        tacStr = tacStr + temp;
        outputTac(tacStr);

        if(reader.hasNext(Pattern.quote("(*,^)")) || reader.hasNext(Pattern.quote("(/,^)"))
                || reader.hasNext(Pattern.quote("(%,^)")))
        {
            token = nextToken(reader);
            getValue = E_(token, reader, tempTac);
        }
        else
        {
        }
        return getValue;
    }

    static String printData(String[] token, Scanner reader) throws IOException
    {
        String getValue = "";

        token = nextToken(reader);

        if(token[0].equals("STR"))
        {
            getValue = token[1];
        }
        else if(token[0].equals("LIT"))
        {
            getValue = token[1];
        }
        else if(token[0].equals("ID"))
        {
            if (reader.hasNext(Pattern.quote("(+,^)")) || reader.hasNext(Pattern.quote("(-,^)"))
                    || reader.hasNext(Pattern.quote("(*,^)")) || reader.hasNext(Pattern.quote("(/,^)")))
            {
                getValue = Expression(token, reader);

            }
            else
            {
                getValue = token[1];
            }
        }
        else if(token[0].equals("INT"))
        {
            if (reader.hasNext(Pattern.quote("(+,^)")) || reader.hasNext(Pattern.quote("(-,^)"))
                    || reader.hasNext(Pattern.quote("(*,^)")) || reader.hasNext(Pattern.quote("(/,^)")))
            {
                getValue = Expression(token, reader);
            }
            else
            {
                getValue = token[1];
            }
        }
        else
        {
        }
        return getValue;
    }

    static String Condition(String[] token, Scanner reader) throws IOException
    {
        String getValue = "";
        String getString;

        if(token[0].equals("ID") || token[0].equals("INT"))
        {
            getValue = Expression(token, reader);
        }
        else
        {
            SyntaxError();
        }

        getString = getValue;
        token = nextToken(reader);

        if(token[0].equals("RO"))
        {
            getString = getString+" "+token[1];

            token = nextToken(reader);
            if(token[0].equals("ID") || token[0].equals("INT"))
            {
                getValue = Expression(token, reader);
                getString = getString+" "+getValue;
            }
            else
            {
                SyntaxError();
            }
        }
        else
        {
            SyntaxError();
        }
        return getString;
    }

    static void inputDelimiter(String[] token, Scanner reader) throws IOException
    {
        if(token[0].equals(";"))
        {
        }
        else if(token[0].equals(","))
        {
            token = nextToken(reader);
            nextInput(token, reader);
        }
        else
        {
            SyntaxError();
        }
    }

    static void nextInput(String[] token, Scanner reader) throws IOException
    {
        String tacStr;

        if(token[0].equals("ID"))
        {
            tacStr = "in "+token[1];
            outputTac(tacStr);
        }
        else
        {
            SyntaxError();
        }

        token = nextToken(reader);
        inputDelimiter(token, reader);
    }

    static void AssignmentStatement(String[] token, Scanner reader) throws IOException
    {
        String tacStr;

        if(token[0].equals("ID"))
        {
            tacStr = token[1];

            token = nextToken(reader);

            if(token[0].equals("<-"))
            {
                token = nextToken(reader);
                String val = Value(token, reader);

                token = nextToken(reader);
                if(token[0].equals(";"))
                {
                    tacStr = tacStr + " = " + val;
                    outputTac(tacStr);
                }
                else if(forflag3==true && token[0].equals(":"))
                {
                    Path fileName = Path.of("temp.txt");
                    String str = Files.readString(fileName);
                    str = str +'\n';
                    FileWriter fw=new FileWriter("temp.txt");
                    tacStr = str + tacStr + " = " + val;
                    fw.write(tacStr);
                    fw.close();
                }
                else
                {
                    SyntaxError();
                }
            }
            else
            {
                SyntaxError();
            }
        }
    }

    static String Value(String[] token, Scanner reader) throws IOException
    {
        String getValue = "";

        if(reader.hasNext(Pattern.quote("(+,^)")) || reader.hasNext(Pattern.quote("(-,^)"))
                || reader.hasNext(Pattern.quote("(*,^)")) || reader.hasNext(Pattern.quote("(/,^)"))
                || reader.hasNext(Pattern.quote("(%,^)")) || forflag2==true)
        {
            forflag2=false;
            forflag3=true;
            getValue = Expression(token, reader);
        }
        else if(token[0].equals("ID"))
        {
            getValue = token[1];
        }
        else if(token[0].equals("INT"))
        {
            getValue = token[1];
        }
        else if(token[0].equals("LIT"))
        {
            getValue = token[1];
        }
        else
        {
            SyntaxError();
        }
        return getValue;
    }

    static void DT(String[] token, Scanner reader) throws IOException
    {
        if(token[0].equals("INT"))
        {
            System.out.println("DT main to aa gaya hai");
        }
        else if(token[0].equals("CHAR"))
        {
        }
        else
        {
            SyntaxError();
        }
    }

    static void optionAssign(String[] token, Scanner reader) throws IOException
    {
        if(token[0].equals("ID"))
        {
            String tacStr = token[1];


            token = nextToken(reader);
            if(token[0].equals("<-"))
            {
                token = nextToken(reader);
                String temp = Value(token, reader);
                System.out.println("Value se option Assign mian walpis aa gia hai "+ temp);

                if(temp.length()>0)
                {
                    tacStr = tacStr + " <- " + temp;
                    outputTac(tacStr);
                }
            }
            else
            {
                SyntaxError();
            }
        }
    }

    static void Variable(String[] token, Scanner reader) throws IOException
    {
        String tacStr;
        String id="";

        if(token[0].equals("ID"))
        {
            System.out.println(token[1]);
            id=token[1];
        }
        else
        {
            SyntaxError();
        }

        if(reader.hasNext(Pattern.quote("(<-,^)")))
        {
            System.out.println("yahan nhi aa raha shayad");
            optionAssign(token, reader);
        }

        forflag2=false;
        token = nextToken(reader);
        VariableDelimiter(token, reader, id);
    }

    static void VariableDelimiter(String[] token, Scanner reader, String id) throws IOException
    {
        String tacStr;
        String type="";

        if(forflag2==true)
        {
            tacStr = "if " + Condition(token, reader) + " goto ";
            int trackLine1 = n;
            outputTac(tacStr);
            tacStr = "goto ";
            int trackLine2 = n;
            outputTac(tacStr);
            forflag2=false;

        }
        if(forflag==true)
        {
            writeToSymbolTable(id+"\t");
            tacStr = " " + id;
            System.out.println("tacSTr: "+tacStr);
            //outputTac(tacStr);
            forflag=false;
            forflag2=true;
        }

        else if(token[0].equals(","))
        {
            token = nextToken(reader);
            nextVariable(token, reader);
        }

        else{
            if(expressionflag==false){
                DT(token, reader);
                type = token[0];

                token = nextToken(reader);
            }

            if(token[0].equals(";"))
            {
                System.out.println("; bhi mil giaa????");
                writeToSymbolTable(id+"\t"+type);
            }
        }
    }

    static void nextVariable(String[] token, Scanner reader) throws IOException
    {
        String tacStr;
        String type=" type not known";

        if(token[0].equals("ID"))
        {
            writeToSymbolTable(token[1]+"\t"+type);
            tacStr = type.toLowerCase() + " " + token[1];
            outputTac(tacStr);

            if(reader.hasNext(Pattern.quote("(<-,^)")))
            {
                optionAssign(token, reader);
            }

            token = nextToken(reader);
            VariableDelimiter(token, reader, type);
        }
        else
        {
            SyntaxError();
        }
    }

    public void startParsingAndTranslating() throws IOException
    {
        File tokenFile = new File("tokens.txt"); //file for input from lexer

        //creating files, writers and readers
        Scanner tokenReader = new Scanner(tokenFile);
        FileWriter writer = new FileWriter("parser-symboltable.txt");
        writer.close();
        writer = new FileWriter("tac.txt");
        writer.close();

        String[] token; //Token variable

        token = nextToken(tokenReader);

        writeToSymbolTable("SCOPE START");

        Start(token,tokenReader);

        writeToSymbolTable("SCOPE END");

        //closing readers and writers
        tokenReader.close();

        System.out.println("\nParser execution successful! Check parser-symboltable.txt file.");
        System.out.println("\nThree Address Code successfully generated! Check tac.txt file.");
    }
}
