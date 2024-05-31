package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;

public class Clear implements Command {
    private final CommandHandler commandHandler;
    public Clear (CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("clear", this);
    }

    @Override
    public void execute(String arguments) {
         commandHandler.clear(arguments);
    }
}
