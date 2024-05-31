package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;

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
