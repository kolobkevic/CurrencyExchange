package ru.kolobkevic.currencyexchange.common.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import ru.kolobkevic.currencyexchange.common.exceptions.DatabaseException;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;


@Slf4j
public class DatabaseServiceImpl implements DatabaseService {
    private static final String DB_NAME = "database/currency_exchange.db";
    private static final String DB_TYPE = "jdbc:sqlite:";
    private static final String DB_DRIVER = "org.sqlite.JDBC";


    private String getUrl() {
        URL url = getClass().getClassLoader().getResource(DB_NAME);
        if (url != null) {
            log.info("Get url {}", DB_TYPE + url.getPath());
            return DB_TYPE + url.getPath();
        }
        return null;
    }

    @Override
    public Connection getConnection() {
        try (HikariDataSource ds = new HikariDataSource(getConfig())) {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new DatabaseException("Can't get connection", e);
        }
    }

    private HikariConfig getConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(DB_DRIVER);
        config.setJdbcUrl(getUrl());
        config.setAutoCommit(true);
        return config;
    }
}
