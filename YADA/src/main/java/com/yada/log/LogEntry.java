package com.yada.log;

import com.yada.food.Food;
import com.yada.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class LogEntry {
    private String date; // Date in YYYY-MM-DD format
    private List<Pair<Food, Integer>> consumedFoods; // Foods and servings

    // Constructor
    public LogEntry(String date) {
        this.date = date;
        this.consumedFoods = new ArrayList<>();
    }

    // Add the missing getDate() method
    public String getDate() {
        return date;
    }

    // Add a food to the daily log
    public void addFood(Food food, int servings) {
        consumedFoods.add(new Pair<>(food, servings));
    }

    // Remove a food from the daily log
    public void removeFood(Food food) {
        consumedFoods.removeIf(entry -> entry.getKey().equals(food));
    }

    // Display log entry (for debugging)
    public void displayLog() {
        System.out.println("Log Date: " + date);
        for (Pair<Food, Integer> entry : consumedFoods) {
            System.out.println("- " + entry.getKey().getId() + ": " + entry.getValue() + " servings");
        }
    }

    public List<Pair<Food, Integer>> getConsumedFoods() {
        return consumedFoods;
    }
}
