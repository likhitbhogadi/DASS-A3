package com.calorietracker.service;

import com.calorietracker.db.FoodDatabase;
import com.calorietracker.model.BasicFood;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation for local food database
 */
public class LocalFoodSource implements FoodSource {
    private final FoodDatabase foodDatabase;
    
    public LocalFoodSource(FoodDatabase foodDatabase) {
        this.foodDatabase = foodDatabase;
    }
    
    @Override
    public String getSourceName() {
        return "Local Database";
    }
    
    @Override
    public List<BasicFood> searchFoods(String query) {
        return foodDatabase.searchFoods(query).stream()
                .filter(food -> food instanceof BasicFood)
                .map(food -> (BasicFood) food)
                .collect(Collectors.toList());
    }
    
    @Override
    public BasicFood getFoodDetails(String foodId) {
        Optional<BasicFood> food = foodDatabase.getAllBasicFoods().stream()
                .filter(f -> f.getName().equals(foodId))
                .findFirst();
        return food.orElse(null);
    }
}
