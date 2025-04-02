package com.calorietracker.service;

import com.calorietracker.model.User;
import java.time.LocalDate;

public class HarrisBenedictCalculator implements CalorieCalculator {
    @Override
    public String getName() {
        return "Harris-Benedict Equation";
    }
    
    @Override
    public double calculateTargetCalories(User user, LocalDate date) {
        double weight = user.getWeight(date); // kg
        double height = user.getHeight(); // cm
        int age = user.getAge();
        double activityFactor = user.getActivityLevel(date).getFactor();
        
        double bmr;
        if ("male".equalsIgnoreCase(user.getGender())) {
            bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }
        
        return bmr * activityFactor;
    }
}
