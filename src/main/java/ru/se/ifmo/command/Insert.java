package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;

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
