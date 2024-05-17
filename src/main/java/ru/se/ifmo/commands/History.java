package ru.se.ifmo.commands;

import ru.se.ifmo.modules.CommandHandler;
import ru.se.ifmo.modules.ConsoleApp;

public class History implements Command {
    private final CommandHandler commandHandler;
    public History (CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("history", this);
    }
    @Override
    public void execute(String arguments) {
         commandHandler.history(arguments);
    }
}
