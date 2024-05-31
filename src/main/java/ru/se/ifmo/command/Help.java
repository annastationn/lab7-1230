package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;

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
