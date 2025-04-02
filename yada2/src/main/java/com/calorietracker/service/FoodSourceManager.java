package com.calorietracker.service;

import com.calorietracker.model.BasicFood;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager class for handling multiple food data sources
 */
public class FoodSourceManager {
    private final Map<String, FoodSource> foodSources;
    
    public FoodSourceManager() {
        foodSources = new HashMap<>();
    }
    
    public void addFoodSource(FoodSource foodSource) {
        foodSources.put(foodSource.getSourceName(), foodSource);
    }
    
    public List<String> getAvailableSources() {
        return new ArrayList<>(foodSources.keySet());
    }
    
    public List<BasicFood> searchFoodsFromSource(String sourceName, String query) {
        FoodSource source = foodSources.get(sourceName);
        if (source != null) {
            return source.searchFoods(query);
        }
        return new ArrayList<>();
    }
    
    public List<BasicFood> searchFoodsFromAllSources(String query) {
        List<BasicFood> results = new ArrayList<>();
        for (FoodSource source : foodSources.values()) {
            results.addAll(source.searchFoods(query));
        }
        return results;
    }
    
    public BasicFood getFoodDetails(String sourceName, String foodId) {
        FoodSource source = foodSources.get(sourceName);
        if (source != null) {
            return source.getFoodDetails(foodId);
        }
        return null;
    }
}
