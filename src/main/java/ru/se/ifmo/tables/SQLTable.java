package ru.se.ifmo.tables;

import java.sql.Connection;

public abstract class SQLTable<T> {
    protected Connection connection;
    protected String dbms;

    public SQLTable(Connection connection, String dbms) {
        this.connection = connection;
        this.dbms = dbms;
    }

    public abstract void insert(T entity);
}
