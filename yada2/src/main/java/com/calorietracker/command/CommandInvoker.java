package com.calorietracker.command;

import java.util.Stack;

public class CommandInvoker {
    private final Stack<Command> undoStack = new Stack<>();
    
    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
    }
    
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }
    
    public void undo() {
        if (canUndo()) {
            Command command = undoStack.pop();
            command.undo();
        }
    }
    
    public Stack<Command> getCommandHistory() {
        return new Stack<Command>() {{
            addAll(undoStack);
        }};
    }
}
