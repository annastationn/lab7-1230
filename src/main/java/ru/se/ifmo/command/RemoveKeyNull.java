package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;

public class RemoveKeyNull implements Command {
    private final CommandHandler commandHandler;
    public RemoveKeyNull (CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("remove_key", this);
    }
    @Override
    public void execute(String arguments) {
         commandHandler.removeKey(arguments);
    }
}
