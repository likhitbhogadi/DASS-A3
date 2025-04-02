package com.calorietracker.model;

import java.time.LocalDateTime;

public class FoodEntry {
    private Food food;
    private double servings;
    private LocalDateTime timestamp;
    
    public FoodEntry(Food food, double servings, LocalDateTime timestamp) {
        this.food = food;
        this.servings = servings;
        this.timestamp = timestamp;
    }
    
    public Food getFood() {
        return food;
    }
    
    public double getServings() {
        return servings;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public double getTotalCalories() {
        return food.getCaloriesPerServing() * servings;
    }
}
