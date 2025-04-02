package com.calorietracker.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyLog {
    private LocalDate date;
    private List<FoodEntry> entries;
    
    public DailyLog(LocalDate date) {
        this.date = date;
        this.entries = new ArrayList<>();
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public List<FoodEntry> getEntries() {
        return new ArrayList<>(entries);
    }
    
    public void addEntry(FoodEntry entry) {
        entries.add(entry);
    }
    
    public boolean removeEntry(FoodEntry entry) {
        return entries.remove(entry);
    }
    
    public double getTotalCalories() {
        return entries.stream()
                .mapToDouble(FoodEntry::getTotalCalories)
                .sum();
    }
}
