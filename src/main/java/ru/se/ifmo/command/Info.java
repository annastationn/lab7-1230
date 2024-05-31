package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;

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
