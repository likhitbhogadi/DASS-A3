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
        System.out.println(Colors.CYAN_BOLD + "===== Calorie Tracker CLI Application =====" + Colors.RESET);

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
        System.out.println("\n" + Colors.CYAN_BOLD + "==== User Setup ====" + Colors.RESET);

        System.out.print("Enter your gender (male/female): ");
        String gender = scanner.nextLine().trim().toLowerCase();
        while (!gender.equals("male") && !gender.equals("female")) {
            System.out.print(Colors.RED_BOLD + "Invalid input. Enter gender (male/female): " + Colors.RESET);
            gender = scanner.nextLine().trim().toLowerCase();
        }

        double height = getDoubleInput("Enter your height (cm): ", 50, 250);
        int age = getIntInput("Enter your age: ", 1, 120);
        double weight = getDoubleInput("Enter your weight (kg): ", 20, 500);

        System.out.println("\n" + Colors.YELLOW_BOLD + "Activity level:" + Colors.RESET);
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
        System.out.println(Colors.GREEN_BOLD + "User profile created successfully!" + Colors.RESET);
    }

    private void displayMainMenu() {
        // LocalDate currentDate = controller.getCurrentDate();
        // double consumedCalories = controller.getCurrentDayCalories();
        // double targetCalories = controller.getTargetCalories();
        // double difference = targetCalories - consumedCalories;
        // String calorieColor = Colors.colorizeCalories(consumedCalories,
        // targetCalories);

        System.out.println("\n" + Colors.CYAN_BOLD + "===== Main Menu =====" + Colors.RESET);
        // System.out.println(Colors.BLUE + "Date: " + Colors.YELLOW_BOLD +
        // currentDate.format(DateTimeFormatter.ISO_DATE) + Colors.RESET);
        // System.out.println(Colors.BLUE + "Calculator: " + Colors.WHITE +
        // controller.getCurrentCalculatorName() + Colors.RESET);
        // System.out.println(Colors.BLUE + "Target Calories: " + Colors.WHITE +
        // targetCalories + Colors.RESET);
        // System.out.println(Colors.BLUE + "Consumed Calories: " + calorieColor +
        // consumedCalories + Colors.RESET);

        // Display difference with color based on value
        // if (difference < 0) {
        // System.out.println(Colors.BLUE + "Difference: " + Colors.RED_BOLD +
        // difference + " (exceeded)" + Colors.RESET);
        // } else {
        // System.out.println(Colors.BLUE + "Difference: " + Colors.GREEN_BOLD +
        // difference + " (remaining)" + Colors.RESET);
        // }

        System.out.println("\n" + Colors.YELLOW_BOLD + "Menu Options:" + Colors.RESET);
        System.out.println(
                Colors.WHITE_BOLD + "1." + Colors.WHITE + " Search Foods/Add Food to Today's Log" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "2." + Colors.WHITE + " View Food Log" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "3." + Colors.WHITE + " Update Food Log" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "4." + Colors.WHITE + " Add New Basic Food" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "5." + Colors.WHITE + " Create Composite Food" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "6." + Colors.WHITE + " Change Date" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "7." + Colors.WHITE + " Update User Info" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "8." + Colors.WHITE + " Change Calorie Calculator" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "9." + Colors.WHITE + " Undo Last Action" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "10." + Colors.WHITE + " Save Data" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "11." + Colors.WHITE + " View Command History" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "12." + Colors.WHITE + " View Nutritional Summary" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "0." + Colors.RED_BOLD + " Exit" + Colors.RESET);
    }

    private void processMainMenuChoice(int choice) {
        switch (choice) {
            case 0:
                exitApplication();
                break;
            case 1:
                searchFoods();
                break;
            case 2:
                viewFoodLog();
                break;
            case 3:
                updateFoodLog();
                break;
            case 4:
                addBasicFood();
                break;
            case 5:
                createCompositeFood();
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
                System.out.println(Colors.RED_BOLD + "Invalid choice. Please try again." + Colors.RESET);
        }
    }

    // private void addFoodToLog() {
    // System.out.println("\n" + Colors.CYAN_BOLD + "==== Add Food to Log ====" +
    // Colors.RESET);

    // // Search for food
    // System.out.print(Colors.BLUE + "Enter search term for food: " +
    // Colors.RESET);
    // String searchTerm = scanner.nextLine().trim();
    // List<Food> foods = controller.searchFoods(searchTerm);

    // if (foods.isEmpty()) {
    // System.out.println(Colors.YELLOW + "No matching foods found." +
    // Colors.RESET);
    // return;
    // }

    // // Display search results
    // System.out.println("\n" + Colors.YELLOW_BOLD + "Search results:" +
    // Colors.RESET);
    // for (int i = 0; i < foods.size(); i++) {
    // Food food = foods.get(i);
    // System.out.printf(Colors.WHITE_BOLD + "%d. " + Colors.GREEN + "%s " +
    // Colors.WHITE + "(%.2f calories per serving)\n" + Colors.RESET,
    // i + 1, food.getName(), food.getCaloriesPerServing());
    // }

    // // Select food
    // int foodIndex = getIntInput("Select a food (0 to cancel): ", 0, foods.size())
    // - 1;
    // if (foodIndex == -1) {
    // System.out.println(Colors.YELLOW + "Operation cancelled." + Colors.RESET);
    // return;
    // }

    // Food selectedFood = foods.get(foodIndex);

    // // Enter number of servings
    // double servings = getDoubleInput(Colors.BLUE + "Enter number of servings: " +
    // Colors.RESET, 0.1, 100);

    // // Add to log
    // controller.addFoodEntry(selectedFood, servings);
    // System.out.printf(Colors.GREEN_BOLD + "Added %.1f serving(s) of %s to the
    // log.\n" + Colors.RESET,
    // servings, selectedFood.getName());
    // }

    private void viewFoodLog() {
        System.out.println("\n" + Colors.CYAN_BOLD + "==== View Food Log ====" + Colors.RESET);

        LocalDate logDate = promptForDate("Enter date to view (YYYY-MM-DD) or press Enter for current date: ");
        if (logDate == null) {
            return;
        }

        DailyLog log = controller.getDailyLog(logDate);
        List<FoodEntry> entries = log.getEntries();

        System.out.println("\n" + Colors.CYAN_BOLD + "==== Food Log for " +
                Colors.YELLOW_BOLD + logDate.format(DateTimeFormatter.ISO_DATE) +
                Colors.CYAN_BOLD + " ====" + Colors.RESET);

        if (entries.isEmpty()) {
            System.out.println(Colors.YELLOW + "No entries for this day." + Colors.RESET);
            return;
        }

        double totalCalories = 0;

        System.out.println(Colors.YELLOW_BOLD + "Food entries:" + Colors.RESET);
        for (int i = 0; i < entries.size(); i++) {
            FoodEntry entry = entries.get(i);
            double entryCalories = entry.getTotalCalories();
            totalCalories += entryCalories;

            System.out.printf(
                    Colors.WHITE_BOLD + "%d. " + Colors.GREEN + "%s " + Colors.WHITE + "- %.1f serving(s) - " +
                            Colors.YELLOW + "%.2f calories\n" + Colors.RESET,
                    i + 1, entry.getFood().getName(), entry.getServings(), entryCalories);
        }

        // Set controller date to selected date to ensure target calculations use
        // correct data
        LocalDate currentDate = controller.getCurrentDate(); // save current date
        controller.setCurrentDate(logDate);

        double targetCalories = controller.getTargetCalories();
        System.out.println("\n" + Colors.BLUE + "Total calories consumed: " +
                Colors.colorizeCalories(totalCalories, targetCalories) + totalCalories + Colors.RESET);
        System.out.println(Colors.BLUE + "Target calories: " + Colors.WHITE + targetCalories + Colors.RESET);

        // Display difference with color based on value
        double difference = targetCalories - totalCalories;
        if (difference < 0) {
            System.out.println(Colors.BLUE + "Difference: " + Colors.RED_BOLD + difference +
                    " (exceeded target)" + Colors.RESET);
        } else {
            System.out.println(Colors.BLUE + "Difference: " + Colors.GREEN_BOLD + difference +
                    " (remaining)" + Colors.RESET);
        }

        controller.setCurrentDate(currentDate); // restore current date
    }

    private void updateFoodLog() {
        System.out.println("\n" + Colors.CYAN_BOLD + "==== Update Food Log ====" + Colors.RESET);

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

        System.out.println("\n" + Colors.CYAN_BOLD + "==== Food Log for " +
                Colors.YELLOW_BOLD + logDate.format(DateTimeFormatter.ISO_DATE) +
                Colors.CYAN_BOLD + " ====" + Colors.RESET);

        if (entries.isEmpty()) {
            System.out.println(Colors.YELLOW + "No entries for this day." + Colors.RESET);
            System.out.println("\n" + Colors.BLUE_BOLD
                    + "To add food entries, please use 'Search Foods/Add Food to Log' from the main menu."
                    + Colors.RESET);

            // Restore original date and return
            controller.setCurrentDate(currentDate);
            return;
        }

        // Display all entries
        System.out.println(Colors.YELLOW_BOLD + "Current entries:" + Colors.RESET);
        for (int i = 0; i < entries.size(); i++) {
            FoodEntry entry = entries.get(i);
            System.out.printf(
                    Colors.WHITE_BOLD + "%d. " + Colors.GREEN + "%s " + Colors.WHITE + "- %.1f serving(s) - " +
                            Colors.YELLOW + "%.2f calories\n" + Colors.RESET,
                    i + 1, entry.getFood().getName(), entry.getServings(),
                    entry.getTotalCalories());
        }

        System.out.println("\n" + Colors.YELLOW_BOLD + "Options:" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "1." + Colors.RED_BOLD + " Remove a food entry" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "2." + Colors.YELLOW + " Modify a food entry" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "0." + Colors.BLUE + " Return to main menu" + Colors.RESET);

        int choice = getIntInput("Enter your choice: ", 0, 2);

        switch (choice) {
            case 0:
                break;
            case 1:
                // Remove food entry
                int entryIndex = getIntInput("Select entry to remove (0 to cancel): ", 0, entries.size()) - 1;
                if (entryIndex >= 0) {
                    controller.removeFoodEntry(entryIndex);
                    System.out.println(Colors.GREEN_BOLD + "Food entry removed successfully." + Colors.RESET);
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
                            String.format(
                                    "Enter new servings for " + Colors.GREEN + "%s" + Colors.RESET
                                            + " (current: %.1f): ",
                                    selectedFood.getName(), oldEntry.getServings()),
                            0.1, 100);

                    // Remove old entry and add new one
                    controller.removeFoodEntry(entryIndex);
                    controller.addFoodEntry(selectedFood, newServings);
                    System.out.println(Colors.GREEN_BOLD + "Food entry updated successfully." + Colors.RESET);
                }
                break;
        }

        // Restore original date
        controller.setCurrentDate(currentDate);
    }

    private LocalDate promptForDate(String prompt) {
        System.out.print(Colors.BLUE + prompt + Colors.RESET);
        String dateInput = scanner.nextLine().trim();

        if (dateInput.isEmpty()) {
            return controller.getCurrentDate();
        }

        try {
            return LocalDate.parse(dateInput);
        } catch (DateTimeParseException e) {
            System.out.println(Colors.RED + "Invalid date format. Please use YYYY-MM-DD." + Colors.RESET);
            return null;
        }
    }

    private void addBasicFood() {
        System.out.println("\n" + Colors.CYAN_BOLD + "==== Add New Basic Food ====" + Colors.RESET);

        System.out.print(Colors.BLUE + "Enter food name: " + Colors.RESET);
        String name = scanner.nextLine().trim();

        // Check if food with this name already exists
        if (controller.getFoodByName(name).isPresent()) {
            System.out.println(Colors.RED + "A food with this name already exists." + Colors.RESET);
            return;
        }

        System.out.print(Colors.BLUE + "Enter keywords (comma-separated): " + Colors.RESET);
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

        double calories = getDoubleInput(Colors.BLUE + "Enter calories per serving: " + Colors.RESET, 0, 10000);

        controller.addBasicFood(name, keywords, calories);
        System.out.println(Colors.GREEN_BOLD + "Basic food added successfully." + Colors.RESET);
    }

    private void createCompositeFood() {
        System.out.println("\n" + Colors.CYAN_BOLD + "==== Create Composite Food ====" + Colors.RESET);

        System.out.print(Colors.BLUE + "Enter composite food name: " + Colors.RESET);
        String name = scanner.nextLine().trim();

        // Check if food with this name already exists
        if (controller.getFoodByName(name).isPresent()) {
            System.out.println(Colors.RED + "A food with this name already exists." + Colors.RESET);
            return;
        }

        System.out.print(Colors.BLUE + "Enter keywords (comma-separated): " + Colors.RESET);
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
            System.out.print(Colors.BLUE + "Enter search term for component food (empty to finish): " + Colors.RESET);
            String searchTerm = scanner.nextLine().trim();

            if (searchTerm.isEmpty()) {
                addingComponents = false;
                continue;
            }

            List<Food> foods = controller.searchFoods(searchTerm);

            if (foods.isEmpty()) {
                System.out.println(Colors.YELLOW + "No matching foods found." + Colors.RESET);
                continue;
            }

            // Display search results
            System.out.println("\n" + Colors.YELLOW_BOLD + "Search results:" + Colors.RESET);
            for (int i = 0; i < foods.size(); i++) {
                Food food = foods.get(i);
                System.out.printf(
                        Colors.WHITE_BOLD + "%d. " + Colors.GREEN + "%s " + Colors.WHITE
                                + "(%.2f calories per serving)\n" + Colors.RESET,
                        i + 1, food.getName(), food.getCaloriesPerServing());
            }

            // Select food
            int foodIndex = getIntInput("Select a food (0 to cancel): ", 0, foods.size()) - 1;
            if (foodIndex == -1) {
                System.out.println(Colors.YELLOW + "Component selection cancelled." + Colors.RESET);
                continue;
            }

            Food selectedFood = foods.get(foodIndex);

            // Enter number of servings
            double servings = getDoubleInput(Colors.BLUE + "Enter number of servings: " + Colors.RESET, 0.1, 100);

            // Add to components
            components.put(selectedFood, servings);
            System.out.printf(Colors.GREEN_BOLD + "Added %.1f serving(s) of %s to the composite food.\n" + Colors.RESET,
                    servings, selectedFood.getName());
        }

        if (components.isEmpty()) {
            System.out.println(Colors.RED + "No components added. Composite food creation cancelled." + Colors.RESET);
            return;
        }

        controller.addCompositeFood(name, keywords, components);
        System.out.println(Colors.GREEN_BOLD + "Composite food created successfully." + Colors.RESET);
    }

    private void searchFoods() {
        System.out.println("\n" + Colors.CYAN_BOLD + "==== Search Foods ====" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "1." + Colors.WHITE + " View all foods" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "2." + Colors.WHITE + " Search by name/keyword" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "0." + Colors.BLUE + " Return to main menu" + Colors.RESET);

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
                    System.out.println(Colors.YELLOW + "No foods found in the database." + Colors.RESET);
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

                System.out.println("\n" + Colors.BLUE_BOLD + "-- Basic Foods --" + Colors.RESET);
                for (int i = 0; i < basicFoods.size(); i++) {
                    System.out.printf(Colors.WHITE_BOLD + "%d. " + Colors.GREEN + "%s\n" + Colors.RESET,
                            i + 1, basicFoods.get(i).getName());
                }

                System.out.println("\n" + Colors.PURPLE_BOLD + "-- Composite Foods --" + Colors.RESET);
                for (int i = 0; i < compositeFoods.size(); i++) {
                    System.out.printf(Colors.WHITE_BOLD + "%d. " + Colors.CYAN + "%s\n" + Colors.RESET,
                            basicFoods.size() + i + 1, compositeFoods.get(i).getName());
                }

                // Merge lists back for selection
                foods = new ArrayList<>(basicFoods);
                foods.addAll(compositeFoods);
                break;
            case 2:
                // Search by name/keyword
                System.out.print(Colors.BLUE + "Enter search term: " + Colors.RESET);
                String searchTerm = scanner.nextLine().trim();
                foods = controller.searchFoods(searchTerm);
                if (foods.isEmpty()) {
                    System.out.println(Colors.YELLOW + "No matching foods found." + Colors.RESET);
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

                System.out.println(
                        "\n" + Colors.BLUE_BOLD + "-- Basic Foods Matching \"" + searchTerm + "\" --" + Colors.RESET);
                if (basicFoods.isEmpty()) {
                    System.out.println(Colors.YELLOW + "No basic foods found matching your search." + Colors.RESET);
                } else {
                    for (int i = 0; i < basicFoods.size(); i++) {
                        System.out.printf(Colors.WHITE_BOLD + "%d. " + Colors.GREEN + "%s\n" + Colors.RESET,
                                i + 1, basicFoods.get(i).getName());
                    }
                }

                System.out.println("\n" + Colors.PURPLE_BOLD + "-- Composite Foods Matching \"" + searchTerm + "\" --"
                        + Colors.RESET);
                if (compositeFoods.isEmpty()) {
                    System.out.println(Colors.YELLOW + "No composite foods found matching your search." + Colors.RESET);
                } else {
                    for (int i = 0; i < compositeFoods.size(); i++) {
                        System.out.printf(Colors.WHITE_BOLD + "%d. " + Colors.CYAN + "%s\n" + Colors.RESET,
                                basicFoods.size() + i + 1, compositeFoods.get(i).getName());
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
            System.out.println("\n" + Colors.YELLOW_BOLD + "Options:" + Colors.RESET);
            System.out.println(
                    Colors.WHITE_BOLD + "1." + Colors.WHITE + " View details of a specific food" + Colors.RESET);
            System.out.println(Colors.WHITE_BOLD + "2." + Colors.GREEN + " Add a food to today's log" + Colors.RESET);
            System.out.println(Colors.WHITE_BOLD + "0." + Colors.BLUE + " Return to main menu" + Colors.RESET);

            choice = getIntInput("Enter your choice: ", 0, 2);

            switch (choice) {
                case 0:
                    stayInFoodMenu = false;
                    break;
                case 1:
                    // View details of a specific food
                    int foodIndex = getIntInput("Enter food number to view details (0 to cancel): ", 0, foods.size())
                            - 1;
                    if (foodIndex == -1) {
                        continue; // Go back to options instead of returning to main menu
                    }

                    Food selectedFood = foods.get(foodIndex);
                    displayFoodDetails(selectedFood);
                    // No return statement here, so it goes back to the options menu
                    break;
                case 2:
                    // Add food to today's log
                    foodIndex = getIntInput("Enter food number to add to log (0 to cancel): ", 0, foods.size()) - 1;
                    if (foodIndex == -1) {
                        continue; // Go back to options instead of returning to main menu
                    }

                    selectedFood = foods.get(foodIndex);
                    double servings = getDoubleInput(Colors.BLUE + "Enter number of servings: " + Colors.RESET, 0.1,
                            100);

                    controller.addFoodEntry(selectedFood, servings);
                    System.out.printf(
                            Colors.GREEN_BOLD + "Added %.1f serving(s) of %s to today's log.\n" + Colors.RESET,
                            servings, selectedFood.getName());

                    System.out.println(
                            "\n" + Colors.YELLOW_BOLD + "Do you want to add more foods to the log?" + Colors.RESET);
                    System.out.println(Colors.WHITE_BOLD + "1." + Colors.GREEN + " Yes" + Colors.RESET);
                    System.out
                            .println(Colors.WHITE_BOLD + "2." + Colors.RED + " No, return to main menu" + Colors.RESET);
                    int moreFood = getIntInput("Enter your choice: ", 1, 2);
                    if (moreFood == 2) {
                        stayInFoodMenu = false;
                    }
                    break;
            }
        }
    }

    private void displayFoodDetails(Food food) {
        System.out.println("\n" + Colors.CYAN_BOLD + "==== Food Details ====" + Colors.RESET);
        System.out.println(Colors.BLUE + "Name: " + Colors.GREEN + food.getName() + Colors.RESET);
        System.out.println(Colors.BLUE + "Type: " + Colors.YELLOW + food.getClass().getSimpleName() + Colors.RESET);
        System.out.println(
                Colors.BLUE + "Calories per serving: " + Colors.WHITE + food.getCaloriesPerServing() + Colors.RESET);
        System.out.println(
                Colors.BLUE + "Keywords: " + Colors.PURPLE + String.join(", ", food.getKeywords()) + Colors.RESET);

        if (food.getClass().getSimpleName().equals("CompositeFood")) {
            System.out.println("\n" + Colors.YELLOW_BOLD + "Components:" + Colors.RESET);
            Map<Food, Double> components = ((CompositeFood) food).getComponents();
            int i = 1;
            for (Map.Entry<Food, Double> entry : components.entrySet()) {
                System.out.printf(
                        Colors.WHITE_BOLD + "  %d. " + Colors.GREEN + "%s " + Colors.WHITE + "- %.1f serving(s) - " +
                                Colors.YELLOW + "%.2f calories\n" + Colors.RESET,
                        i++, entry.getKey().getName(), entry.getValue(),
                        entry.getKey().getCaloriesPerServing() * entry.getValue());
            }
        }

        System.out.print("\n" + Colors.BLUE + "Press Enter to continue..." + Colors.RESET);
        scanner.nextLine();
    }

    private void changeDate() {
        System.out.println("\n" + Colors.CYAN_BOLD + "==== Change Date ====" + Colors.RESET);
        System.out.println(
                Colors.BLUE + "Current date: " + Colors.YELLOW_BOLD + controller.getCurrentDate() + Colors.RESET);

        System.out.print(Colors.BLUE + "Enter new date (YYYY-MM-DD) or 'today': " + Colors.RESET);
        String dateInput = scanner.nextLine().trim();

        LocalDate newDate;
        if (dateInput.equalsIgnoreCase("today")) {
            newDate = LocalDate.now();
        } else {
            try {
                newDate = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.println(Colors.RED + "Invalid date format. Please use YYYY-MM-DD." + Colors.RESET);
                return;
            }
        }

        controller.setCurrentDate(newDate);
        System.out.println(Colors.GREEN_BOLD + "Date changed to: " + Colors.YELLOW_BOLD + newDate + Colors.RESET);
    }

    private void updateUserInfo() {
        System.out.println("\n" + Colors.CYAN_BOLD + "==== Update User Info ====" + Colors.RESET);

        System.out.println(Colors.WHITE_BOLD + "1." + Colors.WHITE + " Update height" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "2." + Colors.WHITE + " Update age" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "3." + Colors.WHITE + " Update weight" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "4." + Colors.WHITE + " Update activity level" + Colors.RESET);
        System.out.println(Colors.WHITE_BOLD + "0." + Colors.CYAN_BOLD + " Back to main menu" + Colors.RESET);

        int choice = getIntInput("Enter your choice: ", 0, 4);

        switch (choice) {
            case 0:
                return;
            case 1:
                double height = getDoubleInput(Colors.BLUE + "Enter new height (cm): " + Colors.RESET, 50, 250);
                controller.updateUserHeight(height);
                System.out.println(Colors.GREEN_BOLD + "Height updated successfully." + Colors.RESET);
                break;
            case 2:
                int age = getIntInput(Colors.BLUE + "Enter new age: " + Colors.RESET, 1, 120);
                controller.updateUserAge(age);
                System.out.println(Colors.GREEN_BOLD + "Age updated successfully." + Colors.RESET);
                break;
            case 3:
                double weight = getDoubleInput(Colors.BLUE + "Enter new weight (kg): " + Colors.RESET, 20, 500);
                controller.updateUserWeight(weight);
                System.out.println(Colors.GREEN_BOLD + "Weight updated successfully." + Colors.RESET);
                break;
            case 4:
                System.out.println("\n" + Colors.YELLOW_BOLD + "Activity level:" + Colors.RESET);
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
                System.out.println(Colors.GREEN_BOLD + "Activity level updated successfully." + Colors.RESET);
                break;
        }
    }

    private void changeCalorieCalculator() {
        System.out.println("\n" + Colors.CYAN_BOLD + "==== Change Calorie Calculator ====" + Colors.RESET);
        System.out.println(Colors.BLUE + "Current calculator: " + Colors.YELLOW_BOLD
                + controller.getCurrentCalculatorName() + Colors.RESET);

        List<String> calculators = controller.getAvailableCalculators();
        for (int i = 0; i < calculators.size(); i++) {
            System.out.printf(Colors.WHITE_BOLD + "%d. " + Colors.WHITE + "%s\n" + Colors.RESET,
                    i + 1, calculators.get(i));
        }

        int calculatorIndex = getIntInput("Select calculator (0 to cancel): ", 0, calculators.size()) - 1;
        if (calculatorIndex == -1) {
            System.out.println(Colors.YELLOW + "Operation cancelled." + Colors.RESET);
            return;
        }

        controller.setCurrentCalculator(calculators.get(calculatorIndex));
        System.out.println(Colors.GREEN_BOLD + "Calculator changed to: " + Colors.YELLOW_BOLD +
                controller.getCurrentCalculatorName() + Colors.RESET);
    }

    private void undoLastAction() {
        if (!controller.getCommandHistory().isEmpty()) {
            System.out.println(Colors.BLUE + "Undoing: " + Colors.YELLOW +
                    controller.getCommandHistory().get(controller.getCommandHistory().size() - 1) + Colors.RESET);
            controller.undo();
            System.out.println(Colors.GREEN_BOLD + "Action undone successfully." + Colors.RESET);
        } else {
            System.out.println(Colors.YELLOW + "Nothing to undo." + Colors.RESET);
        }
    }

    private void saveData() {
        controller.saveAllData();
        System.out.println(Colors.GREEN_BOLD + "All data saved successfully." + Colors.RESET);
    }

    private void viewCommandHistory() {
        System.out.println("\n" + Colors.CYAN_BOLD + "==== Command History ====" + Colors.RESET);
        List<String> history = controller.getCommandHistory();

        if (history.isEmpty()) {
            System.out.println(Colors.YELLOW + "No commands in history." + Colors.RESET);
            return;
        }

        for (int i = history.size() - 1; i >= 0; i--) {
            System.out.printf(Colors.WHITE_BOLD + "%d. " + Colors.WHITE + "%s\n" + Colors.RESET,
                    history.size() - i, history.get(i));
        }
    }

    private void viewNutritionalSummary() {
        System.out.println("\n" + Colors.CYAN_BOLD + "==== View Nutritional Summary ====" + Colors.RESET);

        LocalDate summaryDate = promptForDate(
                "Enter date to view summary (YYYY-MM-DD) or press Enter for current date: ");
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
        System.out.println("\n" + Colors.YELLOW_BOLD + "=== Nutritional Summary ===" + Colors.RESET);
        System.out.println(Colors.BLUE + "Date: " + Colors.YELLOW_BOLD + summaryDate.format(DateTimeFormatter.ISO_DATE)
                + Colors.RESET);
        System.out.println(Colors.BLUE + "Calculator: " + Colors.WHITE + calculatorName + Colors.RESET);
        System.out.println(Colors.BLUE + "Target Calories: " + Colors.WHITE + targetCalories + Colors.RESET);
        System.out.println(Colors.BLUE + "Consumed Calories: " +
                Colors.colorizeCalories(consumedCalories, targetCalories) + consumedCalories + Colors.RESET);

        // Display difference with appropriate color
        if (difference < 0) {
            System.out.println(Colors.BLUE + "Difference: " + Colors.RED_BOLD + difference +
                    " (exceeded target)" + Colors.RESET);
        } else {
            System.out.println(Colors.BLUE + "Difference: " + Colors.GREEN_BOLD + difference +
                    " (remaining)" + Colors.RESET);
        }

        // If there are entries in the log, show a quick breakdown
        List<FoodEntry> entries = log.getEntries();
        if (!entries.isEmpty()) {
            System.out.println("\n" + Colors.BLUE + "Food entries: " + Colors.WHITE + entries.size() + Colors.RESET);

            // Find highest calorie item
            FoodEntry highestCalorieEntry = entries.stream()
                    .max(Comparator.comparing(FoodEntry::getTotalCalories))
                    .orElse(null);

            if (highestCalorieEntry != null) {
                System.out.println(Colors.BLUE + "Highest calorie item: " + Colors.GREEN +
                        highestCalorieEntry.getFood().getName() + Colors.WHITE + " (" +
                        highestCalorieEntry.getTotalCalories() + " calories)" + Colors.RESET);
            }
        }

        if (difference < 0) {
            System.out.println("\n" + Colors.RED + "You exceeded your calorie target by " +
                    Math.abs(difference) + " calories." + Colors.RESET);
        } else {
            System.out.println("\n" + Colors.GREEN + "You have " + difference +
                    " calories remaining for this day." + Colors.RESET);
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
        System.out.println(Colors.YELLOW_BOLD + "Saving data before exit..." + Colors.RESET);
        saveAllDataBeforeExit();
        System.out.println(Colors.GREEN_BOLD + "Thank you for using Calorie Tracker!" + Colors.RESET);
        running = false;
    }

    // Helper methods for input validation
    private int getIntInput(String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(Colors.BLUE + prompt + Colors.RESET);
            try {
                String input = scanner.nextLine().trim();
                value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    break;
                }
                System.out.printf(Colors.RED + "Please enter a number between %d and %d.\n" + Colors.RESET, min, max);
            } catch (NumberFormatException e) {
                System.out.println(Colors.RED + "Invalid input. Please enter a number." + Colors.RESET);
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
                System.out.printf(Colors.RED + "Please enter a number between %.1f and %.1f.\n" + Colors.RESET, min,
                        max);
            } catch (NumberFormatException e) {
                System.out.println(Colors.RED + "Invalid input. Please enter a number." + Colors.RESET);
            }
        }
        return value;
    }
}
