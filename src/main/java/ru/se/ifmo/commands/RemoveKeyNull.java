package ru.se.ifmo.commands;

import ru.se.ifmo.modules.CommandHandler;
import ru.se.ifmo.modules.ConsoleApp;

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
