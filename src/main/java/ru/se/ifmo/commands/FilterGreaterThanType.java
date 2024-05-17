package ru.se.ifmo.commands;

import ru.se.ifmo.modules.CommandHandler;
import ru.se.ifmo.modules.ConsoleApp;

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
