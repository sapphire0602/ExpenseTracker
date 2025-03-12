package com.ahmad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpenseManager {

    private static final String FILE_PATH = "expense.json";
    private static int lastId = 0;
    private static final LocalDate date = LocalDate.now();
    private static List<Expense> expenses = new ArrayList<>();
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
        .setPrettyPrinting()
        .create();

    public static void createNewFile() {
        try {
            File jsonFile = new File(FILE_PATH);
            if (!jsonFile.exists()) {
                jsonFile.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveExpensesToFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(expenses, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadExpensesFromFile() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Expense[] loadExpense = gson.fromJson(reader, Expense[].class);
            if (loadExpense != null) { // NullPointer possible here , f**k that exception
                expenses = new ArrayList<>(Arrays.asList(loadExpense));
            }
        } catch (IOException e) {
            System.err.println(
                "Seems your file is empty , Nothing to read there!"
            );
        }
    }

    public static void addExpense(
        String name,
        String description,
        String category,
        double amount
    ) {
        if (expenses == null) {
            expenses = new ArrayList<>();
        }
        LocalDate currentDate = LocalDate.now();

        int maxId = 0;
        if (!expenses.isEmpty()) {
            maxId = expenses.stream().mapToInt(Expense::getId).max().getAsInt();
        }
        lastId = maxId;
        Expense expense = new Expense(
            ++lastId,
            name,
            description,
            amount,
            currentDate,
            category
        );

        System.out.println("New expense created:");
        System.out.println("ID: " + expense.getId());
        System.out.println("Name: " + expense.getName());
        System.out.println("Description: " + expense.getDescription());
        System.out.println("Amount: " + expense.getAmount());
        System.out.println("Date: " + expense.getDateTime());
        expenses.add(expense);
        //        for (Expense e : expenses){
        //            System.out.println(e.getAmount() + ", " + e.getCategory());
        //        }
        System.out.printf(
            "-------------------------- Expense Added Successfully (ID %d) -------------------------- \n",
            expense.getId()
        );
        saveExpensesToFile();
    }

    private static int findExpenseId(int expenseId) {
        for (Expense expense : expenses) {
            if (expense.getId() == expenseId) {
                return expenses.indexOf(expense);
            }
        }
        return -1;
    }

    public static void updateExpense(
        String name,
        String description,
        double amount,
        String category,
        int expenseId
    ) {
        int index = findExpenseId(expenseId);
        if (index != -1) {
            Expense expense = expenses.get(index);
            expense.setName(name);
            expense.setDescription(description);
            expense.setCategory(category);
            expense.setAmount(amount);
            System.out.printf(
                "Task with (expenseId %d) successfully updated !",
                expenseId
            );
            saveExpensesToFile();
        } else {
            System.out.printf(
                "Task with (expenseId %d) doesn't exist!",
                expenseId
            );
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
