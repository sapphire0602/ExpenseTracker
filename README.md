# Expense Tracker CLI

## Overview
The **Expense Tracker CLI** is a command-line application built in Java to help users track their expenses. Users can add, list, update, delete, and summarize expenses [all / per month] through simple CLI commands.

## Features
- Add an expense with name, description, category, and amount.
- List all recorded expenses.
- Summarize expenses by month.
- Update or delete expenses by ID.
- Clear all expenses.
- Save and load expenses automatically.

## Prerequisites
Ensure you have the following installed:
- Java 17

## Installation
1. Clone this repository:
   ```sh
   git clone https://github.com/sapphire0602/ExpenseTracker.git
   ```
2. Navigate to the project folder:
   ```sh
   cd ExpenseTracker
   cd target
   ```
3. Run the application:
   ```sh
   java -jar ExpenseTracker-1.0-SNAPSHOT.jar
   ```

## Usage
To use the application, type commands as shown below:

### Add an Expense
```sh
add --name "Garri" --description "Weekly garri shopping" --category "Food" --amount 1500
```

### List All Expenses
```sh
list
```

### Summarize Expenses (All Time)
```sh
summary
```

### Summarize Expenses by Month
```sh
summary --month JANUARY
```

### Update an Expense
```sh
update --id 1 --name "Dinner" --amount 30.00
```

### Delete an Expense
```sh
delete --id 1
```

### Clear All Expenses
```sh
clear
```

### Show Help
```sh
help
```

### Exit the Application
```sh
exit
```
### Can also find it here https://roadmap.sh/projects/expense-tracker

## File Storage
The application automatically saves expenses to a file and loads them on startup.

---

### Dev
Developed by **Ahmad** 

