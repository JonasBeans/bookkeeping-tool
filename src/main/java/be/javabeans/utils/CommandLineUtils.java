package be.javabeans.utils;

import java.util.Scanner;
import java.util.function.Predicate;

public class CommandLineUtils {
    private static Scanner inputScanner = new Scanner(System.in);

    public static String getCommandLineInput(){
        return getCommandLineInput((choice) -> true);
    }
    public static String getCommandLineInput(Predicate<String> validation){
        System.out.print("> ");
        String userInput = inputScanner.next();
        while(validation.negate().test(userInput)){
            System.out.println("Not a valid option");
            System.out.print("> ");
            userInput = inputScanner.next();
        }
        return userInput;
    }
}
