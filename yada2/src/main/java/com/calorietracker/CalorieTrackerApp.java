package com.calorietracker;

import com.calorietracker.cli.CalorieTrackerCLI;

import java.io.File;

public class CalorieTrackerApp {
    public static void main(String[] args) {
        // Ensure data directory exists
        String userDir = System.getProperty("user.dir");
        File dataDir = new File(userDir + "/data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        // Create and start the CLI application
        CalorieTrackerCLI cli = new CalorieTrackerCLI();
        
        // Register shutdown hook to save data before unexpected termination
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nApplication is shutting down. Saving data...");
            cli.saveAllDataBeforeExit();
            System.out.println("All data saved successfully.");
        }));
        
        cli.start();
    }
}
