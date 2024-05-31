package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;
public class Exit implements Command {
    private final CommandHandler commandHandler;

    public Exit (CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("exit", this);
    }
    @Override
    public void execute(String arguments) {
         commandHandler.exit(arguments);
    }
}
