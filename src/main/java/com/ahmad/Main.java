package com.ahmad;

import java.time.Month;
import java.util.Scanner;

public class Main {

    public static String command;

    public static void main(String[] args) {
        System.out.println(
            "----------- WELCOME TO EXPENSE TRACKER CLI APP (input <help> to get familiar with the commands!) --------------"
        );
        if (args.length == 0) {
            getHelp();
        }

        ExpenseManager expenseManager = new ExpenseManager();
        expenseManager.loadExpenses();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("\nexpense-tracker >");
                System.out.flush();
                String input = scanner.nextLine().trim();

                handleExpenseMethods(expenseManager, input);
            }
        }
    }

    public static void handleExpenseMethods(
        ExpenseManager manager,
        String input
    ) {
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
                addExpense(manager, args);
                break;
            case "list":
                list(manager);
                break;
            case "summary":
                summary(manager, args);
                break;
            case "delete":
                delete(manager, args);
                break;
            case "update":
                update(manager, args);
                break;
            case "help":
                getHelp();
                break;
            case "clear":
                clear(manager);
                break;
            case "exit":
                System.out.println(
                    "----------------EXITING THE APPLICATION---------------"
                );
                System.exit(0);
                break;
            default:
                System.err.println("Invalid Input, Please Try Again!");
        }
    }

    public static void addExpense(ExpenseManager manager, String[] args) {
        String name = "";
        String description = "";
        double amount = 0;
        String category = "";

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

        if (name == "" || description == "" || category == "" || amount == 0) {
            System.err.println(
                "Error: Missing required fields. Usage: add --name <name> --description <desc> --category <cat> --amount <amount>"
            );
            return;
        }

        manager.addExpense(name, description, category, amount);
    }

    public static void list(ExpenseManager manager) {
        manager.listAllExpenses();
    }

    public static void summary(ExpenseManager manager, String[] args) {
        if (args.length == 1) {
            manager.summarizeExpenses();
        } else {
            if (args[1].equals("--month")) {
                try {
                    Month month = Month.valueOf(args[2].toUpperCase());
                    manager.summarizeByMonth(month);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void delete(ExpenseManager manager, String[] args) {
        if (args.length == 3 && args[1].equals("--id")) {
            manager.deleteExpense(Integer.parseInt(args[2]));
        }
    }

    public static void update(ExpenseManager manager, String[] args) {
        String name = "";
        String description = "";
        double amount = 0;
        String category = "";
        int expenseId = 0;

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

        manager.updateExpense(name, description, amount, category, expenseId);
    }

    public static void clear(ExpenseManager manager) {
        manager.clearAll();
    }

    private static void getHelp() {
        System.out.println("\nCommands:");
        System.out.println(
            "  add --name <name> --description <description> --category <category> --amount <amount>  => [Add expense]"
        );
        System.out.println("  list => [Show all expenses]");
        System.out.println(
            "  summary --month <month>  => [Show monthly-expense summary]"
        );
        System.out.println("  delete --id <id>   => [Remove an expense]");
        System.out.println(
            "  update --id <id> --description <description> -- <category> ......  => [Edit an expense]"
        );
        System.out.println("  clear => [Remove all expenses]");
        System.out.println("  help  => [Show the help message]");
        System.out.println("  exit => Save & exit the lovely application.");
    }
}
