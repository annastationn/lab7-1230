package ru.se.ifmo.module;

import ru.se.ifmo.model.OrganizationType;
import ru.se.ifmo.model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Long.parseLong;

public class CommandHandler {
    private final CollectionService collectionService;
    private static final LinkedList<String> commandHistory = new LinkedList<>();
    //понять какой тип данных в линкед хеш мап внизу
    private static final Map<String, Boolean> scriptsName = new LinkedHashMap<>();
    private final DataProvider provider;

    public CommandHandler(DataProvider provider, User user) {
        this.provider = provider;
        this.collectionService = new CollectionService(user);
        provider.load(collectionService);
    }

    public void help(String arguments) {
        if (!arguments.isBlank()) {
            System.out.println("Неверные аргументы команды");//illegal args exception
        } else {
            System.out.println(
                    """
                            Список доступных команд:\s
                                   help - вывести справку по доступным командам
                                   info - вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
                                   show - вывести в стандартный поток вывода все элементы коллекции в строковом представлении
                                   insert null {element} - добавить новый элемент с заданным ключом
                                   update id {element} - обновить значение элемента коллекции, id которого равен заданному
                                   remove_key null - удалить элемент из коллекции по его ключу
                                   clear - очистить коллекцию
                                   save - сохранить коллекцию в базу данных
                                   execute_script file_name - считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
                                   exit - завершить программу (без сохранения в файл)
                                   remove_lower {element} - удалить из коллекции все элементы, меньшие, чем заданный
                                   history - вывести последние 12 команд (без их аргументов)
                                   remove_greater_key null - удалить из коллекции все элементы, ключ которых превышает заданный
                                   min_by_name - вывести любой объект из коллекции, значение поля name которого является минимальным
                                   filter_greater_than_type type - вывести элементы, значение поля type которых больше заданного
                                   print_unique_annual_turnover - вывести уникальные значения поля annualTurnover всех элементов в коллекции      \s
                                   """
            );
        }
    }

    public void info(String arguments) {
        if (!arguments.isBlank()) {
            System.out.println("Неверные аргументы команды"); // illegal args exception
        } else {
            collectionService.info();
        }
    }

    public void show(String arguments) {
        if (!arguments.isBlank()) {
            System.out.println("Неверные аргументы команды"); // illegal args exception
        } else {
            collectionService.show();
        }
    }

    public void insert(String arguments) {
        if (arguments.isBlank()) {
            System.out.println("Неверные аргументы команды"); // illegal args exception
        } else {
            try {
                Long key = parseLong(arguments);
                collectionService.addElement(key);
            } catch (NumberFormatException e){
                System.out.println("Wrong key format");
            }
        }}

    public void updateById(String arguments){  //args required
        if (arguments.isBlank()){
            System.out.println("Неверные аргументы команды"); // illegal args exception
        } else {
            try {
                long current_id = parseLong(arguments);
                if (current_id > 0){
                    collectionService.update(current_id);
                } else {
                    System.out.println("id не может быть отрицательным");
                }

            } catch (NumberFormatException e){
                System.out.println("Неверный формат аргументов");
            }
        }
    }

    public void removeKey(String arguments){ //args required
        if (arguments.isBlank()){
            System.out.println("Неверные аргументы команды"); // illegal args exception
        } else {
            try {
                long id = parseLong(arguments);
                if (id > 0){
                    collectionService.removeKey(id);
                } else {
                    System.out.println("id не может быть отрицательным");
                }
            } catch (NumberFormatException e){
                System.out.println("Неверный формат аргументов");
            }
        }
    }

    public void clear(String arguments){
        if (!arguments.isBlank()){
            System.out.println("Неверные аргументы команды"); // illegal args exception
        } else {
            collectionService.clear();
        }
    }

    public void save(String arguments){
        if (!arguments.isBlank()){
            System.out.println("Неверные аргументы команды"); // illegal args exception
        } else {
            provider.save(collectionService);
        }
    }

    public void executeScript (String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.printf("Данный файл (path: %s) не существует или не был найден%n", path);
            return;
        }
        if (!file.canRead()) {
            System.out.printf("Данный файл(Путь: %s) не может быть прочитан %n", path);
            return;
        }
        ArrayList<String> commands = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.lines().filter(Objects::nonNull).filter((line) -> !line.isEmpty()).forEach(commands::add);
            for (String cmd : commands) {
                if (cmd.contains("execute_script")) {
                    System.out.printf("Файл скрипта не может рекурсивно содержать комманду %s%n", "execute_script");
                    return;
                }
            }
        } catch (IOException e) {
            System.out.printf("Ошибка чтения файла (Путь: %s)%n", path);
            return;
        }
        for (String cmd : commands) {
            String command = cmd.split(" ")[0];
            ConsoleApp.commandList.get(command).execute(cmd.substring(command.length()).replaceAll("\\s", ""));
        }
    }

    public void exit(String arguments) {
        if (!arguments.isBlank()) {
            System.out.println("Неверные аргументы команды");// illegal args exception
        } else {
            System.out.println(
                    """
                        Если вы выйдете, изменения не сохранятся. Вы уверены, что хотите выйти?
                        y = "Да"        любая другая клавиша = "Нет" \s
                            """);
            Scanner scanner = new Scanner (System.in);
            var answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("y")){
                System.exit(0);
            }
        }
    }

    public void removeLower(String arguments){ //args required
        if (!arguments.isBlank()){
            System.out.println("Неверные аргументы команды"); // illegal args exception
        } else {
            collectionService.removeLower();
        }
    }

    public void history(String arguments){
        if (!arguments.isBlank()){
            System.out.println("Неверные аргументы команды"); // illegal args exception
        } else {
            System.out.println("Последние 7 команд, введённые пользователем: ");
            for (String command : commandHistory) {
                System.out.println(command);
            }
        }
    }

    public void removeGreaterKey(String arguments){
        if (arguments.isBlank()) {
            System.out.println("Неверные аргументы команды");
        }
        else {
            try {
                long startId = parseLong(arguments);
                if (startId > 0) {
                    collectionService.removeGreaterKey(startId);
                } else {
                    System.out.println("id не может быть отрицательным");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат аргументов");
            }
        }

    }

    public void minByName(String arguments) {
        if (!arguments.isBlank()) {
            System.out.println("Неверные аргументы команды");
        }
        else{
            collectionService.minByName();
        }
    }

    public void filterGreaterThanType(String arguments){
        if (arguments.isBlank()){
            System.out.println("Неверные аргументы команды");
        }
        else{
            collectionService.filterGreaterThanType(OrganizationType.valueOf(arguments.toUpperCase()));
        }
    }

    public void printUniqueAnnualTurnover(String arguments){
        if (!arguments.isBlank()){
            System.out.println("Неверные аргументы команды");
        }
        else{
            collectionService.printUniqueAnnualTurnover();
        }
    }

    public static void addCommand(String command){
        if (commandHistory.size() == 7){
            commandHistory.removeFirst();
        }
        commandHistory.addLast(command);
    }
}