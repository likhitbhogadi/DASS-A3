package com.calorietracker.command;

import com.calorietracker.db.LogManager;
import com.calorietracker.model.DailyLog;
import com.calorietracker.model.FoodEntry;

import java.time.LocalDate;

public class RemoveFoodEntryCommand implements Command {
    private final LogManager logManager;
    private final LocalDate date;
    private final int entryIndex;
    private FoodEntry removedEntry;
    
    public RemoveFoodEntryCommand(LogManager logManager, LocalDate date, int entryIndex) {
        this.logManager = logManager;
        this.date = date;
        this.entryIndex = entryIndex;
    }
    
    @Override
    public void execute() {
        // Save the entry before removing for undo
        DailyLog log = logManager.getOrCreateLog(date);
        if (entryIndex >= 0 && entryIndex < log.getEntries().size()) {
            removedEntry = log.getEntries().get(entryIndex);
            logManager.removeFoodEntry(date, entryIndex);
        }
    }
    
    @Override
    public void undo() {
        if (removedEntry != null) {
            logManager.addFoodEntry(date, removedEntry.getFood(), removedEntry.getServings());
        }
    }
    
    @Override
    public String getDescription() {
        if (removedEntry != null) {
            return "Remove " + removedEntry.getServings() + " serving(s) of " + removedEntry.getFood().getName();
        }
        return "Remove food entry";
    }
}
