package ru.kolobkevic.currencyexchange.common;

import java.sql.Connection;

public interface DatabaseService {
    Connection getConnection();
}
