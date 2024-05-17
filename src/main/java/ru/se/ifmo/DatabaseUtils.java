package ru.se.ifmo;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtils {
    public static void printSQLException(SQLException sqlException) {
        sqlException.printStackTrace();
        for (Throwable e : sqlException) {
            if (e instanceof SQLException) {
                System.out.println("Произшла ошибка в SQL.");
                System.out.println("SQLState: " + ((SQLException)e).getSQLState());
                System.out.println("Код ошибки: " + ((SQLException)e).getErrorCode());
                System.err.println("Сообщение: " + e.getMessage());
            }
        }
    }

    public static void executeStatement(Connection connection, String dbms, String query) {
        if (dbms.equals("postgresql")) {
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                printSQLException(e);
            }
        } else {
            System.out.println("Ваша СУБД не поддерживается!");
        }
    }
}
