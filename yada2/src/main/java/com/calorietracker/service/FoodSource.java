package com.calorietracker.service;

import com.calorietracker.model.BasicFood;
import java.util.List;

/**
 * Interface for different sources of food data.
 * This allows for extension to pull data from various web sources in the future.
 */
public interface FoodSource {
    /**
     * Returns the name of this food source
     */
    String getSourceName();
    
    /**
     * Searches for foods from this source using a query string
     */
    List<BasicFood> searchFoods(String query);
    
    /**
     * Gets details for a specific food
     */
    BasicFood getFoodDetails(String foodId);
}
