package com.yada.food;

import java.util.List;

public abstract class Food {
    protected String id;  // Identifier for the food
    protected List<String> keywords;  // Search keywords for food
    protected int calories;  // Calories per serving

    // Constructor
    public Food(String id, List<String> keywords, int calories) {
        this.id = id;
        this.keywords = keywords;
        this.calories = calories;
    }

    // Abstract method to calculate calories (to be implemented by subclasses)
    public abstract int calculateCalories();

    // Getters
    public String getId() {
        return id;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public int getCalories() {
        return calories;
    }

    // Utility method to check if a keyword matches
    public boolean matchesKeyword(String keyword) {
        return keywords.contains(keyword);
    }
}
