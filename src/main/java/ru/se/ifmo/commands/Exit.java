package ru.se.ifmo.commands;

import ru.se.ifmo.modules.CommandHandler;
import ru.se.ifmo.modules.ConsoleApp;

import java.util.LinkedHashMap;
public class Exit implements Command {
    private LinkedHashMap<String, Command> commandMap;
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
