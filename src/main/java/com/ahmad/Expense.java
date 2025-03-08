package com.ahmad;

import java.time.LocalDate;

public class Expense {
    private int id;
    private String name;
    private String description;
    private double amount;
    private LocalDate dateTime;
    private String category;

    public Expense(int id, String name, String description, double amount, LocalDate dateTime, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.dateTime = dateTime;
        this.category = category;
    }
    public Expense(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                ", category='" + category + '\'' +
                '}';
    }
}
