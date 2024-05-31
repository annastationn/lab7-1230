package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;

public class RemoveLower implements Command {
    private final CommandHandler commandHandler;
    public RemoveLower (CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("remove_lower", this);
    }
    @Override
    public void execute(String arguments) {
         commandHandler.removeLower(arguments);
    }
}
