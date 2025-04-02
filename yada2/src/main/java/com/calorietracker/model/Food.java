package com.calorietracker.model;

import java.util.List;
import java.util.ArrayList;

public abstract class Food {
    private String name;
    private List<String> keywords;
    
    public Food(String name, List<String> keywords) {
        this.name = name;
        this.keywords = new ArrayList<>(keywords);
    }
    
    public String getName() {
        return name;
    }
    
    public List<String> getKeywords() {
        return new ArrayList<>(keywords);
    }
    
    public void addKeyword(String keyword) {
        if (!keywords.contains(keyword)) {
            keywords.add(keyword);
        }
    }
    
    public boolean matchesKeyword(String keyword) {
        return name.toLowerCase().contains(keyword.toLowerCase()) || 
               keywords.stream().anyMatch(k -> k.toLowerCase().contains(keyword.toLowerCase()));
    }
    
    public abstract double getCaloriesPerServing();
}
