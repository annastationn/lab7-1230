package ru.se.ifmo;


import ru.se.ifmo.models.User;
import ru.se.ifmo.modules.CommandHandler;
import ru.se.ifmo.modules.ConsoleApp;
import ru.se.ifmo.modules.PromptScanner;
import ru.se.ifmo.commands.*;
import ru.se.ifmo.tables.UsersTable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();
        Connection connection;
        String dbms;
        try {
            properties.load(Main.class.getResourceAsStream("application.properties"));
            String jdbcURL = "jdbc:" + properties.getProperty("db.driver") + "://"
                             + properties.getProperty("db.host") + ":" + properties.getProperty("db.port")
                             + "/" + properties.getProperty("db.name");

            connection = DriverManager.getConnection(jdbcURL, properties.getProperty("db.user"),
                    properties.getProperty("db.password"));
            dbms = properties.getProperty("db.driver");
        } catch (IOException e) {
            System.out.println("Ошибка чтения конфигурации приложения.");
            return;
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
            return;
        }
        PromptScanner.setUserScanner(new Scanner(System.in));
        var scanner = PromptScanner.getUserScanner();

        User user = auth(connection, dbms, scanner);
        ConsoleApp consoleApp = createConsoleApp(connection, dbms, user);

        System.out.println("===================================");
        consoleApp.help("");
        System.out.print("> ");

        while (true) {
            try {
                while (scanner.hasNext()) {
                    var command = "";
                    var arguments = "";
                    String[] input = (scanner.nextLine() + " ").trim().split(" ", 2);
                    if (input.length == 2) {
                        arguments = input[1].trim();
                    }
                    command = input[0].trim();

                    if (ConsoleApp.commandList.containsKey(command)) {
                        ConsoleApp.commandList.get(command).execute(arguments);
                        CommandHandler.addCommand(command);
                    } else if (command.isEmpty()){
                        continue;
                    }
                    else {
                        System.out.println("Unknown command. Keep trying...");
                    }
                    System.out.print("> ");
                }
            } catch (NoSuchElementException e) {
                System.out.println("Exit...");
                System.out.println(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                System.exit(1);
            }

        }
    }

    private static User auth(Connection connection, String dbms, Scanner scanner) {
        UsersTable usersTable = new UsersTable(connection, dbms);
        User user;
        System.out.println("Для того чтобы пользоваться приложением необходимо авторизоваться");
        while (true) {
            System.out.println("Выберите способ авторизации:");
            System.out.println("1. Войти в аккаунт");
            System.out.println("2. Зарегистрировать новый аккаунт");
            System.out.print("Введите номер варианта чтобы продолжить: ");
            int number;
            try {
                number = Integer.parseInt(scanner.next());
            } catch (Exception e) {
                System.out.println("Введите число!");
                continue;
            }
            if (number == 1) {
                System.out.print("Введите ваш юзернейм: ");
                String username = scanner.next();
                String password = createPassword(scanner);
                if (password == null) {
                    System.out.println("Пароль не может быть пустым");
                    continue;
                }
                user = usersTable.getUser(username, password);
                if (user == null) {
                    System.out.println("Введены неверные данные попробуйте еще раз или зарегистрируйте нового пользователя");
                } else {
                    System.out.printf("Вы успешно авторизовались под юзернеймом - %s. Вам открыт доступ к программе%n", user.getUsername());
                    break;
                }
            } else if (number == 2) {
                System.out.print("Введите юзернейм для вашего пользователя: ");
                String username = scanner.next();
                if (username == null || username.isEmpty()) {
                    System.out.println("Юзернейм не может быть пустым");
                    continue;
                }
                if (usersTable.isUnique(username)) {
                    System.out.println("Данный юзернейм уже существует в базе данных, используйте другой юзернейм");
                    continue;
                }
                String password = createPassword(scanner);
                if (password == null) {
                    System.out.println("Пароль не может быть пустым");
                    continue;
                }
                user = new User();
                user.setUsername(username);
                user.setPassword(password);
                usersTable.insert(user);
                if (user.getId() != null && user.getId() != 0) {
                    System.out.printf("Поздравляем! Вы успешно зарегистрировались под юзернеймоим - %s. Вам успешно открыт доступ к программе.%n", user.getUsername());
                    break;
                } else {
                    System.out.println("Произошла ошибка создания пользователя! Попробуйте еще раз");
                }
            } else {
                System.out.println("Введите 1 либо 2 чтобы продолжить");
            }
        }
        return user;
    }

    private static String createPassword(Scanner scanner) {
        String password;
        if (System.console() != null) {
            password = Arrays.toString(System.console().readPassword("Введите ваш пароль: "));
        } else {
            System.out.print("Введите ваш пароль: ");
            password = scanner.next();
        }
        if (password == null || password.isEmpty()) {
            return null;
        }
        return password;
    }

    private static ConsoleApp createConsoleApp(Connection connection, String dbms, User user) {
        CommandHandler commandHandler = new CommandHandler(connection, dbms, user);
        return new ConsoleApp(
                new Help(commandHandler),
                new Info(commandHandler),
                new Show(commandHandler),
                new Insert(commandHandler),
                new UpdateId(commandHandler),
                new RemoveKeyNull(commandHandler),
                new Clear(commandHandler),
                new ExecuteScriptFileName(commandHandler),
                new Exit(commandHandler),
                new RemoveGreaterKeyNull(commandHandler),
                new RemoveLower(commandHandler),
                new History(commandHandler),
                new MinByName(commandHandler),
                new PrintUniqueAnnualTurnover(commandHandler),
                new FilterGreaterThanType(commandHandler)
        );
    }
}