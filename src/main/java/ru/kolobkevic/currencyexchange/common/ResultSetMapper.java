package ru.kolobkevic.currencyexchange.common;

import ru.kolobkevic.currencyexchange.currency.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetMapper {

    private ResultSetMapper() {}

    public static Currency toCurrency(ResultSet resultSet) throws SQLException {
        try {
            return new Currency(
                    resultSet.getInt("id"),
                    resultSet.getString("code"),
                    resultSet.getString("fullName"),
                    resultSet.getString("sign")
            );
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
