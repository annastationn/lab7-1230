package ru.se.ifmo.commands;

import ru.se.ifmo.modules.CommandHandler;
import ru.se.ifmo.modules.ConsoleApp;

public class RemoveGreaterKeyNull implements Command {
    private final CommandHandler commandHandler;
    public RemoveGreaterKeyNull(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("remove_greater_key", this);
    }
    @Override
    public void execute(String arguments) {
         commandHandler.removeGreaterKey(arguments);
    }
}
