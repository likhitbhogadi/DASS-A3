package com.calorietracker.service;

import com.calorietracker.model.BasicFood;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example implementation to show how a web API food source could be implemented
 * This is a placeholder that would be replaced with actual API calls in a production version
 */
public class ExampleWebFoodSource implements FoodSource {
    
    @Override
    public String getSourceName() {
        return "Example Web API";
    }
    
    @Override
    public List<BasicFood> searchFoods(String query) {
        // In a real implementation, this would make an HTTP request to a web API
        // For this example, we'll just return some dummy data
        List<BasicFood> results = new ArrayList<>();
        
        if (query.toLowerCase().contains("apple")) {
            results.add(new BasicFood("Apple", Arrays.asList("fruit", "snack"), 52.0));
        }
        if (query.toLowerCase().contains("banana")) {
            results.add(new BasicFood("Banana", Arrays.asList("fruit", "snack"), 89.0));
        }
        if (query.toLowerCase().contains("chicken")) {
            results.add(new BasicFood("Grilled Chicken Breast", 
                    Arrays.asList("meat", "protein", "dinner"), 165.0));
        }
        
        return results;
    }
    
    @Override
    public BasicFood getFoodDetails(String foodId) {
        // In a real implementation, this would make an HTTP request to get detailed information
        // about a specific food item
        switch (foodId.toLowerCase()) {
            case "apple":
                return new BasicFood("Apple", Arrays.asList("fruit", "snack"), 52.0);
            case "banana":
                return new BasicFood("Banana", Arrays.asList("fruit", "snack"), 89.0);
            case "grilled chicken breast":
                return new BasicFood("Grilled Chicken Breast", 
                        Arrays.asList("meat", "protein", "dinner"), 165.0);
            default:
                return null;
        }
    }
}
