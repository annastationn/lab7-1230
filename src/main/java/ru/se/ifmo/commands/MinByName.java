package ru.se.ifmo.commands;

import ru.se.ifmo.modules.CommandHandler;
import ru.se.ifmo.modules.ConsoleApp;

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
