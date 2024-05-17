package ru.se.ifmo.tables;


import ru.se.ifmo.DatabaseUtils;
import ru.se.ifmo.MD2Encoder;
import ru.se.ifmo.models.Coordinates;
import ru.se.ifmo.models.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsersTable extends SQLTable<User> {

    public UsersTable(Connection connArg, String dbms) {
        super(connArg, dbms);
    }

    @Override
    public void insert(User entity) {
        String insertQuery = "INSERT INTO \"Users\" (username, password) VALUES ('%s', '%s') RETURNING id;".formatted(entity.getUsername(), MD2Encoder.encode(entity.getPassword()));
        try (Statement stmt = connection.createStatement()) {
            System.out.println(insertQuery);
            ResultSet rs = stmt.executeQuery(insertQuery);
            while(rs.next()) {
                entity.setId((long) rs.getInt("id"));
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }

    public boolean isUnique(String username) {
        String selectQuery = "SELECT username FROM \"Users\" WHERE username = '%s'".formatted(username);

        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(selectQuery);
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
        return false;
    }

    public User getUser(Integer id) {
        String selectQuery = "SELECT * FROM \"Users\" WHERE id = %d".formatted(id);
        try (Statement st = connection.createStatement()){
            ResultSet rs = st.executeQuery(selectQuery);
            while (rs.next()) {
                User user = new User();
                user.setId((long) id);
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
        return null;
    }

        public User getUser(String username, String password) {
        String selectQuery = "SELECT * FROM \"Users\" WHERE username = '%s';".formatted(username);
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(selectQuery);
            while (rs.next()) {
                int id = rs.getInt("id");
                User user = new User();
                user.setId((long) id);
                user.setUsername(username);
                if (MD2Encoder.verify(password, rs.getString("password"))) {
                    user.setPassword(password);
                } else {
                    return null;
                }
                return user;
            }
        } catch (SQLException ignored) {
            DatabaseUtils.printSQLException(ignored);
        }
        return null;
    }

}
