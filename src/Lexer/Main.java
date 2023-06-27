package Lexer;

import Parser.*;
import VM.*;

import javax.swing.text.html.parser.Parser;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.System.out;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner myObj = new Scanner(System.in);
        String filename;

//********************************** main for lexer *****************************

         //Enter filename and press Enter

        System.out.println("Enter filename: ");
//        filename = myObj.nextLine();
//
//        if (!filename.endsWith(".mc"))
//        {
//            System.out.println("Wrong File Extension!! Please enter correct file (with extension .mc)......");
//            exit (0);
//        }
//
//        else {
//            System.out.println("Your filename is: " + filename);
//
//            lexer obj = new lexer();
//            obj.readFile(filename);
//        }




//********************************** main for parser *****************************


        ParsingFunctions obj1 = new ParsingFunctions();
        obj1.startParsingAndTranslating();

//********************************** main for machine code *****************************

        machineCodeGenerator mc = new machineCodeGenerator();
        mc.generateCodeByReadingTacFile("tac_final.txt");

 //********************************** main for VM *****************************


//
//        virtualMachine vm = new virtualMachine();
//        vm.startVM();


    }
}
