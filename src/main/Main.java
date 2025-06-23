package main;

import java.util.*;
import commands.InputHandler;

public class Main {
    public static Scanner ask = new Scanner(System.in);
    static boolean del = true;
    static String indent = ">"; 
    static InputHandler handler = new InputHandler();
    public static void main(String[] args) {
        System.out.println("CLIIIIII v1.betaaaaaa");
        while(del) {
            System.out.print(indent+" ");
            String input = ask.next();
            switch(input) {
                case "ex":
                System.out.println("bye");
                del = false;
                break;
                case "":
                System.out.println("hmm");
                break;
                case "mut":
                String arg = ask.next();
                customize(arg);
                break;
                case "help":
                handler.showGuides();
                break;
                default:
                handler.handle(input);
            }
        }
    }
    
    static void customize(String custom) {
        switch(custom) {
            case "--lvl":
            String change = ask.next();
            indent = change;
            System.out.println("Indent changed successfully");
            break;
            default:
            System.out.println("Invalid Input");
        }
    }
    
}