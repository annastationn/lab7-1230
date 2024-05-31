package ru.se.ifmo.table;

import ru.se.ifmo.DatabaseUtils;
import ru.se.ifmo.model.User;

import java.sql.*;

public class UsersTable extends SqlTable {
    public UsersTable(Connection connection) {
        super(connection);
    }

    public void insertUser(User user) {
        String sql = "insert into users (username, password) values (?, ?)";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.execute();
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }


    public boolean isUnique(String username) {
        String sql = "select username from users where username = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
        return false;
    }


    public User getUser(String username) {
        String sql = "select * from users where username = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String password = resultSet.getString("password");
                return new User(username, password);
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
        return null;
    }
}