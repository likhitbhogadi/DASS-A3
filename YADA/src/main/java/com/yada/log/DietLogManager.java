package com.yada.log;

import com.yada.food.Food;
import com.yada.util.Pair;
import com.yada.food.BasicFood;

import java.io.*;
import java.util.*;

public class DietLogManager {
    private Map<String, LogEntry> logs;  // Date-based logs (YYYY-MM-DD)
    private Stack<LogEntry> undoStack;   // Stack for undoing actions

    public DietLogManager() {
        this.logs = new HashMap<>();
        this.undoStack = new Stack<>();
    }

    // Add food to the log for a specific date
    public void addFoodToLog(String date, Food food, int servings) {
        logs.putIfAbsent(date, new LogEntry(date));
        logs.get(date).addFood(food, servings);
        undoStack.push(new LogEntry(date));  // Save state for undo
    }

    // Undo the last action (if available)
    public void undo() {
        if (!undoStack.isEmpty()) {
            LogEntry lastEntry = undoStack.pop();
            logs.put(lastEntry.getDate(), lastEntry);  // Restore previous state
        } else {
            System.out.println("Nothing to undo!");
        }
    }

    // Save logs to a text file
    public void saveLogsToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (LogEntry log : logs.values()) {
            writer.write("Date: " + log.getDate());
            writer.newLine();
            for (Pair<Food, Integer> entry : log.getConsumedFoods()) {
                writer.write(entry.getKey().getId() + "," + entry.getValue());
                writer.newLine();
            }
        }
        writer.close();
    }

    // Load logs from a text file
    public void loadLogsFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        LogEntry currentLog = null;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Date:")) {
                String date = line.substring(6).trim();
                currentLog = new LogEntry(date);
                logs.put(date, currentLog);
            } else {
                String[] parts = line.split(",");
                String foodId = parts[0];
                int servings = Integer.parseInt(parts[1]);
                // Assuming food objects can be retrieved later using ID
                currentLog.addFood(new BasicFood(foodId, new ArrayList<>(), 0), servings);
            }
        }
        reader.close();
    }

    // Display all logs
    public void displayLogs() {
        for (LogEntry log : logs.values()) {
            log.displayLog();
        }
    }

    public void removeFoodFromLog(String date, Food food) {
        if (logs.containsKey(date)) {
            logs.get(date).removeFood(food);
        }
    }
}
