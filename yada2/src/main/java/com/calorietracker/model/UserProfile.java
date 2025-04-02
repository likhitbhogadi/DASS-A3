package com.calorietracker.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UserProfile {
    private String gender;
    private double height; // in cm
    private int age;
    private Map<String, Double> weightHistory;
    private Map<String, String> activityLevelHistory;
    
    public UserProfile() {
        this.weightHistory = new HashMap<>();
        this.activityLevelHistory = new HashMap<>();
    }
    
    public UserProfile(String gender, double height, int age, double initialWeight, User.ActivityLevel initialActivityLevel) {
        this.gender = gender;
        this.height = height;
        this.age = age;
        this.weightHistory = new HashMap<>();
        this.activityLevelHistory = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        weightHistory.put(today.toString(), initialWeight);
        activityLevelHistory.put(today.toString(), initialActivityLevel.name());
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public double getHeight() {
        return height;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public Map<String, Double> getWeightHistory() {
        return weightHistory;
    }
    
    public void setWeightHistory(Map<String, Double> weightHistory) {
        this.weightHistory = weightHistory;
    }
    
    public Map<String, String> getActivityLevelHistory() {
        return activityLevelHistory;
    }
    
    public void setActivityLevelHistory(Map<String, String> activityLevelHistory) {
        this.activityLevelHistory = activityLevelHistory;
    }
    
    // Helper methods to convert to/from User object
    public User toUser() {
        User user = new User(gender, height, age, 0, User.ActivityLevel.SEDENTARY);
        
        // Add weight history
        for (Map.Entry<String, Double> entry : weightHistory.entrySet()) {
            LocalDate date = LocalDate.parse(entry.getKey());
            user.setWeight(date, entry.getValue());
        }
        
        // Add activity level history
        for (Map.Entry<String, String> entry : activityLevelHistory.entrySet()) {
            LocalDate date = LocalDate.parse(entry.getKey());
            User.ActivityLevel level = User.ActivityLevel.valueOf(entry.getValue());
            user.setActivityLevel(date, level);
        }
        
        return user;
    }
    
    public static UserProfile fromUser(User user) {
        UserProfile profile = new UserProfile();
        profile.setGender(user.getGender());
        profile.setHeight(user.getHeight());
        profile.setAge(user.getAge());
        
        // We don't have direct access to the history maps in User class
        // This is a limitation of the current design
        
        return profile;
    }
}
