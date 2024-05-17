package ru.se.ifmo.modules;

import ru.se.ifmo.exceptions.ScriptRecursionException;
import ru.se.ifmo.models.OrganizationType;
import ru.se.ifmo.models.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.*;

import static java.lang.Long.parseLong;

public class CommandHandler {
    private CollectionService collectionService;
    private static LinkedList<String> commandHistory = new LinkedList<>();
    //понять какой тип данных в линкед хеш мап внизу
    private static Map<String, Boolean> scriptsName = new LinkedHashMap<String, Boolean>();

    public CommandHandler(Connection connection, String dbms, User user) {
        this.collectionService = new CollectionService(connection, dbms, user);
    }

    public void help(String arguments) {
        if (!arguments.isBlank()) {
            System.out.println("Неверные аргументы команды");//illegal args exception
        } else {
            System.out.println(
                    """
                            Список доступных команд: 
                                   help - вывести справку по доступным командам
                                   info - вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
                                   show - вывести в стандартный поток вывода все элементы коллекции в строковом представлении
                                   insert null {element} - добавить новый элемент с заданным ключом
                                   update id {element} - обновить значение элемента коллекции, id которого равен заданному
                                   remove_key null - удалить элемент из коллекции по его ключу
                                   clear - очистить коллекцию
                                   save - сохранить коллекцию в файл
                                   execute_script file_name - считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
                                   exit - завершить программу (без сохранения в файл)
                                   remove_lower {element} - удалить из коллекции все элементы, меньшие, чем заданный
                                   history - вывести последние 12 команд (без их аргументов)
                                   remove_greater_key null - удалить из коллекции все элементы, ключ которых превышает заданный
                                   min_by_name - вывести любой объект из коллекции, значение поля name которого является минимальным
                                   filter_greater_than_type type - вывести элементы, значение поля type которых больше заданного
                                   print_unique_annual_turnover - вывести уникальные значения поля annualTurnover всех элементов в коллекции       
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
                // Проверяем, что все промежуточные номера уже присутствуют в коллекции
                // Если все промежуточные номера уже существуют, добавляем объект
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

    public void executeScript (String path) {
        if (path.isBlank()) {
            System.out.println("Неверные аргументы команды"); //illegal args exception
        } else {
            try {
                Path pathToScript = Paths.get(path);
                PromptScanner.setUserScanner(new Scanner(pathToScript));
                Scanner scriptScanner = PromptScanner.getUserScanner();

                Path scriptFile = pathToScript.getFileName();
                if (!scriptScanner.hasNext()) {throw  new NoSuchElementException();}
                scriptsName.put(scriptFile.toString(), true);
                do {
                    var command = "";
                    var arguments = "";
                    String [] input = (scriptScanner.nextLine() + " ").trim().split(" ", 2);
                    if (input.length == 2){
                        arguments = input[1].trim();
                    }
                    command = input[0].trim();
                    while (scriptScanner.hasNextLine() && command.isEmpty()) {
                        input = (scriptScanner.nextLine() + " ").trim().split(" ", 2);
                        if (input.length == 2) {
                            arguments = input[1].trim();
                        }
                        command = input[0].trim();
                    }
                    if (ConsoleApp.commandList.containsKey(command)) {
                        if (command.equalsIgnoreCase("executeScript")) {
                            Path scriptNameFromArgument = Paths.get(arguments).getFileName();
                            if (scriptsName.containsKey(scriptNameFromArgument)) {
                                throw new ScriptRecursionException("Один и тот же скрипт не может выполняться рекурсивно");
                            }
                            ConsoleApp.commandList.get("executeScript").execute(arguments);
                        }
                        else {                        ConsoleApp.commandList.get(command).execute(arguments);
                            System.out.println("Команда" + command + "выполнена успешно");
                        }
                    } else {
                        System.out.println("Неизвестная команда. Попроюуй ввести ещё раз");
                    }
                } while (scriptScanner.hasNextLine());
                scriptsName.remove(scriptFile);
                PromptScanner.setUserScanner(new Scanner(System.in));
                System.out.println("Скрипт" + scriptFile + "успешно выполнен");

            } catch (FileNotFoundException e) {
                System.out.println("Файл" + path + "не найден");
            } catch (NoSuchElementException e) {
                System.out.println("Файл" + path + "пуст");
            } catch (IllegalStateException e) {
                System.out.println("Непредвиденная ошибка");
            } catch (SecurityException e){
                System.out.println("Недостаточно прав для чтения файла " + path);
            } catch (ScriptRecursionException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("Ошибка ввода/вывода");
            } catch (InvalidPathException e){
                System.out.println("Проверьте путь к файлу. В нём не должно быть лишних символов");
            }
        }
    }

    public void exit(String arguments) {
        if (!arguments.isBlank()) {
            System.out.println("Неверные аргументы команды");// illegal args exception
        } else {
            System.out.println(
                    """
                        Если вы выйдете, изменения не сохранятся. Вы уверены, что хотите выйти?
                        y = "Да"        любая другая клавиша = "Нет"  
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