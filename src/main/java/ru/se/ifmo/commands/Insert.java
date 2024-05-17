package ru.se.ifmo.commands;

import ru.se.ifmo.modules.CommandHandler;
import ru.se.ifmo.modules.ConsoleApp;

public class Insert implements Command {
    private final CommandHandler commandHandler;
    public Insert(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("insert", this);
    }
    @Override
    public void execute(String arguments) {
        commandHandler.insert(arguments);
    }
}
