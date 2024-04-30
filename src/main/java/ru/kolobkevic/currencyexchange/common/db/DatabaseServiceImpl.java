package ru.kolobkevic.currencyexchange.common.db;

import lombok.extern.slf4j.Slf4j;
import ru.kolobkevic.currencyexchange.common.exceptions.DatabaseException;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class DatabaseServiceImpl implements DatabaseService {
    private static final String DB_NAME = "database/currency_exchange.db";
    private static final String DB_TYPE = "jdbc:sqlite:";
    private static final String DB_DRIVER = "org.sqlite.JDBC";

    @Override
    public Connection getConnection() {
        try {
            log.info("Get driver {}", DB_DRIVER);
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Driver " + DB_DRIVER + " not found", e);
        }
        try {
            Connection connection = DriverManager.getConnection(getUrl());
            connection.setAutoCommit(true);
            return connection;
        } catch (SQLException e) {
            throw new DatabaseException("Unable to connect to database", e);
        }

    }

    private String getUrl() {
        URL url = getClass().getClassLoader().getResource(DB_NAME);
        if (url != null) {
            log.info("Get url {}", DB_TYPE + url.getPath());
            return DB_TYPE + url.getPath();
        }
        return null;
    }
}
