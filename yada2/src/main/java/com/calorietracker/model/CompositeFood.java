package com.calorietracker.model;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents a composite food item that consists of multiple food items.
 * Each component food item can have a specified number of servings.
 */
public class CompositeFood extends Food {
    private Map<Food, Double> components; // Food and servings
    
    public CompositeFood(String name, List<String> keywords) {
        super(name, keywords);
        this.components = new HashMap<>();
    }
    
    public void addComponent(Food food, double servings) {
        components.put(food, servings);
    }
    
    public Map<Food, Double> getComponents() {
        return new HashMap<>(components);
    }
    
    @Override
    public double getCaloriesPerServing() {
        return components.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getCaloriesPerServing() * entry.getValue())
                .sum();
    }
}
