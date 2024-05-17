package ru.se.ifmo.commands;

import ru.se.ifmo.modules.CommandHandler;
import ru.se.ifmo.modules.ConsoleApp;

public class PrintUniqueAnnualTurnover implements Command {
    private final CommandHandler commandHandler;
    public PrintUniqueAnnualTurnover (CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        ConsoleApp.commandList.put("print_unique_annual_turnover", this);
    }
    @Override
    public void execute(String arguments) {
        commandHandler.printUniqueAnnualTurnover(arguments);
    }
}
