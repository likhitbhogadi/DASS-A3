package com.yada.db;

import com.yada.food.BasicFood;
import com.yada.food.CompositeFood;
import com.yada.food.Food;

import java.io.*;
import java.util.*;

public class FoodDatabase {
    private List<Food> foods;  // List of all foods (basic + composite)

    public FoodDatabase() {
        this.foods = new ArrayList<>();
    }

    // Add a basic food to the database
    public void addBasicFood(BasicFood food) {
        foods.add(food);
    }

    // Add a composite food to the database
    public void addCompositeFood(CompositeFood food) {
        foods.add(food);
    }

    // Load food data from a file (basic example)
    public void loadFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");  // Assuming CSV format
            String id = parts[0];
            List<String> keywords = Arrays.asList(parts[1].split("\\|"));
            int calories = Integer.parseInt(parts[2]);

            addBasicFood(new BasicFood(id, keywords, calories));
        }
        reader.close();
    }

    // Save the food database to a file
    public void saveToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (Food food : foods) {
            writer.write(food.getId() + "," + String.join("|", food.getKeywords()) + "," + food.getCalories());
            writer.newLine();
        }
        writer.close();
    }

    // Find foods matching a keyword
    public List<Food> searchByKeyword(String keyword) {
        List<Food> results = new ArrayList<>();
        for (Food food : foods) {
            if (food.matchesKeyword(keyword)) {
                results.add(food);
            }
        }
        return results;
    }
}
