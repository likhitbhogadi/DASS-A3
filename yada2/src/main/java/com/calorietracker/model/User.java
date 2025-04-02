package com.calorietracker.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String gender;
    private double height; // in cm
    private int age;
    private Map<LocalDate, Double> weightByDate;
    private Map<LocalDate, ActivityLevel> activityLevelByDate;
    
    public enum ActivityLevel {
        SEDENTARY(1.2),
        LIGHTLY_ACTIVE(1.375),
        MODERATELY_ACTIVE(1.55),
        VERY_ACTIVE(1.725),
        EXTRA_ACTIVE(1.9);
        
        private final double factor;
        
        ActivityLevel(double factor) {
            this.factor = factor;
        }
        
        public double getFactor() {
            return factor;
        }
    }
    
    public User(String gender, double height, int age, double initialWeight, ActivityLevel initialActivityLevel) {
        this.gender = gender;
        this.height = height;
        this.age = age;
        this.weightByDate = new HashMap<>();
        this.activityLevelByDate = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        weightByDate.put(today, initialWeight);
        activityLevelByDate.put(today, initialActivityLevel);
    }
    
    public String getGender() {
        return gender;
    }
    
    public double getHeight() {
        return height;
    }
    
    public int getAge() {
        return age;
    }
    
    public double getWeight(LocalDate date) {
        // Get weight for specified date, or the most recent weight before that date
        return weightByDate.entrySet().stream()
                .filter(entry -> !entry.getKey().isAfter(date))
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .orElse(0.0);
    }
    
    public ActivityLevel getActivityLevel(LocalDate date) {
        // Get activity level for specified date, or the most recent level before that date
        return activityLevelByDate.entrySet().stream()
                .filter(entry -> !entry.getKey().isAfter(date))
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .orElse(ActivityLevel.SEDENTARY);
    }
    
    public void setWeight(LocalDate date, double weight) {
        weightByDate.put(date, weight);
    }
    
    public void setActivityLevel(LocalDate date, ActivityLevel level) {
        activityLevelByDate.put(date, level);
    }
}
