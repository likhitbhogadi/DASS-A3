#!/bin/bash

mvn clean compile
mvn exec:java -Dexec.mainClass="com.calorietracker.CalorieTrackerApp"