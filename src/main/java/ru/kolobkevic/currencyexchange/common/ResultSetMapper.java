package ru.kolobkevic.currencyexchange.common;

import ru.kolobkevic.currencyexchange.common.exceptions.MapperException;
import ru.kolobkevic.currencyexchange.currency.Currency;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ResultSetMapper {

    private ResultSetMapper() {
    }

    public static Currency toCurrency(ResultSet resultSet) {
        try {
            return new Currency(
                    resultSet.getInt("id"),
                    resultSet.getString("code"),
                    resultSet.getString("fullName"),
                    resultSet.getString("sign")
            );
        } catch (SQLException e) {
            throw new MapperException("Unable to map resultSet to Currency", e);
        }
    }

    public static ExchangeRate toExchangeRate(ResultSet resultSet) {
        try {
            return new ExchangeRate(
                    resultSet.getInt("id"),
                    resultSet.getInt("baseCurrencyId"),
                    resultSet.getInt("targetCurrencyId"),
                    resultSet.getBigDecimal("rate")
            );
        } catch (SQLException e) {
            throw new MapperException("Unable to map resultSet to ExchangeRate", e);
        }
    }
}
