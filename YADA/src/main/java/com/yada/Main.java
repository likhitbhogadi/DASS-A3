package com.yada;

import com.yada.food.BasicFood;
import com.yada.food.CompositeFood;
import com.yada.food.Food;
import com.yada.db.FoodDatabase;
import com.yada.log.DietLogManager;
import com.yada.profile.UserProfile;
import com.yada.profile.CalorieCalculator;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DietLogManager logManager = new DietLogManager();
        FoodDatabase foodDatabase = new FoodDatabase(); // Fix: Initialize the food database
        Scanner scanner = new Scanner(System.in);

        // Load existing logs (if available)
        try {
            logManager.loadLogsFromFile("logs.txt");
            System.out.println("Logs loaded successfully!");
        } catch (IOException e) {
            System.out.println("No logs found. Starting fresh.");
        }

        // Sample User Profile (this can be made dynamic later)
        UserProfile user = new UserProfile("Male", 30, 175, 70, "Moderate");

        System.out.println("\nWelcome to YADA - Your Diet Assistant!\n");
        System.out.println("Available Commands:\n");
        System.out.println("1. add <food_name> <calories> <servings>");
        System.out.println("2. view <date>");
        System.out.println("3. undo");
        System.out.println("4. calorie-target");
        System.out.println("5. create-composite <composite_name>");
        System.out.println("6. log <date> <food_name> <servings>");
        System.out.println("7. delete <date> <food_name>");
        System.out.println("8. update-profile <gender> <age> <height> <weight> <activity>");
        System.out.println("9. set-calorie-method <harris|mifflin>");
        System.out.println("10. save");
        System.out.println("11. exit");

        while (true) {
            System.out.print("\nEnter a command: ");
            String command = scanner.nextLine();
            String[] parts = command.split(" ");

            switch (parts[0].toLowerCase()) {
                case "add":
                    if (parts.length == 4) {
                        String foodName = parts[1];
                        int calories = Integer.parseInt(parts[2]);
                        int servings = Integer.parseInt(parts[3]);
                        BasicFood food = new BasicFood(foodName, Arrays.asList("user-added"), calories);
                        logManager.addFoodToLog("2025-03-26", food, servings); // Static date for now
                        System.out.println("Added " + servings + " servings of " + foodName);
                    } else {
                        System.out.println("Usage: add <food_name> <calories> <servings>");
                    }
                    break;

                case "view":
                    if (parts.length == 2) {
                        String date = parts[1];
                        System.out.println("Logs for " + date + ":");
                        logManager.displayLogs();
                    } else {
                        System.out.println("Usage: view <date>");
                    }
                    break;

                case "undo":
                    logManager.undo();
                    System.out.println("Last action undone.");
                    break;

                case "calorie-target":
                    int harrisCalories = CalorieCalculator.calculateHarrisBenedict(user);
                    int mifflinCalories = CalorieCalculator.calculateMifflinStJeor(user);
                    System.out.println("Calorie Targets:");
                    System.out.println("- Harris-Benedict: " + harrisCalories);
                    System.out.println("- Mifflin-St Jeor: " + mifflinCalories);
                    break;

                case "save":
                    try {
                        logManager.saveLogsToFile("logs.txt");
                        System.out.println("Logs saved successfully!");
                    } catch (IOException e) {
                        System.out.println("Error saving logs: " + e.getMessage());
                    }
                    break;

                case "exit":
                    System.out.println("Exiting. Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;

                case "create-composite":
                    if (parts.length >= 3) {
                        String compositeName = parts[1];
                        Map<Food, Integer> components = new HashMap<>();
                        System.out
                                .println("Enter components in the format <food_name>:<servings>, separated by spaces:");
                        String[] componentInputs = scanner.nextLine().split(" ");
                        for (String componentInput : componentInputs) {
                            String[] componentParts = componentInput.split(":");
                            String foodName = componentParts[0];
                            int servings = Integer.parseInt(componentParts[1]);

                            // Find the food by name in the database
                            Food component = foodDatabase.searchByKeyword(foodName).stream()
                                    .filter(food -> food.getId().equalsIgnoreCase(foodName))
                                    .findFirst()
                                    .orElse(null);

                            if (component != null) {
                                components.put(component, servings);
                            } else {
                                System.out.println("Food " + foodName + " not found. Skipping.");
                            }
                        }

                        // Create and add composite food
                        CompositeFood compositeFood = new CompositeFood(compositeName, Arrays.asList("composite"),
                                components);
                        foodDatabase.addCompositeFood(compositeFood);
                        System.out.println("Composite food " + compositeName + " created successfully.");
                    } else {
                        System.out.println("Usage: create-composite <composite_name>");
                    }
                    break;

                case "log":
                    if (parts.length == 4) {
                        String date = parts[1];
                        String foodName = parts[2];
                        int servings = Integer.parseInt(parts[3]);

                        // Find food by name
                        Food food = foodDatabase.searchByKeyword(foodName).stream()
                                .filter(f -> f.getId().equalsIgnoreCase(foodName))
                                .findFirst()
                                .orElse(null);

                        if (food != null) {
                            logManager.addFoodToLog(date, food, servings);
                            System.out.println("Added " + servings + " servings of " + foodName + " to " + date + ".");
                        } else {
                            System.out.println("Food " + foodName + " not found.");
                        }
                    } else {
                        System.out.println("Usage: log <date> <food_name> <servings>");
                    }
                    break;

                case "delete":
                    if (parts.length == 3) {
                        String date = parts[1];
                        String foodName = parts[2];

                        // Find food by name
                        Food food = foodDatabase.searchByKeyword(foodName).stream()
                                .filter(f -> f.getId().equalsIgnoreCase(foodName))
                                .findFirst()
                                .orElse(null);

                        if (food != null) {
                            logManager.removeFoodFromLog(date, food);
                            System.out.println("Removed " + foodName + " from " + date + ".");
                        } else {
                            System.out.println("Food " + foodName + " not found in logs.");
                        }
                    } else {
                        System.out.println("Usage: delete <date> <food_name>");
                    }
                    break;

                case "update-profile":
                    System.out.println("Enter updated values for gender, age, height, weight, and activity level:");
                    String[] profileInputs = scanner.nextLine().split(" ");
                    user = new UserProfile(profileInputs[0], Integer.parseInt(profileInputs[1]),
                            Integer.parseInt(profileInputs[2]), Integer.parseInt(profileInputs[3]), profileInputs[4]);
                    System.out.println("Profile updated successfully.");
                    break;

                case "set-calorie-method":
                    if (parts.length == 2) {
                        String method = parts[1];
                        if (method.equalsIgnoreCase("harris")) {
                            System.out.println("Using Harris-Benedict method.");
                        } else if (method.equalsIgnoreCase("mifflin")) {
                            System.out.println("Using Mifflin-St Jeor method.");
                        } else {
                            System.out.println("Unknown method. Options: harris, mifflin.");
                        }
                    } else {
                        System.out.println("Usage: set-calorie-method <harris|mifflin>");
                    }
                    break;

                default:
                    System.out.println("Unknown command. Please try again.");
            }
        }
    }
}
