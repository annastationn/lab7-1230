package ru.se.ifmo.commands;

import ru.se.ifmo.modules.CommandHandler;
import ru.se.ifmo.modules.ConsoleApp;

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
