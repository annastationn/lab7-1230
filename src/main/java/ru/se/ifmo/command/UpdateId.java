package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;

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
