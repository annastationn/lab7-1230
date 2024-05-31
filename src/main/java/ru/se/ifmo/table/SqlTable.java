package ru.se.ifmo.table;

import java.sql.Connection;

public abstract class SqlTable {
    private final Connection connection;

    public SqlTable(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
