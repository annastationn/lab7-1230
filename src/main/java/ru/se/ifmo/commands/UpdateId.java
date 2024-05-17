package ru.se.ifmo.commands;

import ru.se.ifmo.modules.CommandHandler;
import ru.se.ifmo.modules.ConsoleApp;

public class UpdateId implements Command {
    private final CommandHandler commandHandler;
    public UpdateId (CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("update", this);
    }
    @Override
    public void execute(String arguments) {
         commandHandler.updateById(arguments);
    }
}
