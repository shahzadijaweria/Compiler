package VM;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//public class machineCodeGenerator {
//
//     int t = 1;                                   //used to track number for temporary variable generation
//     int relativeAddress = 0;                     //used to track relative address for translator's symbol table
//     List<List> machineCode = new ArrayList<>();  //used to store machine code for each line of three-address code
//
//    public void setTempVarCount(int count)
//    {
//        t = count;
//    }
//
//    public void setRelativeAddress(int addr)
//    {
//        relativeAddress = addr;
//    }
//
//    public List getMachineCode()
//    {
//        return machineCode;
//    }
//
//     String generateTempVar()
//    {
//        String tempVar = "t"+t;
//        t++;
//        return tempVar;
//    }
//
//     void writeToTranslatorSymbolTable(String s, String type, String initVal) throws IOException
//    {
//        FileWriter writer = new FileWriter("translator-symboltable.txt",true);
//
//        if(initVal.length() > 0)
//        {
//            writer.append(s+"\t"+type+"\t"+relativeAddress+"\t"+initVal+"\n");
//        }
//        else
//        {
//            writer.append(s+"\t"+type+"\t"+relativeAddress+"\n");
//        }
//        writer.close();
//
//        //update relative address accordingly
//        if(type.equals("INT"))
//        {
//            relativeAddress += 4;
//        }
//        else if(type.equals("CHAR"))
//        {
//            relativeAddress += 1;
//        }
//        else if(type.equals("STR"))
//        {
//            relativeAddress += initVal.length()-2;
//        }
//    }
//
//     void addMachineCode(String opCode, String op1, String op2, String op3)
//    {
//        List<String> mc = new ArrayList<>();
//        mc.add(opCode);
//        mc.add(op1);
//        mc.add(op2);
//        mc.add(op3);
//
//        machineCode.add(mc);
//    }
//
//     String getRelativeAddress(String var) throws FileNotFoundException {
//        File file = new File("translator-symboltable.txt");
//        Scanner reader = new Scanner(file);
//        String address = "";
//
//        while(reader.hasNext())
//        {
//            String temp = reader.nextLine();
//            String[] str = temp.split("\t");
//            if(str[0].contentEquals(var))
//            {
//                address = str[2];
//                break;
//            }
//        }
//
//        reader.close();
//        return address;
//    }
//
//    boolean isNumericConstant(String s)
//    {
//        if(s.charAt(0) >= '0' && s.charAt(0) <= '9')
//            return true;
//        return false;
//    }
//
//     boolean isLiteralConstant(String s)
//    {
//        if(s.startsWith("'") && s.endsWith("'"))
//            return true;
//        return false;
//    }
//
//     String getOperandAddress(String var) throws IOException {
//        String address = "";
//
//        if(isNumericConstant(var))
//        {
//            String tempVar = generateTempVar();
//            writeToTranslatorSymbolTable(tempVar, "INT", var);
//            address = getRelativeAddress(tempVar);
//        }
//        else if(isLiteralConstant(var))
//        {
//            String tempVar = generateTempVar();
//            writeToTranslatorSymbolTable(tempVar, "CHAR", var);
//            address = getRelativeAddress(tempVar);
//        }
//        else
//            address = getRelativeAddress(var);
//
//        return address;
//    }
//
//     String getOpCodeForArithmeticOperator(String s)
//    {
//        if(s.contentEquals("+"))
//            return "1";
//        else if(s.contentEquals("-"))
//            return "2";
//        else if(s.contentEquals("*"))
//            return "3";
//        else if(s.contentEquals("/"))
//            return "4";
//        else
//            return "";
//    }
//
//     String getOpCodeForRelationalOperator(String s)
//    {
//        if(s.contentEquals("LT"))
//            return "6";
//        else if(s.contentEquals("LE"))
//            return "7";
//        else if(s.contentEquals("GT"))
//            return "8";
//        else if(s.contentEquals("GE"))
//            return "9";
//        else if(s.contentEquals("EQ"))
//            return "10";
//        else if(s.contentEquals("NE"))
//            return "11";
//        else
//            return "";
//    }
//
//     void tacToMachineCodeConversion() throws IOException {
//        File file = new File("tac.txt");
//        Scanner reader = new Scanner(file);
//        FileWriter mcWriter = new FileWriter("machine-code.txt");
//
//        int counter = 1;    //to track line numbers of tac instructions
//        while(reader.hasNext())
//        {
//            String opCode = "-1", op1 = "-1", op2 = "-1", op3 = "-1";   //default values
//
//            String temp = reader.nextLine();
//            temp = temp.substring(Integer.toString(counter).length(), temp.length());
//            temp = temp.trim();
//
//            String[] str = temp.split(" ");
//
//            //variable declaration statements
//            if(str[0].equalsIgnoreCase("INT") || str[0].equalsIgnoreCase("CHAR"))
//            {
//                if(str[0].equalsIgnoreCase("INT"))
//                    opCode = "15";
//                else
//                    opCode = "16";
//
//                op1 = getRelativeAddress(str[1]);
//                mcWriter.write(opCode+" "+op1+" "+op2+" "+op3+"\n");
//                addMachineCode(opCode,op1,op2,op3);
//            }
//            //goto statements
//            else if(str[0].equalsIgnoreCase("goto"))
//            {
//                opCode = "14";
//                op1 = str[1];
//                mcWriter.write(opCode+" "+op1+" "+op2+" "+op3+"\n");
//                addMachineCode(opCode,op1,op2,op3);
//            }
//            //input statements
//            else if(str[0].equalsIgnoreCase("in"))
//            {
//                opCode = "12";
//                op1 = getRelativeAddress(str[1]);
//                mcWriter.write(opCode+" "+op1+" "+op2+" "+op3+"\n");
//                addMachineCode(opCode,op1,op2,op3);
//            }
//            //output statements
//            else if(str[0].equalsIgnoreCase("out"))
//            {
//                opCode = "13";
//                if(isNumericConstant(str[1]) || isLiteralConstant(str[1]))
//                    op1 = getOperandAddress(str[1]);
//                else
//                    op1 = getRelativeAddress(str[1]);
//                mcWriter.write(opCode+" "+op1+" "+op2+" "+op3+"\n");
//                addMachineCode(opCode,op1,op2,op3);
//            }
//            //if statements
//            else if(str[0].equalsIgnoreCase("if"))
//            {
//                opCode = getOpCodeForRelationalOperator(str[2]);
//                op1 = getOperandAddress(str[1].substring(1,str[1].length())); //ignoring the ( in condition
//                op2 = getOperandAddress(str[3].substring(0,str[3].length()-1)); //ignoring the ) in condition
//                op3 = str[str.length - 1];
//
//                mcWriter.write(opCode+" "+op1+" "+op2+" "+op3+"\n");
//                addMachineCode(opCode,op1,op2,op3);
//            }
//            //Assignment statements and arithmetic operations
//            else if(str.length >= 3)
//            {
//                if(str[1].contentEquals("="))
//                {
//                    if(str[2].startsWith("\"") || str[2].startsWith("'"))
//                    {
//                        opCode = "5";
//                        op1 = getRelativeAddress(str[0]);
//                    }
//                    else
//                    {
//                        if(str.length > 3)
//                        {
//                            if(str[3].contentEquals("+") || str[3].contentEquals("-") || str[3].contentEquals("*") || str[3].contentEquals("/"))
//                            {
//                                opCode = getOpCodeForArithmeticOperator(str[3]);
//                                op1 = getOperandAddress(str[2]);
//                                op2 = getOperandAddress(str[4]);
//                                op3 = getRelativeAddress(str[0]);
//                            }
//                        }
//                        else
//                        {
//                            opCode = "5";
//                            op1 = getOperandAddress(str[2]);
//                            op2 = getRelativeAddress(str[0]);
//                        }
//                    }
//
//                    mcWriter.write(opCode+" "+op1+" "+op2+" "+op3+"\n");
//                    addMachineCode(opCode,op1,op2,op3);
//                }
//            }
//            counter++;
//        }
//        mcWriter.close();
//    }
//
//    public void startConversion() throws IOException
//    {
//        tacToMachineCodeConversion();
//    }
//}


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class machineCodeGenerator {
private String id;
//public int initValueForInt;
private String initValueForString;
private String relativeAddr;
private String type;
private boolean isTacFileEnd;
public ArrayList<String> obj;
public ArrayList<ArrayList> arrayOfObj;

public int relativeAddressForSymbolTable;

public machineCodeGenerator(){
    id=  null;
    //initValueForInt = -1;
    initValueForString = null;
    relativeAddr = null;
    type = null;
    relativeAddressForSymbolTable =0;
    isTacFileEnd = false;
    obj = new ArrayList<>();
    arrayOfObj = new ArrayList<>();
}

    public String getId() {
        return id;
    }

    public String getInitValueForString(String variable) throws FileNotFoundException{
        File file = new File("symbolTableForTranslator.txt");
        Scanner reader = new Scanner(file);

        while(reader.hasNext())
        {
            String temp = reader.nextLine();
            String[] str = temp.split("\t");
            if(str[0].contentEquals(variable))
            {
                initValueForString = str[1];  // 4th index will have relativeAddr
                break;
            }
        }

        reader.close();
         return initValueForString;
    }

    public String getType() {
        return type;
    }

    public String getRelativeAddr(String variable) throws FileNotFoundException{
    File file = new File("symbolTableForTranslator.txt");
    Scanner reader = new Scanner(file);

    while(reader.hasNext())
    {
        String temp = reader.nextLine();
        String[] str = temp.split("\t");
        if(str[0].contentEquals(variable))
        {
            relativeAddr = str[3];  // 4th index will have relativeAddr
            break;
        }
    }

    reader.close();
    return relativeAddr;
}

    public void setId(String id) {
        this.id = id;
    }

    public void setInitValueForString(String initValueForString) {
        this.initValueForString = initValueForString;
    }

    public void setRelativeAddr(String relativeAddr) {
        this.relativeAddr = relativeAddr;
    }

    public void setType(String type) {
        this.type = type;
    }

    //public void writeToTranslatorSymbolTable(String s, String type, String initVal) throws IOException {
    public void writeToTranslatorSymbolTable() throws IOException {
         FileWriter writer = new FileWriter("symbolTableForTranslator.txt",true);
               writer.write(id+"\t"+initValueForString+"\t"+type+"\t"+relativeAddressForSymbolTable+"\t"+"\n");


            //update relative address according to the type of variable
            if (type.equals("int")) {
                relativeAddressForSymbolTable += 4;
            } else if (type.equals("char")) {
                relativeAddressForSymbolTable += 1;
            } else if (type.equals("str")) {
                relativeAddressForSymbolTable += 2;
            }


            writer.close();



    }

    public boolean isNumericLiteral(String s)
    {
        if(s.charAt(0) >= '0' && s.charAt(0) <= '9')
            return true;
        return false;
    }

    public boolean isCharacterLiteral(String s)
    {
        if(s.startsWith("'") && s.endsWith("'"))
            return true;
        return false;
    }

    public boolean isString(String s)
    {
        if(s.startsWith("\"") && s.endsWith("\""))
            return true;
        return false;
    }

    public String getOperandAddr(String var, String operand) throws IOException { // operand is str[0] i.e. destiantion variable address, which is used to setID()
        String address = "";

        if(isNumericLiteral(var))
        {
            setType("int");
            setInitValueForString(var);
            setId(operand);
            writeToTranslatorSymbolTable();
            address = getRelativeAddr(operand);
        }
        else if(isCharacterLiteral(var))
        {
            setType("char");
            setInitValueForString(var);
            setId(operand);
            writeToTranslatorSymbolTable();
            address = getRelativeAddr(operand);
        }
        else if(isString(var))
        {
            setType("str");
            setInitValueForString(var);
            setId(operand);
            writeToTranslatorSymbolTable();
            address = getRelativeAddr(operand);
        }
        else
            address = getRelativeAddr(var);

        return address;
    }


    public String getOpCodeForArithmeticOperator(String s)
    {
        if(s.equals("+"))
            return "3";
        else if(s.equals("-"))
            return "4";
        else if(s.equals("*"))
            return "5";
        else if(s.equals("/"))
            return "6";
        else if(s.equals("%"))
            return "7";
        else
            return "";
    }

    public String getOpCodeForRelationalOperator(String s)
    {
        if(s.equals("LT"))
            return "15";
        else if(s.equals("LE"))
            return "16";
        else if(s.equals("GT"))
            return "17";
        else if(s.equals("GE"))
            return "18";
        else if(s.equals("EQ"))
            return "19";
        else if(s.equals("NE"))
            return "20";
        else
            return "";
    }



    public void generateCodeByReadingTacFile (String filename) throws IOException {
    ArrayList<String> trackOfFirstOccuranceOfVar = new ArrayList<>();
    File file = new File(filename);
    Scanner reader = new Scanner(file);
    FileWriter mcWriter = new FileWriter("mc_machine_code.txt");
//    ArrayList <String> machineCode = new ArrayList<>();
//    String mc = null;

    int counter = 1;    //to track line numbers of tac
    while(reader.hasNext())
    {
        String opCode = "-1", op1 = "-1", op2 = "-1", op3 = "-1";   //default values for machine code parameters

        String temp = reader.nextLine();
        temp = temp.substring(Integer.toString(counter).length(), temp.length());
        temp = temp.trim();

        String[] str = temp.split(" ");  // split str based on whitespace


        //variable declaration statements
        if(str[0].equals("int") || str[0].equals("char") )
        {
            if(str[0].equals("int"))
                opCode = "1";
            else
                opCode = "2";

            op1 = getRelativeAddr(str[1]);
            mcWriter.write(opCode+" "+op1+" "+op2+" "+op3+"\n");

        }
        //goto statements
        else if(str[0].equals("goto"))
        {
            opCode = "11";
            op1 = str[1];
            mcWriter.write(opCode+" "+op1+" "+op2+" "+op3+"\n");

        }
        //input statements
        else if(str[0].equals("in"))
        {
            opCode = "8";
            op1 = getRelativeAddr(str[1]);
            mcWriter.write(opCode+" "+op1+" "+op2+" "+op3+"\n");
        }
        //output statements
        else if(str[0].equals("out"))
        {
            opCode = "9";

                op1 = getRelativeAddr(str[1]);
            mcWriter.write(opCode+" "+op1+" "+op2+" "+op3+"\n");
        }

        // for return statement
        else if (str[0].equals("return")){
            opCode = "14";
            op1 = getRelativeAddr(str[1]); // statement is written like "return t" (where t<-0)
            mcWriter.write(opCode+" "+op1+" "+op2+" "+op3+"\n");
        }

        //if statements
        else if(str[0].equals("if"))
        {
            opCode = getOpCodeForRelationalOperator(str[2]);

            op1 = getRelativeAddr(str[1]);
            op2 = getRelativeAddr(str[3]);
            op3 = str[str.length - 1];      // "if x LT y goto linenumber" // it is used to extract "linenumber" from this string

            mcWriter.write(opCode+" "+op1+" "+op2+" "+op3+"\n");
        }
        //Assignment statements and arithmetic operations
        else if(str.length >= 3)
        {
            if(str[1].equals("<-"))  // Assignment statement for string or char
            {

                if(str[2].startsWith("\"") || str[2].startsWith("'"))  // if it is a string or char
                {  // whenever this statement comes in tac, it follows the format with temp variable
                    // e.g  t<-"str" OR t<-'char'
                    opCode = "10";
                   // op1 = getRelativeAddr(str[0]);
                    op1 = getOperandAddr(str[2],str[0]);

                }
                else
                {
                    if(str.length > 3)  //Assignment statement for AO
                    {
                        if(str[3].equals("+") || str[3].equals("-") || str[3].equals("*") || str[3].equals("/") || str[3].equals("%"))
                        {
                            opCode = getOpCodeForArithmeticOperator(str[3]);
//                            op1 = getOperandAddr(str[2]);
//                            op2 = getOperandAddr(str[4]);
                            op1 = getRelativeAddr(str[2]);
                            op2 = getRelativeAddr(str[4]);
                            op3 = getRelativeAddr(str[0]);
                        }
                    }
                    else  // Assignment statement for statements like  t<-2, x<-t
                    {
                        opCode = "10";

                        if (isNumericLiteral(str[2])) {// dealing with this case i.e. t<-2
                            op1 = getOperandAddr(str[2],str[0]);
                        }
                        else{// dealing with this case i.e. x<-t

                            op1 = getOperandAddr(str[2], str[0]); // address of t
                            if (trackOfFirstOccuranceOfVar.isEmpty() || !(trackOfFirstOccuranceOfVar.contains(str[0])))
                            {
                                /// if arrayList is empty or if var is already not added inlist, then add
                                   // variable in arraylist as well as in symboltable

                                setId(str[0]);
                                initValueForString = getInitValueForString(str[2]);
                                if (initValueForString.startsWith("\"")) // if init value is str
                                    type = "str";
                                else if (initValueForString.startsWith("\'")) // if init value is char
                                    type = "char";
                                else
                                    type = "int";
                                setType(type);
                                setInitValueForString(str[2]);
                                relativeAddr = getRelativeAddr(str[0]);    // address of destination i.e. x in this case
                                setRelativeAddr(relativeAddr);
                                writeToTranslatorSymbolTable();

                                trackOfFirstOccuranceOfVar.add(str[0]);
                            }
                            op2 = getRelativeAddr(str[0]);  ;           // address of destination i.e. x in this case

                        }
                    }
                }

                mcWriter.write(opCode+" "+op1+" "+op2+" "+op3+"\n");
            }
        }
        counter++;
    }
    mcWriter.close();
    isTacFileEnd = true;
    // now write all codes to symbol table
        writeToTranslatorSymbolTable();

    }


}