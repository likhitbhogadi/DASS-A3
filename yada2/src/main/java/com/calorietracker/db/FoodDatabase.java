package com.calorietracker.db;

import com.calorietracker.model.BasicFood;
import com.calorietracker.model.CompositeFood;
import com.calorietracker.model.Food;

import java.util.List;
import java.util.Optional;

public interface FoodDatabase {
    List<Food> getAllFoods();
    List<BasicFood> getAllBasicFoods();
    List<CompositeFood> getAllCompositeFoods();
    
    Optional<Food> getFoodByName(String name);
    List<Food> searchFoods(String query);
    
    void addBasicFood(BasicFood food);
    void addCompositeFood(CompositeFood food);
    
    boolean removeFood(String name);
    
    void save();
    void load();
}
