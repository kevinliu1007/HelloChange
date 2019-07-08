package com.rocketmiles.hellochange;

import java.util.Scanner;

/**
 * Main executable class for this project.
 *
 * Command line argument format as "java HelloChange ($20 bills) ($10 bills) ($5 bills) ($2 bills) ($1 bills)"
 * to initialize the start up amount of changes that the change machine has from the beginning.
 *
 * Type "put ($20 bills) ($10 bills) ($5 bills) ($2 bills) ($1 bills)" to add additional amount of the bill
 * entered from the command line to the change machine.
 *
 * Type "take ($20 bills) ($10 bills) ($5 bills) ($2 bills) ($1 bills)" to remove amount of the bill entered
 * from the command line to reduce the bills from the change machine.
 *
 * Type "show" to show the current state of the change machine which includes the total dollar amount in the
 * change machine and the amount of each bills in the change machine.
 *
 * Type "change (dollar amount)" to get changes of smaller bills of the given dollar amount.
 *
 * Type "quit" to exit out of the HelloChange program.
 */
public class HelloChange {
    public static void main(String[] args) {
        if (args.length < 5) {
            System.out.println("Wrong command line argument, failed to initialize the program!");
            return;
        }

        Change change;
        try {
            change = new Change(args);
        } catch (NumberFormatException e) {
            return;
        }

        String line;
        Scanner scanner = new Scanner(System.in);
        System.out.println("ready");
        line = scanner.nextLine();

        readInput(line, scanner, change);

        System.out.println("Bye");
    }

    private static void readInput(String line, Scanner scanner, Change change) {
        // Loop for reading input and execute logic
        while(!line.equals("quit")) {
            String[] params = line.split("\\s+");

            switch(params[0]){
                case "put":
                    if (params.length != 6) {
                        printWrongInput();
                        break;
                    }
                    change.putAndTake(params, true);
                    break;
                case "show":
                    change.show();
                    break;
                case "take":
                    if (params.length != 6) {
                        printWrongInput();
                        break;
                    }
                    change.putAndTake(params, false);
                    break;
                case "change":
                    if (params.length != 2) {
                        printWrongInput();
                        break;
                    }
                    change.makeChange(Integer.valueOf(params[1]));
                    break;
                default:
                    printWrongInput();
                    break;
            }

            line = scanner.nextLine();
        }
    }

    private static void printWrongInput() {
        System.out.println("Wrong input, please try again!");
    }
}
