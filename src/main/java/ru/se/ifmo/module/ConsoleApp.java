package ru.se.ifmo.module;

import ru.se.ifmo.command.Command;

import java.util.LinkedHashMap;
public class ConsoleApp {

    String filename;
    //хэшмапа команд. Ключ - имя команды; Значение - класс-оболочка команды
    public static LinkedHashMap<String, Command> commandList = new LinkedHashMap<>();
    private final Command clear;
    private final Command executeScript;
    private final Command exit;
    private final Command filterGreaterThanType;
    private final Command help;
    private final Command history;
    private final Command info;
    private final Command insert;
    private final Command minByName;
    private final Command printUniqueAnnualTurnover;
    private final Command removeGreaterKeyNull;
    private final Command removeKeyNull;
    private final Command removeLower;
    private final Command save;
    private final Command show;
    private final Command update;

    public ConsoleApp(Command... commands) {
        this.help = commands[0];
        this.info = commands[1];
        this.show = commands[2];
        this.insert = commands[3];
        this.update = commands[4];
        this.removeKeyNull = commands[5];
        this.clear = commands[6];
        this.save = commands[7];
        this.executeScript = commands[8];
        this.exit = commands[9];
        this.removeGreaterKeyNull = commands[10];
        this.removeLower = commands[11];
        this.history = commands[12];
        this.minByName = commands[13];
        this.printUniqueAnnualTurnover = commands[14];
        this.filterGreaterThanType = commands[15];
    }

    public void help(String arguments){
        help.execute(arguments);
    }

    public void info(String arguments){
        info.execute(arguments);
    }

    public void show(String arguments){
        show.execute(arguments);
    }

    public void insert(String arguments){
        insert.execute(arguments);
    }

    public void update(String arguments){
        update.execute(arguments);
    }

    public void removeKeyNull(String arguments){
        removeKeyNull.execute(arguments);
    }

    public void clear(String arguments){
        clear.execute(arguments);
    }

    public void save(String arguments){
        save.execute(arguments);
    }

    public void executeScript(String arguments){
        executeScript.execute(arguments);
    }

    public void exit(String arguments){
        exit.execute(arguments);
    }

    public void removeGreaterKeyNull(String arguments){
        removeGreaterKeyNull.execute(arguments);
    }

    public void removeLower(String arguments){
        removeLower.execute(arguments);
    }

    public void history(String arguments){
        history.execute(arguments);
    }

    public void minByName(String arguments){
        minByName.execute(arguments);
    }

    public void printUniqueAnnualTurnover(String arguments){
        printUniqueAnnualTurnover.execute(arguments);
    }

    public void filterGreaterThanType(String arguments){
        filterGreaterThanType.execute(arguments);
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}