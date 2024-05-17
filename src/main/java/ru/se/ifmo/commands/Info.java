package ru.se.ifmo.commands;

import ru.se.ifmo.modules.CommandHandler;
import ru.se.ifmo.modules.ConsoleApp;

public class Info implements Command {
    private final CommandHandler commandHandler;
    public Info (CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("info", this);
    }
    @Override
    public void execute(String arguments) {
         commandHandler.info(arguments);
    }
}
