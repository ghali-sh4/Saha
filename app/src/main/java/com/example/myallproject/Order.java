package com.example.myallproject;

import java.util.ArrayList;
public class Order {
    private String phoneNumber;
    private ArrayList<Meal> items;
    private double total;
    private long timestamp;

    public Order() {}

    public Order(String phoneNumber, ArrayList<Meal> items, double total) {
        this.phoneNumber = phoneNumber;
        this.items = items;
        this.total = total;
        this.timestamp = System.currentTimeMillis();
    }

    public String getPhoneNumber() { return phoneNumber; }
    public ArrayList<Meal> getItems() { return items; }
    public double getTotal() { return total; }
    public long getTimestamp() { return timestamp; }
}

