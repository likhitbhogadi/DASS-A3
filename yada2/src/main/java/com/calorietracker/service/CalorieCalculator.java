package com.calorietracker.service;

import com.calorietracker.model.User;
import java.time.LocalDate;

public interface CalorieCalculator {
    String getName();
    
    double calculateTargetCalories(User user, LocalDate date);
}
