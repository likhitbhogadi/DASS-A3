package com.calorietracker.model;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
