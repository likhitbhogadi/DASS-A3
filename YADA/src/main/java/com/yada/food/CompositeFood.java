package com.yada.food;

import java.util.List;
import java.util.Map;

public class CompositeFood extends Food {
    // Constructor - calculate calories immediately during construction
    public CompositeFood(String id, List<String> keywords, Map<Food, Integer> components) {
        super(id, keywords, 0);
        // Calculate calories once during construction
        int totalCalories = 0;
        for (Map.Entry<Food, Integer> entry : components.entrySet()) {
            totalCalories += entry.getKey().calculateCalories() * entry.getValue();
        }
        this.calories = totalCalories;
    }

    // Simply return the pre-calculated calories
    @Override
    public int calculateCalories() {
        return this.calories;
    }

    // Display method - matches BasicFood format
    public void displayFoodInfo() {
        System.out.println("Food: " + id + ", Calories: " + calories + " per serving");
        System.out.println("Keywords: " + keywords);
    }
}
