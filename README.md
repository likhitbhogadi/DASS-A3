# **YADA - Yet Another Diet Assistant**  
YADA is a command-line-based diet management software designed to help users track their daily food intake, manage their calorie consumption, and achieve their diet goals.

## **Features**  
- **Food Database**:  
  - Add basic and composite foods.  
  - Store food entries with calories, servings, and keywords.  
  - Support for composite foods (e.g., sandwich made of bread and peanut butter).  

- **Daily Logs**:  
  - Log food consumption for specific dates.  
  - View, update, and delete food entries for any day.  
  - Undo actions (like adding or deleting food entries).  

- **User Profile and Calorie Goals**:  
  - Manage user profile (age, height, weight, gender, and activity level).  
  - Calculate daily calorie targets using Harris-Benedict and Mifflin-St Jeor equations.  
  - Dynamically switch between different calorie calculation methods.  

- **Persistence**:  
  - Save and load food logs and user data from `logs.txt`.  
  - Logs are automatically saved and can be edited manually if needed.  

---

## **Getting Started**  

### **Requirements**  
- **Java 17 or later**  
- **Apache Maven**  

---

### **Running the Project**  

1. **Clone the repository (if applicable)**:  
   ```bash
   git clone <repository-url>
   cd YADA
   ```

2. **Compile the code:**  
   ```bash
   mvn compile
   ```

3. **Run the program:**  
   ```bash
   mvn exec:java
   ```

4. **Interact with YADA:**  
   You can run the following commands in the CLI:

   - **Add Food:**  
     ```
     add <food_name> <calories> <servings>
     Example: add apple 100 2
     ```

   - **View Logs for a Specific Date:**  
     ```
     view <date>
     Example: view 2025-03-26
     ```

   - **Create a Composite Food:**  
     ```
     create-composite <composite_name>
     Enter components in the format <food_name>:<servings> separated by spaces.
     Example:
     > create-composite sandwich  
     > apple:1 peanut_butter:1
     ```

   - **Log Food for a Specific Date:**  
     ```
     log <date> <food_name> <servings>
     Example: log 2025-03-27 sandwich 2
     ```

   - **Delete a Food Entry:**  
     ```
     delete <date> <food_name>
     Example: delete 2025-03-27 sandwich
     ```

   - **Undo the Last Action:**  
     ```
     undo
     ```

   - **View Calorie Targets:**  
     ```
     calorie-target
     ```

   - **Update User Profile:**  
     ```
     update-profile <gender> <age> <height> <weight> <activity>
     Example: update-profile Male 25 180 75 Moderate
     ```

   - **Set Calorie Calculation Method:**  
     ```
     set-calorie-method <harris|mifflin>
     ```

   - **Save Logs:**  
     ```
     save
     ```

   - **Exit the Program:**  
     ```
     exit
     ```

---

## **File Structure**  
```plaintext
YADA/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── yada/
│       │           ├── Main.java                # Main class (CLI)
│       │           ├── food/
│       │           │   ├── Food.java            # Abstract class for food
│       │           │   ├── BasicFood.java       # Basic food class
│       │           │   └── CompositeFood.java   # Composite food class
│       │           ├── db/
│       │           │   └── FoodDatabase.java    # Food database handling (load/save)
│       │           ├── log/
│       │           │   ├── LogEntry.java        # Log entry class
│       │           │   └── DietLogManager.java  # Log manager with undo functionality
│       │           └── profile/
│       │               ├── UserProfile.java     # User profile (age, weight, etc.)
│       │               └── CalorieCalculator.java # Calculates target calorie intake
│       └── resources/
│           └── logs.txt                         # Saved logs and food entries
│
├── pom.xml                                      # Maven configuration file
├── README.md                                    # Project documentation (this file)
└── logs.txt                                     # Food log persistence file
```

---

## **To-Do / Future Improvements**  
- Add more advanced calorie tracking features.  
- Improve the undo functionality to handle multi-step operations.  
- Refactor the CLI for a cleaner, modular design.

---

## **Contributors**  
- Likhit Bhogadi 2023101065
- Eshwar Sriramoju 2023101111

---
