package com.calorietracker;

import com.calorietracker.cli.CalorieTrackerCLI;

public class CalorieTrackerApp {
    public static void main(String[] args) {
        // Create and start the CLI application
        CalorieTrackerCLI cli = new CalorieTrackerCLI();
        cli.start();
    }
}
