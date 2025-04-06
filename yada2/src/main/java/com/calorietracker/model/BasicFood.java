package com.calorietracker.model;

import java.util.List;

/**
 * Represents a basic food item with a name, keywords, and calories per serving.
 */
public class BasicFood extends Food {
    private double calories;
    
    public BasicFood(String name, List<String> keywords, double calories) {
        super(name, keywords);
        this.calories = calories;
    }
    
    @Override
    public double getCaloriesPerServing() {
        return calories;
    }
    
    public void setCalories(double calories) {
        this.calories = calories;
    }
}
