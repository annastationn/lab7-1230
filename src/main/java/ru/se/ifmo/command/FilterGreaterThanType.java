package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;

public class FilterGreaterThanType implements Command {
    private final CommandHandler commandHandler;
    public FilterGreaterThanType(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("filter_greater_than_type", this);
    }
    @Override
    public void execute(String arguments) {
        commandHandler.filterGreaterThanType(arguments);
    }
}
