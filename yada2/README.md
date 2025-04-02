# Calorie Tracker CLI Application

A command-line Java application for tracking daily calorie intake, managing food databases, and monitoring nutritional goals.

## Features

- User profile management (gender, height, age, weight, activity level)
- Dynamic daily calorie target calculation using multiple methods
- Food database with basic and composite foods
- Daily food log with timestamp tracking
- Search functionality for foods
- Command history with unlimited undo functionality
- Date navigation for historical data viewing
- Persistent storage of food database and logs

## Architecture

### Design Patterns

- **Command Pattern**: Implements undo functionality through command objects that encapsulate actions
- **Composite Pattern**: Used for food items (basic foods vs. composite foods)
- **Strategy Pattern**: Different calorie calculation methods are interchangeable
- **Flyweight Pattern**: Shares food objects to reduce memory usage
- **Factory Method**: Creates different types of food objects

### Key Components

1. **Model Layer**
   - `Food` (abstract): Base class for all food items
   - `BasicFood`: Simple food with calories per serving
   - `CompositeFood`: Complex foods composed of other foods
   - `User`: Stores user information and history
   - `DailyLog`: Represents a day's food log
   - `FoodEntry`: Single food entry with serving size and timestamp

2. **Service Layer**
   - `CalorieCalculator` (interface): Calculates target calories
   - `HarrisBenedictCalculator`: Implementation using Harris-Benedict equation
   - `MifflinStJeorCalculator`: Implementation using Mifflin-St Jeor equation
   - `FoodSource` (interface): Retrieves food information from different sources
   - `FoodSourceManager`: Manages multiple food sources

3. **Data Layer**
   - `FoodDatabase` (interface): Stores and retrieves food information
   - `JsonFoodDatabase`: JSON implementation of the food database
   - `LogManager`: Handles storage and retrieval of daily logs

4. **Command Layer**
   - `Command` (interface): Base for all command objects
   - `AddFoodEntryCommand`: Adds food to daily log
   - `RemoveFoodEntryCommand`: Removes food from daily log
   - `CommandInvoker`: Tracks and executes commands

5. **Controller Layer**
   - `CalorieTrackerController`: Mediates between UI and data/services

6. **UI Layer**
   - `CalorieTrackerCLI`: Command-line interface for the application

## Running the Application

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven (for dependency management)

### Dependencies

- Google Gson (for JSON serialization/deserialization)

### Running Instructions

1. **Clone the repository**
   ```
   git clone <repository-url>
   cd yada2
   ```

2. **Compile the application**
   ```
   mvn clean compile
   ```

3. **Run the application**
   ```
   mvn exec:java -Dexec.mainClass="com.calorietracker.CalorieTrackerApp"
   ```

   Alternatively, you can run the JAR directly:
   ```
   java -jar target/calorie-tracker-1.0-SNAPSHOT.jar
   ```

## User Guide

Upon launching the application:

1. **First-time setup**: Enter your gender, height, age, weight, and activity level
2. **Main Menu** provides access to all functionality:
   - Add foods to your log
   - View your daily food log
   - Remove foods from your log
   - Add new basic foods
   - Create composite foods from other foods
   - Search food database
   - Change date to view/edit other days
   - Update user information
   - Change calorie calculation method
   - Undo previous actions
   - Save data
   - View command history

## Extensibility

The application has been designed for easy extension:

1. **Adding new calorie calculation methods**:
   - Create a new class implementing `CalorieCalculator`
   - Add the calculator to the `calculators` map in `CalorieTrackerController`

2. **Adding new food sources**:
   - Create a new class implementing `FoodSource`
   - Register it with the `FoodSourceManager`

3. **Supporting new food data formats**:
   - Modify the `JsonFoodDatabase` class or
   - Create a new implementation of `FoodDatabase`

## Data Storage

- Basic foods are stored in `data/basic_foods.json`
- Composite foods are stored in `data/composite_foods.json`
- Daily logs are stored in `data/log.json`
- All data is automatically saved when exiting the application and can also be saved manually

## Requirements

- JDK 8+
- Gson library
- 50MB free disk space
- 256MB RAM minimum
