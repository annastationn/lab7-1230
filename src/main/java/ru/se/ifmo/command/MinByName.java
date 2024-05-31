package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;

public class MinByName implements Command {
    private final CommandHandler commandHandler;
    public MinByName (CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("min_by_name", this);
    }
    @Override
    public void execute(String arguments) {
         commandHandler.minByName(arguments);
    }
}
