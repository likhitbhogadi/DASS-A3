package com.calorietracker.db;

import com.calorietracker.model.User;
import com.calorietracker.model.UserProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ProfileManager {
    private final String profileFilePath;
    private final Gson gson;
    
    public ProfileManager(String profileFilePath) {
        this.profileFilePath = profileFilePath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        
        // Create profile file if it doesn't exist
        createFileIfNotExists(profileFilePath);
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
                
                // Initialize with empty profile
                try (FileWriter writer = new FileWriter(file)) {
                    UserProfile emptyProfile = new UserProfile();
                    gson.toJson(emptyProfile, writer);
                }
            } catch (IOException e) {
                System.err.println("Error creating file: " + filePath);
                e.printStackTrace();
            }
        }
    }
    
    public User loadUserProfile() {
        try (FileReader reader = new FileReader(profileFilePath)) {
            UserProfile profile = gson.fromJson(reader, UserProfile.class);
            
            // If profile exists and has gender set (indicating it's been configured)
            if (profile != null && !profile.getGender().isEmpty()) {
                return profile.toUser();
            }
            
        } catch (IOException e) {
            System.err.println("Error loading user profile from file");
            e.printStackTrace();
        }
        
        return null; // No profile found or error loading profile
    }
    
    public void saveUserProfile(User user) {
        try (FileWriter writer = new FileWriter(profileFilePath)) {
            UserProfile profile = UserProfile.fromUser(user);
            
            // Since we can't directly access the history maps in the User class,
            // we need to update them manually here
            profile.getWeightHistory().clear();
            profile.getActivityLevelHistory().clear();
            
            // At minimum, save the current weight and activity level for today's date
            String today = java.time.LocalDate.now().toString();
            profile.getWeightHistory().put(today, user.getWeight(java.time.LocalDate.now()));
            profile.getActivityLevelHistory().put(today, user.getActivityLevel(java.time.LocalDate.now()).name());
            
            gson.toJson(profile, writer);
        } catch (IOException e) {
            System.err.println("Error saving user profile to file");
            e.printStackTrace();
        }
    }
}
