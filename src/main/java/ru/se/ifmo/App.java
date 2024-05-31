package ru.se.ifmo;

import ru.se.ifmo.command.*;
import ru.se.ifmo.model.User;
import ru.se.ifmo.module.*;
import ru.se.ifmo.table.OrganizationsTable;
import ru.se.ifmo.table.UsersTable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class App {
    public static void main(String[] args) {

        PromptScanner.setUserScanner(new Scanner(System.in));
        var scanner = PromptScanner.getUserScanner();
        Locale.setDefault(Locale.US);
        Properties properties = new Properties();
        Connection connection;
        try {
            properties.load(App.class.getResourceAsStream("application.properties"));
            String jdbcURL = "jdbc:" + properties.getProperty("db.driver") + "://"
                             + properties.getProperty("db.host") + ":" + properties.getProperty("db.port")
                             + "/" + properties.getProperty("db.name");

            connection = DriverManager.getConnection(jdbcURL, properties.getProperty("db.user"),
                    properties.getProperty("db.password"));
        } catch (IOException e) {
            System.out.println("Ошибка чтения конфигурации приложения.");
            return;
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
            return;
        }
        DataProvider dataProvider = new SqlProvider(new OrganizationsTable(connection, new UsersTable(connection)));
        User user = new AuthenticationController(new UsersTable(connection)).auth();
        ConsoleApp consoleApp = createConsoleApp(dataProvider, user);

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

    private static ConsoleApp createConsoleApp(DataProvider dataProvider, User user) {
        CommandHandler commandHandler = new CommandHandler(dataProvider, user);
        return new ConsoleApp(
                new Help(commandHandler),
                new Info(commandHandler),
                new Show(commandHandler),
                new Insert(commandHandler),
                new UpdateId(commandHandler),
                new RemoveKeyNull(commandHandler),
                new Clear(commandHandler),
                new Save(commandHandler),
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