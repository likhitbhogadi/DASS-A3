HOW TO RUN THE PROGRAM:

1. Clone the repository
   git clone <repository-url>
   cd DASS-A3
   cd yada2

2. Make the script executable
   chmod +x compileAndRun.sh

3. Compile and run the application
   ./compileAndRun.sh

4. To only run the application without recompiling
   chmod +x run.sh
   ./run.sh

HOW TO EXERCISE FEATURES:

1. First-time setup: Enter your gender, height, age, weight, and activity level

2. Main Menu provides access to all functionality:
   - Add foods to your log: 
     Select food items from the database and specify quantity to add to your daily intake. 
     You can select by name or browse the complete list.
   
   - View your daily food log: 
     See all foods consumed for the current day with nutritional information and total calorie count. 
     This provides a summary of your day's nutrition.
   
   - Remove foods from your log: 
     Delete specific food entries from your current day's log if recorded by mistake or to adjust your intake tracking.
   
   - Add new basic foods: 
     Create new food items by entering nutritional information including calories, protein, carbs, and fats. 
     These foods become available in your database.
   
   - Create composite foods from other foods: 
     Combine multiple foods to create recipes or meal entries. 
     Specify ingredients and portions to calculate the composite nutritional value.
   
   - Search food database: 
     Find specific foods by name, nutritional content, or other filters. 
     Results display detailed nutritional information for each matching food.
   
   - Change date to view/edit other days: 
     Switch between different dates to view or modify food logs from past or future days for consistent tracking.
   
   - Update user information: 
     Modify your profile including weight, height, activity level, and other metrics to ensure accurate calorie recommendations.
   
   - Change calorie calculation method: 
     Select different formulas (e.g., Harris-Benedict, Mifflin-St Jeor) to calculate your daily calorie needs based on your preferences.
   
   - Undo previous actions: 
     Revert the last changes made to your food log or settings if you made a mistake or changed your mind.
   
   - Save data: 
     Manually save all your food logs and user information to ensure no data is lost. This is also done automatically when exiting.
   
   - View command history: 
     See a chronological list of all actions you've performed in the current session for reference or troubleshooting.

All data is automatically saved when exiting the application and can also be saved manually.
