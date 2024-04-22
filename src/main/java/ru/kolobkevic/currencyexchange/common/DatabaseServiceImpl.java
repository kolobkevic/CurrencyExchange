package ru.kolobkevic.currencyexchange.common;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseServiceImpl implements DatabaseService {
    private static final String DB_NAME = "database/currency_exchange.db";
    private static final String DB_TYPE = "jdbc:sqlite:";

    @Override
    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ДРАЙВЕР НЕ НАЙДЕН");
        }
        try {
            Connection connection = DriverManager.getConnection(getUrl());
            connection.setAutoCommit(true);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException();
        }

    }

    private String getUrl() {
        URL url = getClass().getClassLoader().getResource(DB_NAME);
        if (url != null) {
            return DB_TYPE + url.getPath();
        }
        return null;
    }
}
