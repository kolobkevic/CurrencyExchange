package ru.kolobkevic.currencyexchange.common.db;

import java.sql.Connection;

public interface DatabaseService {
    Connection getConnection();
}
