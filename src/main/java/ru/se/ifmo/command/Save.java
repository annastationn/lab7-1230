package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;

public class Save implements Command {
    private final CommandHandler commandHandler;
    public Save (CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("save", this);
    }
    @Override
    public void execute(String arguments) {
        commandHandler.save(arguments);
    }
}
