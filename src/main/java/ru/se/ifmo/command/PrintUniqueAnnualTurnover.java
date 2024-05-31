package ru.se.ifmo.command;


import ru.se.ifmo.module.CommandHandler;
import ru.se.ifmo.module.ConsoleApp;

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
