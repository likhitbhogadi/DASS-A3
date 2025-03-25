package com.yada.profile;

public class CalorieCalculator {

    // Method 1: Harris-Benedict Equation (for Basal Metabolic Rate - BMR)
    public static int calculateHarrisBenedict(UserProfile user) {
        int bmr;
        if (user.getGender().equalsIgnoreCase("Male")) {
            bmr = (int) (88.36 + (13.4 * user.getWeight()) + (4.8 * user.getHeight()) - (5.7 * user.getAge()));
        } else {
            bmr = (int) (447.6 + (9.2 * user.getWeight()) + (3.1 * user.getHeight()) - (4.3 * user.getAge()));
        }

        return adjustForActivityLevel(bmr, user.getActivityLevel());
    }

    // Method 2: Mifflin-St Jeor Equation (similar to Harris-Benedict but more recent)
    public static int calculateMifflinStJeor(UserProfile user) {
        int bmr;
        if (user.getGender().equalsIgnoreCase("Male")) {
            bmr = (int) (10 * user.getWeight() + 6.25 * user.getHeight() - 5 * user.getAge() + 5);
        } else {
            bmr = (int) (10 * user.getWeight() + 6.25 * user.getHeight() - 5 * user.getAge() - 161);
        }

        return adjustForActivityLevel(bmr, user.getActivityLevel());
    }

    // Adjusts the BMR based on activity level (returns total calories)
    private static int adjustForActivityLevel(int bmr, String activityLevel) {
        switch (activityLevel.toLowerCase()) {
            case "sedentary":
                return (int) (bmr * 1.2);  // Little or no exercise
            case "moderate":
                return (int) (bmr * 1.55);  // Moderate exercise
            case "active":
                return (int) (bmr * 1.725);  // Heavy exercise
            default:
                throw new IllegalArgumentException("Unknown activity level: " + activityLevel);
        }
    }
}
