package com.example.java8.spliterator;

public class Payment {
    private String category;
    private double amount;

    public Payment(double amount, String category) {
        this.amount = amount;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}
