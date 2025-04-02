package com.calorietracker.db;

import com.calorietracker.model.DailyLog;
import com.calorietracker.model.Food;
import com.calorietracker.model.FoodEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LogManager {
    private final String logFilePath;
    private final Gson gson;
    private final FoodDatabase foodDatabase;
    private final Map<LocalDate, DailyLog> logs;
    
    public LogManager(String logFilePath, FoodDatabase foodDatabase) {
        this.logFilePath = logFilePath;
        this.foodDatabase = foodDatabase;
        this.logs = new HashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        
        // Create log file if it doesn't exist
        createFileIfNotExists(logFilePath);
    }
    
    private void createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }
                file.createNewFile();
                // Initialize with empty array
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]");
                }
            } catch (IOException e) {
                System.err.println("Error creating file: " + filePath);
                e.printStackTrace();
            }
        }
    }
    
    public DailyLog getOrCreateLog(LocalDate date) {
        if (!logs.containsKey(date)) {
            logs.put(date, new DailyLog(date));
        }
        return logs.get(date);
    }
    
    public boolean addFoodEntry(LocalDate date, Food food, double servings) {
        DailyLog log = getOrCreateLog(date);
        FoodEntry entry = new FoodEntry(food, servings, LocalDateTime.now());
        log.addEntry(entry);
        return true;
    }
    
    public boolean removeFoodEntry(LocalDate date, int entryIndex) {
        DailyLog log = getOrCreateLog(date);
        if (entryIndex >= 0 && entryIndex < log.getEntries().size()) {
            FoodEntry entry = log.getEntries().get(entryIndex);
            return log.removeEntry(entry);
        }
        return false;
    }
    
    public void save() {
        try (FileWriter writer = new FileWriter(logFilePath)) {
            JsonArray jsonArray = new JsonArray();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
            
            for (DailyLog log : logs.values()) {
                if (log.getEntries().isEmpty()) continue;
                
                JsonObject jsonLog = new JsonObject();
                jsonLog.addProperty("date", log.getDate().format(dateFormatter));
                
                JsonArray entriesArray = new JsonArray();
                for (FoodEntry entry : log.getEntries()) {
                    JsonObject jsonEntry = new JsonObject();
                    jsonEntry.addProperty("foodName", entry.getFood().getName());
                    jsonEntry.addProperty("servings", entry.getServings());
                    jsonEntry.addProperty("timestamp", entry.getTimestamp().format(dateTimeFormatter));
                    entriesArray.add(jsonEntry);
                }
                
                jsonLog.add("entries", entriesArray);
                jsonArray.add(jsonLog);
            }
            
            gson.toJson(jsonArray, writer);
        } catch (IOException e) {
            System.err.println("Error saving logs to file");
            e.printStackTrace();
        }
    }
    
    public void load() {
        logs.clear();
        
        try (FileReader reader = new FileReader(logFilePath)) {
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
            
            if (jsonArray != null) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
                
                for (JsonElement element : jsonArray) {
                    JsonObject jsonLog = element.getAsJsonObject();
                    LocalDate date = LocalDate.parse(jsonLog.get("date").getAsString(), dateFormatter);
                    
                    DailyLog log = getOrCreateLog(date);
                    
                    JsonArray entriesArray = jsonLog.get("entries").getAsJsonArray();
                    for (JsonElement entryElement : entriesArray) {
                        JsonObject jsonEntry = entryElement.getAsJsonObject();
                        String foodName = jsonEntry.get("foodName").getAsString();
                        double servings = jsonEntry.get("servings").getAsDouble();
                        LocalDateTime timestamp = LocalDateTime.parse(
                            jsonEntry.get("timestamp").getAsString(), dateTimeFormatter);
                        
                        Optional<Food> food = foodDatabase.getFoodByName(foodName);
                        if (food.isPresent()) {
                            FoodEntry entry = new FoodEntry(food.get(), servings, timestamp);
                            log.addEntry(entry);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading logs from file");
            e.printStackTrace();
        }
    }
}
