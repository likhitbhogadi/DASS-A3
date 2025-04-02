package com.calorietracker.command;

import com.calorietracker.db.LogManager;
import com.calorietracker.model.DailyLog;
import com.calorietracker.model.Food;
import com.calorietracker.model.FoodEntry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AddFoodEntryCommand implements Command {
    private final LogManager logManager;
    private final LocalDate date;
    private final Food food;
    private final double servings;
    private FoodEntry addedEntry;
    private int addedIndex;
    
    public AddFoodEntryCommand(LogManager logManager, LocalDate date, Food food, double servings) {
        this.logManager = logManager;
        this.date = date;
        this.food = food;
        this.servings = servings;
    }
    
    @Override
    public void execute() {
        logManager.addFoodEntry(date, food, servings);
        
        // Save the added entry for undo
        DailyLog log = logManager.getOrCreateLog(date);
        List<FoodEntry> entries = log.getEntries();
        addedEntry = entries.get(entries.size() - 1);
        addedIndex = entries.size() - 1;
    }
    
    @Override
    public void undo() {
        logManager.removeFoodEntry(date, addedIndex);
    }
    
    @Override
    public String getDescription() {
        return "Add " + servings + " serving(s) of " + food.getName();
    }
}
