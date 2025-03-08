package com.ahmad;

import java.time.Month;
import java.util.Objects;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static String command;

    public static void main(String[] args) {
        System.out.println("----------- WELCOME TO EXPENSE TRACKER CLI APP (input <help> to get familiar with the commands!) --------------");
        if (args.length==0){
            getHelp();
        }

        ExpenseManager.createNewFile();
        ExpenseManager.loadExpensesFromFile();

        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.print("\nexpense-tracker >");
            System.out.flush();
            String input = scanner.nextLine().trim();

            handleExpenseMethods(input);
        }
    }

    public static void handleExpenseMethods(String input) {
        String[] args = input.trim().split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        if (args.length == 0) {
            System.out.println("Please, Enter an argument !");
            return;
        }
        try {
            command = args[0].toLowerCase();
        } catch (Exception e) {
            System.out.println("re enter the args");
        }
        switch (command) {
            case "add":
                addExpense(args);
                break;

            case "list":
                list();
                break;

            case "summary":
                summary(args);
                break;

            case "delete":
                delete(args);
                break;

            case "update":
                update(args);
                break;

            case "help":
                getHelp();
                break;

            case "clear":
                clear();
                break;

            case "exit":
                ExpenseManager.saveExpensesToFile();
                System.out.println("----------------EXITING THE APPLICATION---------------");
                System.exit(0);

            default:
                System.err.println("Invalid Input, Please Try Again!");
        }

        ExpenseManager.saveExpensesToFile();
    }

    public static void addExpense(String[] args) {
        String name = null;
        String description = null;
        double amount = -1;
        String category = null;
        for (int i = 1; i < args.length; i += 2) {
            switch (args[i]) {
                case "--name":
                    name = args[i + 1].trim().replaceAll("^\"|\"$", "");
                    break;
                case "--description":
                    description = args[i + 1].trim().replaceAll("^\"|\"$", "");
                    break;
                case "--category":
                    category = args[i + 1].trim().replaceAll("^\"|\"$", "");
                    break;
                case "--amount":
                    amount = Double.parseDouble(args[i + 1]);
                    break;
            }
        }
        ExpenseManager.addExpense(name, description, category, amount);
        if (name == null || description == null || category == null || amount == -1) {
            System.err.println("Error: Missing required fields. Usage: add --name <name> --description <desc> --category <cat> --amount <amount>");
        }
    }

    public static void list() {
        ExpenseManager.listAllExpenses();
    }

    public static void summary(String[] args) {
        if (args.length == 1) {
            ExpenseManager.summarizeExpenses();
        } else {
            if (args[1].equals("--month")) {
                try {
                    Month month = Month.valueOf(args[2].toUpperCase());
                    ExpenseManager.summarizeByMonth(month);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void delete(String[] args) {
        if (args.length == 3 && args[1].equals("--id")) {
            ExpenseManager.deleteExpense(Integer.parseInt(args[2]));
        }
    }

    public static void update(String[] args) {
        String name = null;
        String description = null;
        double amount = -1;
        String category = null;
        int expenseId = -1;
        for (int i = 1; i < args.length; i += 2) {
            switch (args[i]) {
                case "--id":
                    expenseId = Integer.parseInt(args[i + 1]);
                    break;
                case "--name":
                    name = args[i + 1].replaceAll("^\"|\"$", "");
                    break;
                case "--description":
                    description = args[i + 1].replaceAll("^\"|\"$", "");
                    break;
                case "--category":
                    category = args[i + 1].replaceAll("^\"|\"$", "");
                    break;
                case "--amount":
                    amount = Double.parseDouble(args[i + 1]);
            }
        }
        if (expenseId <= 0) {
            System.err.println("Invalid Expense Id");
        }
        ExpenseManager.updateExpense(name, description, amount, category, expenseId);
    }

    public static void clear() {
        ExpenseManager.clearAll();
    }

    private static  void getHelp(){
        System.out.println("\nCommands:");
        System.out.println("  add --name <name> --description <description> --category <category> --amount <amount>  => [Add expense]");
        System.out.println("  list => [Show all expenses]");
        System.out.println("  summary --month <month>  => [Show monthly-expense summary]");
        System.out.println("  delete --id <id>   => [Remove an expense]");
        System.out.println("  update --id <id> --description <description> -- <category> ......  => [Edit an expense]");
        System.out.println("  clear => [Remove all expenses]");
        System.out.println("  help  => [Show the help message]");
        System.out.println("  exit => Save & exit the lovely application.");
    }
}
