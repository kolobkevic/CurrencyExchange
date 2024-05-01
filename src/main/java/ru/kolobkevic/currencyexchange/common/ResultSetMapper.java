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
                    resultSet.getString("full_name"),
                    resultSet.getString("sign")
            );
        } catch (SQLException e) {
            throw new MapperException("Unable to map resultSet to Currency", e);
        }
    }

    public static ExchangeRate toExchangeRate(ResultSet resultSet) {
        try {
            return new ExchangeRate(
                    resultSet.getInt("er_id"),
                    new Currency(resultSet.getInt("b_id"), resultSet.getString("b_code"),
                            resultSet.getString("b_full_name"), resultSet.getString("b_sign")),
                    new Currency(resultSet.getInt("t_id"), resultSet.getString("t_code"),
                            resultSet.getString("t_full_name"), resultSet.getString("t_sign")),
                    resultSet.getBigDecimal("er_rate")
            );
        } catch (SQLException e) {
            throw new MapperException("Unable to map resultSet to ExchangeRate", e);
        }
    }
}
