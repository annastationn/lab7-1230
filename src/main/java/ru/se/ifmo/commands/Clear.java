package ru.se.ifmo.commands;


import ru.se.ifmo.modules.CommandHandler;
import ru.se.ifmo.modules.ConsoleApp;

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
