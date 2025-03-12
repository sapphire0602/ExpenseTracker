package com.ahmad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpenseManager {

    // Set the default file name to ".expense_tracker.json" will be hidden by default in most file manager.
    private static final String FILE_NAME = ".expense_tracker.json";
    private int lastId = 0;
    private final LocalDate date = LocalDate.now();
    private List<Expense> expenses = new ArrayList<>();
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
        .setPrettyPrinting()
        .create();

    public void saveExpenses() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(expenses, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadExpenses() {
        try (FileReader reader = new FileReader(FILE_NAME)) {
            expenses = Arrays.asList(gson.fromJson(reader, Expense[].class));
        } catch (Exception e) { // try (FileReader reader = new FileReader(FILE_NAME) {
            System.err.println(e.getMessage());
        }
    }

    public void addExpense(
        String name,
        String description,
        String category,
        double amount
    ) {
        // Set the initial ID to 1
        int id = 1;

        // Check if the list is not empty and retrieve the last ID of the expense
        // then set the initial ID to the last ID + 1
        if (!expenses.isEmpty()) {
            final int lastID = expenses.getLast().getId();
            id = lastID + 1;
        }

        Expense expense = new Expense(
            id,
            name,
            description,
            amount,
            LocalDate.now(),
            category
        );

        expenses.add(expense);
        System.out.println("New expense created:");
        System.out.println("ID: " + expense.getId());
        System.out.println("Name: " + expense.getName());
        System.out.println("Description: " + expense.getDescription());
        System.out.println("Amount: " + expense.getAmount());
        System.out.println("Date: " + expense.getDateTime());
        System.out.printf(
            "-------------------------- Expense Added Successfully (ID %d) -------------------------- \n",
            expense.getId()
        );
        saveExpenses();
    }

    public void updateExpense(
        String name,
        String description,
        double amount,
        String category,
        int pos
    ) {
        // Update expenses based on the position which you can check by listing all the expenses.
        if (pos > 0 && pos <= expenses.size()) {
            // Adjust for 0-based index
            Expense expense = expenses.get(pos - 1);

            // Update the fields only if they're not empty.
            if (!name.isBlank()) {
                expense.setName(name);
            }

            if (!description.isBlank()) {
                expense.setDescription(description);
            }

            if (!category.isBlank()) {
                expense.setCategory(category);
            }

            if (amount > 0) {
                expense.setAmount(amount);
            }

            System.out.printf(
                "Task with (expenseId %d) successfully updated !",
                pos
            );
            saveExpenses();
        } else {
            System.out.printf("Task with (expenseId %d) doesn't exist!", pos);
        }
    }

    public static void listAllExpenses() {
        loadExpensesFromFile();
        System.out.format(
            "%-5s %-12s %-20s %-30s %-10s\n",
            "ID",
            "DATE",
            "NAME",
            "DESCRIPTION",
            "AMOUNT"
        );
        System.out.println(
            "-----------------------------------------------------------------------------------------"
        );

        for (Expense expense : expenses) {
            System.out.format(
                "%-5d %-12s %-20s %-30s $%-10.2f\n",
                expense.getId(),
                expense.getDateTime(),
                expense.getName(),
                expense.getDescription(),
                expense.getAmount()
            );
        }
    }

    public static void summarizeExpenses() {
        loadExpensesFromFile(); // get total
        double totalExpenses = expenses
            .stream()
            .mapToDouble(Expense::getAmount)
            .sum();
        //                .reduce(0.0, (a, b) -> a + b); //can use method reference Double :: sum instead
        System.out.println("Summary : " + totalExpenses);
    }

    public static void deleteExpense(int expenseId) {
        loadExpensesFromFile();
        boolean removeExpense = expenses.removeIf(expense ->
            (expenseId == expense.getId())
        );
        if (removeExpense) {
            System.out.println(
                "Expense with ID : " + expenseId + "  Successfully deleted !"
            );
        } else {
            System.out.println(
                "Failed to delete expense with ID : " +
                expenseId +
                " , because it doesn't exist!"
            );
        }
        saveExpensesToFile();
        //        Stream<Expense> expenseIdToDelete = expenses.stream().filter(e -> (e.getId() == expenseId));
        //        expenses.remove(expenseIdToDelete);
    }

    public static void summarizeByMonth(Month month) {
        loadExpensesFromFile();
        double totalAmountForMonth = expenses
            .stream()
            .filter(e -> (e.getDateTime().getMonth() == month))
            .map(Expense::getAmount)
            .reduce(0.0, (a, b) -> a + b);

        System.out.println("Month Expense : " + totalAmountForMonth);
    }

    public static void clearAll() {
        loadExpensesFromFile();
        try {
            FileWriter writer = new FileWriter(FILE_PATH);
            writer.write("[]");
            writer.close();

            expenses.clear();
            saveExpensesToFile();
            System.out.println("All expenses cleared successfully");
        } catch (IOException e) {
            System.out.println("Unable to clear file data !");
        }
    }
}
