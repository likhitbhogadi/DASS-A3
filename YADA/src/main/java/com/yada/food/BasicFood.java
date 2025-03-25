package com.yada.food;

import java.util.List;

public class BasicFood extends Food {

    // Constructor
    public BasicFood(String id, List<String> keywords, int calories) {
        super(id, keywords, calories);
    }

    // BasicFood's calorie count is the value set in the constructor
    @Override
    public int calculateCalories() {
        return this.calories;
    }

    // Display method (for debugging)
    public void displayFoodInfo() {
        System.out.println("Food: " + id + ", Calories: " + calories + " per serving");
        System.out.println("Keywords: " + keywords);
    }
}
