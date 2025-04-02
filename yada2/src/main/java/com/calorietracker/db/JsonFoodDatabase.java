package com.calorietracker.db;

import com.calorietracker.model.BasicFood;
import com.calorietracker.model.CompositeFood;
import com.calorietracker.model.Food;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class JsonFoodDatabase implements FoodDatabase {
    private final String basicFoodsFilePath;
    private final String compositeFoodsFilePath;
    private final Gson gson;
    
    // Use a map to store foods for quick lookup (flyweight pattern)
    private final Map<String, BasicFood> basicFoods;
    private final Map<String, CompositeFood> compositeFoods;
    
    public JsonFoodDatabase(String basicFoodsFilePath, String compositeFoodsFilePath) {
        this.basicFoodsFilePath = basicFoodsFilePath;
        this.compositeFoodsFilePath = compositeFoodsFilePath;
        this.basicFoods = new HashMap<>();
        this.compositeFoods = new HashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        
        // Create files if they don't exist
        createFileIfNotExists(basicFoodsFilePath);
        createFileIfNotExists(compositeFoodsFilePath);
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
    
    @Override
    public List<Food> getAllFoods() {
        List<Food> allFoods = new ArrayList<>();
        allFoods.addAll(getAllBasicFoods());
        allFoods.addAll(getAllCompositeFoods());
        return allFoods;
    }
    
    @Override
    public List<BasicFood> getAllBasicFoods() {
        return new ArrayList<>(basicFoods.values());
    }
    
    @Override
    public List<CompositeFood> getAllCompositeFoods() {
        return new ArrayList<>(compositeFoods.values());
    }
    
    @Override
    public Optional<Food> getFoodByName(String name) {
        Food food = basicFoods.get(name);
        if (food == null) {
            food = compositeFoods.get(name);
        }
        return Optional.ofNullable(food);
    }
    
    @Override
    public List<Food> searchFoods(String query) {
        String lowercaseQuery = query.toLowerCase();
        return getAllFoods().stream()
                .filter(food -> food.matchesKeyword(lowercaseQuery))
                .collect(Collectors.toList());
    }
    
    @Override
    public void addBasicFood(BasicFood food) {
        basicFoods.put(food.getName(), food);
    }
    
    @Override
    public void addCompositeFood(CompositeFood food) {
        compositeFoods.put(food.getName(), food);
    }
    
    @Override
    public boolean removeFood(String name) {
        boolean removed = basicFoods.remove(name) != null;
        return removed || compositeFoods.remove(name) != null;
    }
    
    @Override
    public void save() {
        // Save basic foods
        try (FileWriter writer = new FileWriter(basicFoodsFilePath)) {
            JsonArray jsonArray = new JsonArray();
            
            for (BasicFood food : basicFoods.values()) {
                JsonObject jsonFood = new JsonObject();
                jsonFood.addProperty("name", food.getName());
                jsonFood.addProperty("calories", food.getCaloriesPerServing());
                
                JsonArray keywordsArray = new JsonArray();
                for (String keyword : food.getKeywords()) {
                    keywordsArray.add(keyword);
                }
                jsonFood.add("keywords", keywordsArray);
                
                jsonArray.add(jsonFood);
            }
            
            gson.toJson(jsonArray, writer);
        } catch (IOException e) {
            System.err.println("Error saving basic foods to JSON");
            e.printStackTrace();
        }
        
        // Save composite foods
        try (FileWriter writer = new FileWriter(compositeFoodsFilePath)) {
            JsonArray jsonArray = new JsonArray();
            
            for (CompositeFood food : compositeFoods.values()) {
                JsonObject jsonFood = new JsonObject();
                jsonFood.addProperty("name", food.getName());
                
                JsonArray keywordsArray = new JsonArray();
                for (String keyword : food.getKeywords()) {
                    keywordsArray.add(keyword);
                }
                jsonFood.add("keywords", keywordsArray);
                
                JsonArray componentsArray = new JsonArray();
                for (Map.Entry<Food, Double> component : food.getComponents().entrySet()) {
                    JsonObject jsonComponent = new JsonObject();
                    jsonComponent.addProperty("foodName", component.getKey().getName());
                    jsonComponent.addProperty("servings", component.getValue());
                    componentsArray.add(jsonComponent);
                }
                jsonFood.add("components", componentsArray);
                
                jsonArray.add(jsonFood);
            }
            
            gson.toJson(jsonArray, writer);
        } catch (IOException e) {
            System.err.println("Error saving composite foods to JSON");
            e.printStackTrace();
        }
    }
    
    @Override
    public void load() {
        // Clear existing data
        basicFoods.clear();
        compositeFoods.clear();
        
        // Load basic foods
        try (FileReader reader = new FileReader(basicFoodsFilePath)) {
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
            
            if (jsonArray != null) {
                for (JsonElement element : jsonArray) {
                    JsonObject jsonFood = element.getAsJsonObject();
                    String name = jsonFood.get("name").getAsString();
                    double calories = jsonFood.get("calories").getAsDouble();
                    
                    List<String> keywords = new ArrayList<>();
                    JsonArray keywordsArray = jsonFood.get("keywords").getAsJsonArray();
                    for (JsonElement keyword : keywordsArray) {
                        keywords.add(keyword.getAsString());
                    }
                    
                    BasicFood food = new BasicFood(name, keywords, calories);
                    basicFoods.put(name, food);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading basic foods from JSON");
            e.printStackTrace();
        }
        
        // Load composite foods - we need to load basic foods first
        try (FileReader reader = new FileReader(compositeFoodsFilePath)) {
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
            
            if (jsonArray != null) {
                for (JsonElement element : jsonArray) {
                    JsonObject jsonFood = element.getAsJsonObject();
                    String name = jsonFood.get("name").getAsString();
                    
                    List<String> keywords = new ArrayList<>();
                    JsonArray keywordsArray = jsonFood.get("keywords").getAsJsonArray();
                    for (JsonElement keyword : keywordsArray) {
                        keywords.add(keyword.getAsString());
                    }
                    
                    CompositeFood food = new CompositeFood(name, keywords);
                    
                    JsonArray componentsArray = jsonFood.get("components").getAsJsonArray();
                    for (JsonElement componentElement : componentsArray) {
                        JsonObject jsonComponent = componentElement.getAsJsonObject();
                        String foodName = jsonComponent.get("foodName").getAsString();
                        double servings = jsonComponent.get("servings").getAsDouble();
                        
                        // Try to find the component food
                        Optional<Food> componentFood = getFoodByName(foodName);
                        if (componentFood.isPresent()) {
                            food.addComponent(componentFood.get(), servings);
                        }
                    }
                    
                    compositeFoods.put(name, food);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading composite foods from JSON");
            e.printStackTrace();
        }
    }
}
