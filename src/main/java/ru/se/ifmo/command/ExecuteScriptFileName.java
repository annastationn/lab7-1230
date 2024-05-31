package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;

public class ExecuteScriptFileName implements Command {
    private final CommandHandler commandHandler;
    public ExecuteScriptFileName (CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("execute_script", this);
    }
    @Override
    public void execute(String arguments) {
        commandHandler.executeScript(arguments);
    }
}
