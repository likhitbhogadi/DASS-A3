package com.yada.profile;

public class UserProfile {
    private String gender;  // "Male" or "Female"
    private int age;        // Age in years
    private int height;     // Height in centimeters
    private int weight;     // Weight in kilograms
    private String activityLevel;  // "Sedentary", "Moderate", "Active"

    // Constructor
    public UserProfile(String gender, int age, int height, int weight, String activityLevel) {
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.activityLevel = activityLevel;
    }

    // Getters and Setters
    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }
}
