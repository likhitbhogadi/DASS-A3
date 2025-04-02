package com.calorietracker.command;

public interface Command {
    void execute();
    void undo();
    String getDescription();
}
