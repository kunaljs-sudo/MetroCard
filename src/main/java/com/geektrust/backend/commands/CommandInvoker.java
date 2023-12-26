package com.geektrust.backend.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.geektrust.backend.exceptions.NoSuchCommandException;

public class CommandInvoker {
    private static final Map<String, ICommand> commandMap = new HashMap<>();

    // Register the command into hashmap
    public void register(String commandName, ICommand command) {
        commandMap.put(commandName, command);
    }

    // Get registred Command
    private ICommand get(String commandName) {
        return commandMap.get(commandName);
    }

    public void executeCommand(String commandName, List<String> tokens) {
        ICommand command = get(commandName);
        if (command == null || !commandMap.containsKey(commandName)) {
            throw new NoSuchCommandException(
                    "The command: " + commandName + " is not a valid command");
        }
        command.execute(tokens);
    }
}
