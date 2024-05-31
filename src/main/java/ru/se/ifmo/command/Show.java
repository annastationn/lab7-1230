package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;

public class Show implements Command {
    private final CommandHandler commandHandler;
    public Show (CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("show", this);
    }
    @Override
    public void execute(String arguments) {
        commandHandler.show(arguments);
    }
}
