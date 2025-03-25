package com.yada.food;

import java.util.List;
import java.util.Map;

public class CompositeFood extends Food {
    private Map<Food, Integer> components;  // Food and number of servings

    // Constructor
    public CompositeFood(String id, List<String> keywords, Map<Food, Integer> components) {
        super(id, keywords, 0);  // Calories will be calculated
        this.components = components;
    }

    // Calculate total calories by summing up the components
    @Override
    public int calculateCalories() {
        int totalCalories = 0;
        for (Map.Entry<Food, Integer> entry : components.entrySet()) {
            totalCalories += entry.getKey().calculateCalories() * entry.getValue();
        }
        this.calories = totalCalories;  // Update total calories
        return totalCalories;
    }

    // Display method
    public void displayFoodInfo() {
        System.out.println("Composite Food: " + id + ", Total Calories: " + calculateCalories());
        System.out.println("Contains:");
        for (Map.Entry<Food, Integer> entry : components.entrySet()) {
            System.out.println("- " + entry.getKey().getId() + ": " + entry.getValue() + " servings");
        }
    }
}
