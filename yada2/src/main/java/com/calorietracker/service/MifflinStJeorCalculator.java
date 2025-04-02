package com.calorietracker.service;

import com.calorietracker.model.User;
import java.time.LocalDate;

public class MifflinStJeorCalculator implements CalorieCalculator {
    @Override
    public String getName() {
        return "Mifflin-St Jeor Equation";
    }
    
    @Override
    public double calculateTargetCalories(User user, LocalDate date) {
        double weight = user.getWeight(date); // kg
        double height = user.getHeight(); // cm
        int age = user.getAge();
        double activityFactor = user.getActivityLevel(date).getFactor();
        
        double bmr;
        if ("male".equalsIgnoreCase(user.getGender())) {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }
        
        return bmr * activityFactor;
    }
}
