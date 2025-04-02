package com.calorietracker.controller;

import com.calorietracker.command.*;
import com.calorietracker.db.*;
import com.calorietracker.model.*;
import com.calorietracker.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CalorieTrackerController {
    private final FoodDatabase foodDatabase;
    private final LogManager logManager;
    private final ProfileManager profileManager;
    private final CommandInvoker commandInvoker;
    private User user;
    private LocalDate currentDate;
    private CalorieCalculator currentCalculator;
    private final Map<String, CalorieCalculator> calculators;
    
    public CalorieTrackerController(String basicFoodsPath, String compositeFoodsPath, String logPath, String profilePath) {
        this.foodDatabase = new JsonFoodDatabase(basicFoodsPath, compositeFoodsPath);
        this.logManager = new LogManager(logPath, foodDatabase);
        this.profileManager = new ProfileManager(profilePath);
        this.commandInvoker = new CommandInvoker();
        this.currentDate = LocalDate.now();
        
        // Initialize calculators
        calculators = new HashMap<>();
        calculators.put("harris-benedict", new HarrisBenedictCalculator());
        calculators.put("mifflin-st-jeor", new MifflinStJeorCalculator());
        currentCalculator = calculators.get("harris-benedict");
        
        // Load data
        foodDatabase.load();
        logManager.load();
        
        // Try to load user profile
        this.user = profileManager.loadUserProfile();
    }
    
    // User methods
    public void createUser(String gender, double height, int age, double weight, User.ActivityLevel activityLevel) {
        user = new User(gender, height, age, weight, activityLevel);
        profileManager.saveUserProfile(user);
    }
    
    public User getUser() {
        return user;
    }
    
    public void updateUserWeight(double weight) {
        if (user != null) {
            user.setWeight(currentDate, weight);
            profileManager.saveUserProfile(user);
        }
    }
    
    public void updateUserActivityLevel(User.ActivityLevel activityLevel) {
        if (user != null) {
            user.setActivityLevel(currentDate, activityLevel);
            profileManager.saveUserProfile(user);
        }
    }
    
    public void updateUserHeight(double height) {
        if (user != null) {
            // Need to create a new user since height is not modifiable in the User class
            User newUser = new User(user.getGender(), height, user.getAge(), 
                                  user.getWeight(currentDate), user.getActivityLevel(currentDate));
            this.user = newUser;
            profileManager.saveUserProfile(user);
        }
    }
    
    public void updateUserAge(int age) {
        if (user != null) {
            // Need to create a new user since age is not modifiable in the User class
            User newUser = new User(user.getGender(), user.getHeight(), age, 
                                  user.getWeight(currentDate), user.getActivityLevel(currentDate));
            this.user = newUser;
            profileManager.saveUserProfile(user);
        }
    }
    
    // public void updateUserGender(String gender) {
    //     if (user != null) {
    //         // Need to create a new user since gender is not modifiable in the User class
    //         User newUser = new User(gender, user.getHeight(), user.getAge(), 
    //                               user.getWeight(currentDate), user.getActivityLevel(currentDate));
    //         this.user = newUser;
    //         profileManager.saveUserProfile(user);
    //     }
    // }
    
    // Date methods
    public LocalDate getCurrentDate() {
        return currentDate;
    }
    
    public void setCurrentDate(LocalDate date) {
        this.currentDate = date;
    }
    
    // Calculator methods
    public List<String> getAvailableCalculators() {
        return new ArrayList<>(calculators.keySet());
    }
    
    public void setCurrentCalculator(String calculatorName) {
        if (calculators.containsKey(calculatorName)) {
            currentCalculator = calculators.get(calculatorName);
        }
    }
    
    public double getTargetCalories() {
        if (user != null && currentCalculator != null) {
            return Math.round(currentCalculator.calculateTargetCalories(user, currentDate));
        }
        return 0;
    }
    
    public String getCurrentCalculatorName() {
        return currentCalculator.getName();
    }
    
    // Food database methods
    public List<Food> getAllFoods() {
        return foodDatabase.getAllFoods();
    }
    
    public List<Food> searchFoods(String query) {
        return foodDatabase.searchFoods(query);
    }
    
    public Optional<Food> getFoodByName(String name) {
        return foodDatabase.getFoodByName(name);
    }
    
    public void addBasicFood(String name, List<String> keywords, double calories) {
        BasicFood food = new BasicFood(name, keywords, calories);
        foodDatabase.addBasicFood(food);
    }
    
    public void addCompositeFood(String name, List<String> keywords, Map<Food, Double> components) {
        CompositeFood food = new CompositeFood(name, keywords);
        for (Map.Entry<Food, Double> component : components.entrySet()) {
            food.addComponent(component.getKey(), component.getValue());
        }
        foodDatabase.addCompositeFood(food);
    }
    
    public void saveDatabase() {
        foodDatabase.save();
    }
    
    // Log methods
    public DailyLog getCurrentDayLog() {
        return logManager.getOrCreateLog(currentDate);
    }
    
    public DailyLog getDailyLog(LocalDate date) {
        return logManager.getOrCreateLog(date);
    }
    
    public double getCurrentDayCalories() {
        return getCurrentDayLog().getTotalCalories();
    }
    
    public double getCalorieDifference() {
        return getTargetCalories() - getCurrentDayCalories();
    }
    
    public void addFoodEntry(Food food, double servings) {
        Command command = new AddFoodEntryCommand(logManager, currentDate, food, servings);
        commandInvoker.executeCommand(command);
    }
    
    public void removeFoodEntry(int index) {
        Command command = new RemoveFoodEntryCommand(logManager, currentDate, index);
        commandInvoker.executeCommand(command);
    }
    
    public void undo() {
        if (commandInvoker.canUndo()) {
            commandInvoker.undo();
        }
    }
    
    public List<String> getCommandHistory() {
        List<String> history = new ArrayList<>();
        for (Command command : commandInvoker.getCommandHistory()) {
            history.add(command.getDescription());
        }
        return history;
    }
    
    public void saveLog() {
        logManager.save();
    }
    
    // Application lifecycle
    public void saveAllData() {
        foodDatabase.save();
        logManager.save();
        if (user != null) {
            profileManager.saveUserProfile(user);
        }
    }
}
