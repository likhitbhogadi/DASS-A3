package com.calorietracker.cli;

import com.calorietracker.controller.CalorieTrackerController;
import com.calorietracker.model.CompositeFood;
import com.calorietracker.model.DailyLog;
import com.calorietracker.model.Food;
import com.calorietracker.model.FoodEntry;
import com.calorietracker.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CalorieTrackerCLI {
    private final Scanner scanner;
    private final CalorieTrackerController controller;
    private boolean running = true;
    
    public CalorieTrackerCLI() {
        scanner = new Scanner(System.in);
        String userDir = System.getProperty("user.dir");
        String basicFoodsPath = userDir + "/data/basic_foods.json";
        String compositeFoodsPath = userDir + "/data/composite_foods.json";
        String logPath = userDir + "/data/log.json";
        String profilePath = userDir + "/data/user_profile.json";
        
        controller = new CalorieTrackerController(basicFoodsPath, compositeFoodsPath, logPath, profilePath);
    }
    
    public void start() {
        System.out.println("===== Calorie Tracker CLI Application =====");
        
        // Check if we need to setup a user
        if (controller.getUser() == null) {
            setupUser();
        }
        
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ", 0, 12);
            processMainMenuChoice(choice);
        }
    }
    
    private void setupUser() {
        System.out.println("\n==== User Setup ====");
        
        System.out.print("Enter your gender (male/female): ");
        String gender = scanner.nextLine().trim().toLowerCase();
        while (!gender.equals("male") && !gender.equals("female")) {
            System.out.print("Invalid input. Enter gender (male/female): ");
            gender = scanner.nextLine().trim().toLowerCase();
        }
        
        double height = getDoubleInput("Enter your height (cm): ", 50, 250);
        int age = getIntInput("Enter your age: ", 1, 120);
        double weight = getDoubleInput("Enter your weight (kg): ", 20, 500);
        
        System.out.println("\nActivity level:");
        System.out.println("1. Sedentary (little or no exercise)");
        System.out.println("2. Lightly active (light exercise 1-3 days/week)");
        System.out.println("3. Moderately active (moderate exercise 3-5 days/week)");
        System.out.println("4. Very active (hard exercise 6-7 days/week)");
        System.out.println("5. Extra active (very hard exercise & physical job)");
        int activityChoice = getIntInput("Select your activity level: ", 1, 5);
        
        User.ActivityLevel activityLevel = User.ActivityLevel.SEDENTARY;
        switch (activityChoice) {
            case 1:
                activityLevel = User.ActivityLevel.SEDENTARY;
                break;
            case 2:
                activityLevel = User.ActivityLevel.LIGHTLY_ACTIVE;
                break;
            case 3:
                activityLevel = User.ActivityLevel.MODERATELY_ACTIVE;
                break;
            case 4:
                activityLevel = User.ActivityLevel.VERY_ACTIVE;
                break;
            case 5:
                activityLevel = User.ActivityLevel.EXTRA_ACTIVE;
                break;
        }
        
        controller.createUser(gender, height, age, weight, activityLevel);
        System.out.println("User profile created successfully!");
    }
    
    private void displayMainMenu() {
        LocalDate currentDate = controller.getCurrentDate();
        
        System.out.println("\n===== Main Menu =====");
        // System.out.println("Current Date: " + currentDate.format(DateTimeFormatter.ISO_DATE));
        // System.out.println("Current Calculator: " + controller.getCurrentCalculatorName());
        // System.out.println("Target Calories: " + controller.getTargetCalories());
        // System.out.println("Consumed Calories: " + controller.getCurrentDayCalories());
        // System.out.println("Difference: " + controller.getCalorieDifference());
        
        System.out.println("\n1. View Food Log");
        System.out.println("2. Update Food Log");
        System.out.println("3. Add New Basic Food");
        System.out.println("4. Create Composite Food");
        System.out.println("5. Search Foods/Add Food to Today's Log");
        System.out.println("6. Change Date");
        System.out.println("7. Update User Info");
        System.out.println("8. Change Calorie Calculator");
        System.out.println("9. Undo Last Action");
        System.out.println("10. Save Data");
        System.out.println("11. View Command History");
        System.out.println("12. View Nutritional Summary for Date");
        System.out.println("0. Exit");
    }
    
    private void processMainMenuChoice(int choice) {
        switch (choice) {
            case 0:
                exitApplication();
                break;
            case 1:
                viewFoodLog();
                break;
            case 2:
                updateFoodLog();
                break;
            case 3:
                addBasicFood();
                break;
            case 4:
                createCompositeFood();
                break;
            case 5:
                searchFoods();
                break;
            case 6:
                changeDate();
                break;
            case 7:
                updateUserInfo();
                break;
            case 8:
                changeCalorieCalculator();
                break;
            case 9:
                undoLastAction();
                break;
            case 10:
                saveData();
                break;
            case 11:
                viewCommandHistory();
                break;
            case 12:
                viewNutritionalSummary();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private void addFoodToLog() {
        System.out.println("\n==== Add Food to Log ====");
        
        // Search for food
        System.out.print("Enter search term for food: ");
        String searchTerm = scanner.nextLine().trim();
        List<Food> foods = controller.searchFoods(searchTerm);
        
        if (foods.isEmpty()) {
            System.out.println("No matching foods found.");
            return;
        }
        
        // Display search results
        System.out.println("\nSearch results:");
        for (int i = 0; i < foods.size(); i++) {
            Food food = foods.get(i);
            System.out.printf("%d. %s (%.2f calories per serving)\n", 
                             i + 1, food.getName(), food.getCaloriesPerServing());
        }
        
        // Select food
        int foodIndex = getIntInput("Select a food (0 to cancel): ", 0, foods.size()) - 1;
        if (foodIndex == -1) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        Food selectedFood = foods.get(foodIndex);
        
        // Enter number of servings
        double servings = getDoubleInput("Enter number of servings: ", 0.1, 100);
        
        // Add to log
        controller.addFoodEntry(selectedFood, servings);
        System.out.printf("Added %.1f serving(s) of %s to the log.\n", 
                         servings, selectedFood.getName());
    }
    
    private void viewFoodLog() {
        System.out.println("\n==== View Food Log ====");
        
        LocalDate logDate = promptForDate("Enter date to view (YYYY-MM-DD) or press Enter for current date: ");
        if (logDate == null) {
            return;
        }
        
        DailyLog log = controller.getDailyLog(logDate);
        List<FoodEntry> entries = log.getEntries();
        
        System.out.println("\n==== Food Log for " + logDate.format(DateTimeFormatter.ISO_DATE) + " ====");
        
        if (entries.isEmpty()) {
            System.out.println("No entries for this day.");
            return;
        }
        
        double totalCalories = 0;
        
        System.out.println("Food entries:");
        for (int i = 0; i < entries.size(); i++) {
            FoodEntry entry = entries.get(i);
            double entryCalories = entry.getTotalCalories();
            totalCalories += entryCalories;
            
            System.out.printf("%d. %s - %.1f serving(s) - %.2f calories\n", 
                             i + 1, entry.getFood().getName(), entry.getServings(), entryCalories);
        }
        
        // Set controller date to selected date to ensure target calculations use correct data
        LocalDate currentDate = controller.getCurrentDate(); // save current date
        controller.setCurrentDate(logDate);
        
        double targetCalories = controller.getTargetCalories();
        System.out.println("\nTotal calories consumed: " + totalCalories);
        System.out.println("Target calories: " + targetCalories);
        System.out.println("Difference: " + (targetCalories - totalCalories));
        
        controller.setCurrentDate(currentDate); // restore current date
    }
    
    private void updateFoodLog() {
        System.out.println("\n==== Update Food Log ====");
        
        LocalDate logDate = promptForDate("Enter date to update (YYYY-MM-DD) or press Enter for current date: ");
        if (logDate == null) {
            return;
        }
        
        // Store current date in case we need to restore it
        LocalDate currentDate = controller.getCurrentDate();
        
        // Temporarily set controller date to the selected log date
        controller.setCurrentDate(logDate);
        DailyLog log = controller.getCurrentDayLog();
        List<FoodEntry> entries = log.getEntries();
        
        System.out.println("\n==== Food Log for " + logDate.format(DateTimeFormatter.ISO_DATE) + " ====");
        
        if (entries.isEmpty()) {
            System.out.println("No entries for this day.");
            System.out.println("\nTo add food entries, please use 'Search Foods/Add Food to Log' from the main menu.");
            
            // Restore original date and return
            controller.setCurrentDate(currentDate);
            return;
        }
        
        // Display all entries
        System.out.println("Current entries:");
        for (int i = 0; i < entries.size(); i++) {
            FoodEntry entry = entries.get(i);
            System.out.printf("%d. %s - %.1f serving(s) - %.2f calories\n", 
                             i + 1, entry.getFood().getName(), entry.getServings(), 
                             entry.getTotalCalories());
        }
        
        System.out.println("\nOptions:");
        System.out.println("1. Remove a food entry");
        System.out.println("2. Modify a food entry");
        System.out.println("0. Return to main menu");
        
        int choice = getIntInput("Enter your choice: ", 0, 2);
        
        switch (choice) {
            case 0:
                break;
            case 1:
                // Remove food entry
                int entryIndex = getIntInput("Select entry to remove (0 to cancel): ", 0, entries.size()) - 1;
                if (entryIndex >= 0) {
                    controller.removeFoodEntry(entryIndex);
                    System.out.println("Food entry removed successfully.");
                }
                break;
            case 2:
                // Modify food entry (remove and add new)
                entryIndex = getIntInput("Select entry to modify (0 to cancel): ", 0, entries.size()) - 1;
                if (entryIndex >= 0) {
                    FoodEntry oldEntry = entries.get(entryIndex);
                    Food selectedFood = oldEntry.getFood();
                    
                    // Get new serving amount
                    double newServings = getDoubleInput(
                        String.format("Enter new servings for %s (current: %.1f): ", 
                                     selectedFood.getName(), oldEntry.getServings()), 
                        0.1, 100);
                    
                    // Remove old entry and add new one
                    controller.removeFoodEntry(entryIndex);
                    controller.addFoodEntry(selectedFood, newServings);
                    System.out.println("Food entry updated successfully.");
                }
                break;
        }
        
        // Restore original date
        controller.setCurrentDate(currentDate);
    }
    
    private LocalDate promptForDate(String prompt) {
        System.out.print(prompt);
        String dateInput = scanner.nextLine().trim();
        
        if (dateInput.isEmpty()) {
            return controller.getCurrentDate();
        }
        
        try {
            return LocalDate.parse(dateInput);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return null;
        }
    }
    
    private void addBasicFood() {
        System.out.println("\n==== Add New Basic Food ====");
        
        System.out.print("Enter food name: ");
        String name = scanner.nextLine().trim();
        
        // Check if food with this name already exists
        if (controller.getFoodByName(name).isPresent()) {
            System.out.println("A food with this name already exists.");
            return;
        }
        
        System.out.print("Enter keywords (comma-separated): ");
        String keywordsInput = scanner.nextLine().trim();
        List<String> keywords = new ArrayList<>();
        if (!keywordsInput.isEmpty()) {
            String[] parts = keywordsInput.split(",");
            for (String part : parts) {
                String keyword = part.trim();
                if (!keyword.isEmpty()) {
                    keywords.add(keyword);
                }
            }
        }
        
        double calories = getDoubleInput("Enter calories per serving: ", 0, 10000);
        
        controller.addBasicFood(name, keywords, calories);
        System.out.println("Basic food added successfully.");
    }
    
    private void createCompositeFood() {
        System.out.println("\n==== Create Composite Food ====");
        
        System.out.print("Enter composite food name: ");
        String name = scanner.nextLine().trim();
        
        // Check if food with this name already exists
        if (controller.getFoodByName(name).isPresent()) {
            System.out.println("A food with this name already exists.");
            return;
        }
        
        System.out.print("Enter keywords (comma-separated): ");
        String keywordsInput = scanner.nextLine().trim();
        List<String> keywords = new ArrayList<>();
        if (!keywordsInput.isEmpty()) {
            String[] parts = keywordsInput.split(",");
            for (String part : parts) {
                String keyword = part.trim();
                if (!keyword.isEmpty()) {
                    keywords.add(keyword);
                }
            }
        }
        
        // Add components
        Map<Food, Double> components = new HashMap<>();
        boolean addingComponents = true;
        
        while (addingComponents) {
            // Search for food
            System.out.print("Enter search term for component food (empty to finish): ");
            String searchTerm = scanner.nextLine().trim();
            
            if (searchTerm.isEmpty()) {
                addingComponents = false;
                continue;
            }
            
            List<Food> foods = controller.searchFoods(searchTerm);
            
            if (foods.isEmpty()) {
                System.out.println("No matching foods found.");
                continue;
            }
            
            // Display search results
            System.out.println("\nSearch results:");
            for (int i = 0; i < foods.size(); i++) {
                Food food = foods.get(i);
                System.out.printf("%d. %s (%.2f calories per serving)\n", 
                                 i + 1, food.getName(), food.getCaloriesPerServing());
            }
            
            // Select food
            int foodIndex = getIntInput("Select a food (0 to cancel): ", 0, foods.size()) - 1;
            if (foodIndex == -1) {
                System.out.println("Component selection cancelled.");
                continue;
            }
            
            Food selectedFood = foods.get(foodIndex);
            
            // Enter number of servings
            double servings = getDoubleInput("Enter number of servings: ", 0.1, 100);
            
            // Add to components
            components.put(selectedFood, servings);
            System.out.printf("Added %.1f serving(s) of %s to the composite food.\n", 
                             servings, selectedFood.getName());
        }
        
        if (components.isEmpty()) {
            System.out.println("No components added. Composite food creation cancelled.");
            return;
        }
        
        controller.addCompositeFood(name, keywords, components);
        System.out.println("Composite food created successfully.");
    }
    
    private void searchFoods() {
        System.out.println("\n==== Search Foods ====");
        System.out.println("1. View all foods");
        System.out.println("2. Search by name/keyword");
        System.out.println("0. Return to main menu");
        
        int choice = getIntInput("Enter your choice: ", 0, 2);
        
        List<Food> foods = new ArrayList<>();
        
        switch (choice) {
            case 0:
                return;
            case 1:
                // View all foods
                foods = controller.getAllFoods();
                // Sort foods alphabetically by name
                foods.sort(Comparator.comparing(Food::getName));
                if (foods.isEmpty()) {
                    System.out.println("No foods found in the database.");
                    return;
                }
                
                // Separate foods into basic and composite
                List<Food> basicFoods = new ArrayList<>();
                List<Food> compositeFoods = new ArrayList<>();
                
                for (Food food : foods) {
                    if (food.getClass().getSimpleName().equals("BasicFood")) {
                        basicFoods.add(food);
                    } else {
                        compositeFoods.add(food);
                    }
                }
                
                System.out.println("\n-- Basic Foods --");
                for (int i = 0; i < basicFoods.size(); i++) {
                    System.out.printf("%d. %s\n", i + 1, basicFoods.get(i).getName());
                }
                
                System.out.println("\n-- Composite Foods --");
                for (int i = 0; i < compositeFoods.size(); i++) {
                    System.out.printf("%d. %s\n", basicFoods.size() + i + 1, compositeFoods.get(i).getName());
                }
                
                // Merge lists back for selection
                foods = new ArrayList<>(basicFoods);
                foods.addAll(compositeFoods);
                break;
            case 2:
                // Search by name/keyword
                System.out.print("Enter search term: ");
                String searchTerm = scanner.nextLine().trim();
                foods = controller.searchFoods(searchTerm);
                if (foods.isEmpty()) {
                    System.out.println("No matching foods found.");
                    return;
                }
                
                // Separate search results into basic and composite
                basicFoods = new ArrayList<>();
                compositeFoods = new ArrayList<>();
                
                for (Food food : foods) {
                    if (food.getClass().getSimpleName().equals("BasicFood")) {
                        basicFoods.add(food);
                    } else {
                        compositeFoods.add(food);
                    }
                }
                
                System.out.println("\n-- Basic Foods Matching \"" + searchTerm + "\" --");
                if (basicFoods.isEmpty()) {
                    System.out.println("No basic foods found matching your search.");
                } else {
                    for (int i = 0; i < basicFoods.size(); i++) {
                        System.out.printf("%d. %s\n", i + 1, basicFoods.get(i).getName());
                    }
                }
                
                System.out.println("\n-- Composite Foods Matching \"" + searchTerm + "\" --");
                if (compositeFoods.isEmpty()) {
                    System.out.println("No composite foods found matching your search.");
                } else {
                    for (int i = 0; i < compositeFoods.size(); i++) {
                        System.out.printf("%d. %s\n", basicFoods.size() + i + 1, compositeFoods.get(i).getName());
                    }
                }
                
                // Merge lists back for selection
                foods = new ArrayList<>(basicFoods);
                foods.addAll(compositeFoods);
                break;
        }
        
        // Return to this point after viewing food details or adding to log
        boolean stayInFoodMenu = true;
        while (stayInFoodMenu && !foods.isEmpty()) {
            System.out.println("\nOptions:");
            System.out.println("1. View details of a specific food");
            System.out.println("2. Add a food to today's log");
            System.out.println("0. Return to main menu");
            
            choice = getIntInput("Enter your choice: ", 0, 2);
            
            switch (choice) {
                case 0:
                    stayInFoodMenu = false;
                    break;
                case 1:
                    // View details of a specific food
                    int foodIndex = getIntInput("Enter food number to view details (0 to cancel): ", 0, foods.size()) - 1;
                    if (foodIndex == -1) {
                        continue;  // Go back to options instead of returning to main menu
                    }
                    
                    Food selectedFood = foods.get(foodIndex);
                    displayFoodDetails(selectedFood);
                    // No return statement here, so it goes back to the options menu
                    break;
                case 2:
                    // Add food to today's log
                    foodIndex = getIntInput("Enter food number to add to log (0 to cancel): ", 0, foods.size()) - 1;
                    if (foodIndex == -1) {
                        continue;  // Go back to options instead of returning to main menu
                    }
                    
                    selectedFood = foods.get(foodIndex);
                    double servings = getDoubleInput("Enter number of servings: ", 0.1, 100);
                    
                    controller.addFoodEntry(selectedFood, servings);
                    System.out.printf("Added %.1f serving(s) of %s to today's log.\n", 
                                   servings, selectedFood.getName());
                    
                    System.out.println("\nDo you want to add more foods to the log?");
                    System.out.println("1. Yes");
                    System.out.println("2. No, return to main menu");
                    int moreFood = getIntInput("Enter your choice: ", 1, 2);
                    if (moreFood == 2) {
                        stayInFoodMenu = false;
                    }
                    break;
            }
        }
    }
    
    private void displayFoodDetails(Food food) {
        System.out.println("\n==== Food Details ====");
        System.out.println("Name: " + food.getName());
        System.out.println("Type: " + food.getClass().getSimpleName());
        System.out.println("Calories per serving: " + food.getCaloriesPerServing());
        System.out.println("Keywords: " + String.join(", ", food.getKeywords()));
        
        if (food.getClass().getSimpleName().equals("CompositeFood")) {
            System.out.println("\nComponents:");
            Map<Food, Double> components = ((CompositeFood) food).getComponents();
            int i = 1;
            for (Map.Entry<Food, Double> entry : components.entrySet()) {
                System.out.printf("  %d. %s - %.1f serving(s) - %.2f calories\n", 
                                i++, entry.getKey().getName(), entry.getValue(),
                                entry.getKey().getCaloriesPerServing() * entry.getValue());
            }
        }
        
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void changeDate() {
        System.out.println("\n==== Change Date ====");
        System.out.println("Current date: " + controller.getCurrentDate());
        
        System.out.print("Enter new date (YYYY-MM-DD) or 'today': ");
        String dateInput = scanner.nextLine().trim();
        
        LocalDate newDate;
        if (dateInput.equalsIgnoreCase("today")) {
            newDate = LocalDate.now();
        } else {
            try {
                newDate = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                return;
            }
        }
        
        controller.setCurrentDate(newDate);
        System.out.println("Date changed to: " + newDate);
    }
    
    private void updateUserInfo() {
        System.out.println("\n==== Update User Info ====");
        
        System.out.println("1. Update gender");
        System.out.println("2. Update height");
        System.out.println("3. Update age");
        System.out.println("4. Update weight");
        System.out.println("5. Update activity level");
        System.out.println("0. Back to main menu");
        
        int choice = getIntInput("Enter your choice: ", 0, 5);
        
        switch (choice) {
            case 0:
                return;
            case 1:
                System.out.print("Enter gender (male/female): ");
                String gender = scanner.nextLine().trim().toLowerCase();
                while (!gender.equals("male") && !gender.equals("female")) {
                    System.out.print("Invalid input. Enter gender (male/female): ");
                    gender = scanner.nextLine().trim().toLowerCase();
                }
                controller.updateUserGender(gender);
                System.out.println("Gender updated successfully.");
                break;
            case 2:
                double height = getDoubleInput("Enter new height (cm): ", 50, 250);
                controller.updateUserHeight(height);
                System.out.println("Height updated successfully.");
                break;
            case 3:
                int age = getIntInput("Enter new age: ", 1, 120);
                controller.updateUserAge(age);
                System.out.println("Age updated successfully.");
                break;
            case 4:
                double weight = getDoubleInput("Enter new weight (kg): ", 20, 500);
                controller.updateUserWeight(weight);
                System.out.println("Weight updated successfully.");
                break;
            case 5:
                System.out.println("\nActivity level:");
                System.out.println("1. Sedentary (little or no exercise)");
                System.out.println("2. Lightly active (light exercise 1-3 days/week)");
                System.out.println("3. Moderately active (moderate exercise 3-5 days/week)");
                System.out.println("4. Very active (hard exercise 6-7 days/week)");
                System.out.println("5. Extra active (very hard exercise & physical job)");
                
                int activityChoice = getIntInput("Select your activity level: ", 1, 5);
                
                User.ActivityLevel activityLevel = User.ActivityLevel.SEDENTARY;
                switch (activityChoice) {
                    case 1:
                        activityLevel = User.ActivityLevel.SEDENTARY;
                        break;
                    case 2:
                        activityLevel = User.ActivityLevel.LIGHTLY_ACTIVE;
                        break;
                    case 3:
                        activityLevel = User.ActivityLevel.MODERATELY_ACTIVE;
                        break;
                    case 4:
                        activityLevel = User.ActivityLevel.VERY_ACTIVE;
                        break;
                    case 5:
                        activityLevel = User.ActivityLevel.EXTRA_ACTIVE;
                        break;
                }
                
                controller.updateUserActivityLevel(activityLevel);
                System.out.println("Activity level updated successfully.");
                break;
        }
    }
    
    private void changeCalorieCalculator() {
        System.out.println("\n==== Change Calorie Calculator ====");
        System.out.println("Current calculator: " + controller.getCurrentCalculatorName());
        
        List<String> calculators = controller.getAvailableCalculators();
        for (int i = 0; i < calculators.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, calculators.get(i));
        }
        
        int calculatorIndex = getIntInput("Select calculator (0 to cancel): ", 0, calculators.size()) - 1;
        if (calculatorIndex == -1) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        controller.setCurrentCalculator(calculators.get(calculatorIndex));
        System.out.println("Calculator changed to: " + controller.getCurrentCalculatorName());
    }
    
    private void undoLastAction() {
        if (!controller.getCommandHistory().isEmpty()) {
            System.out.println("Undoing: " + controller.getCommandHistory().get(controller.getCommandHistory().size() - 1));
            controller.undo();
        } else {
            System.out.println("Nothing to undo.");
        }
    }
    
    private void saveData() {
        controller.saveAllData();
        System.out.println("All data saved successfully.");
    }
    
    private void viewCommandHistory() {
        System.out.println("\n==== Command History ====");
        List<String> history = controller.getCommandHistory();
        
        if (history.isEmpty()) {
            System.out.println("No commands in history.");
            return;
        }
        
        for (int i = history.size() - 1; i >= 0; i--) {
            System.out.printf("%d. %s\n", history.size() - i, history.get(i));
        }
    }
    
    private void viewNutritionalSummary() {
        System.out.println("\n==== View Nutritional Summary ====");
        
        LocalDate summaryDate = promptForDate("Enter date to view summary (YYYY-MM-DD) or press Enter for current date: ");
        if (summaryDate == null) {
            return;
        }
        
        // Store current date to restore it later
        LocalDate originalDate = controller.getCurrentDate();
        
        // Temporarily set date to calculate stats
        controller.setCurrentDate(summaryDate);
        
        // Get nutritional information for the requested date
        DailyLog log = controller.getCurrentDayLog();
        double consumedCalories = log.getTotalCalories();
        double targetCalories = controller.getTargetCalories();
        double difference = targetCalories - consumedCalories;
        String calculatorName = controller.getCurrentCalculatorName();
        
        // Display summary
        System.out.println("\n=== Nutritional Summary ===");
        System.out.println("Date: " + summaryDate.format(DateTimeFormatter.ISO_DATE));
        System.out.println("Calculator: " + calculatorName);
        System.out.println("Target Calories: " + targetCalories);
        System.out.println("Consumed Calories: " + consumedCalories);
        System.out.println("Difference: " + difference);
        
        // If there are entries in the log, show a quick breakdown
        List<FoodEntry> entries = log.getEntries();
        if (!entries.isEmpty()) {
            System.out.println("\nFood entries: " + entries.size());
            
            // Find highest calorie item
            FoodEntry highestCalorieEntry = entries.stream()
                .max(Comparator.comparing(FoodEntry::getTotalCalories))
                .orElse(null);
            
            if (highestCalorieEntry != null) {
                System.out.println("Highest calorie item: " + 
                    highestCalorieEntry.getFood().getName() + " (" + 
                    highestCalorieEntry.getTotalCalories() + " calories)");
            }
        }
        
        if (difference < 0) {
            System.out.println("\nYou exceeded your calorie target by " + Math.abs(difference) + " calories.");
        } else {
            System.out.println("\nYou have " + difference + " calories remaining for this day.");
        }
        
        // Restore original date
        controller.setCurrentDate(originalDate);
    }
    
    /**
     * Method to save all data, exposed for shutdown hook
     */
    public void saveAllDataBeforeExit() {
        if (controller != null) {
            controller.saveAllData();
        }
    }
    
    private void exitApplication() {
        System.out.println("Saving data before exit...");
        saveAllDataBeforeExit();
        System.out.println("Thank you for using Calorie Tracker!");
        running = false;
    }
    
    // Helper methods for input validation
    private int getIntInput(String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    break;
                }
                System.out.printf("Please enter a number between %d and %d.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return value;
    }
    
    private double getDoubleInput(String prompt, double min, double max) {
        double value;
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                value = Double.parseDouble(input);
                if (value >= min && value <= max) {
                    break;
                }
                System.out.printf("Please enter a number between %.1f and %.1f.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return value;
    }
}
