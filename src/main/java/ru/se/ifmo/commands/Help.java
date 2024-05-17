package ru.se.ifmo.commands;

import ru.se.ifmo.modules.CommandHandler;
import ru.se.ifmo.modules.ConsoleApp;

public class Help implements Command {
    private final CommandHandler commandHandler;
    public Help (CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("help", this);
    }
    @Override
    public void execute(String arguments){
         commandHandler.help(arguments);
    }
}
