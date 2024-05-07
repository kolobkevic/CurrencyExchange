package ru.kolobkevic.currencyexchange.currency;

import org.sqlite.SQLiteErrorCode;
import ru.kolobkevic.currencyexchange.common.ResultSetMapper;
import ru.kolobkevic.currencyexchange.common.exceptions.DatabaseException;
import ru.kolobkevic.currencyexchange.common.exceptions.ObjectAlreadyExistsException;
import ru.kolobkevic.currencyexchange.common.repositories.CrudRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyRepository implements CrudRepository<Currency> {
    private static final String FIND_BY_ID_SQL = "SELECT id, code, full_name, sign FROM currency WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT id, code, full_name, sign FROM currency";
    private static final String FIND_BY_CODE_SQL = "SELECT id, code, full_name, sign FROM currency WHERE Code = ?";
    private static final String SAVE_SQL = "INSERT INTO currency(code, full_name, sign) VALUES(?, ?, ?) " +
            "RETURNING currency.id";
    private static final String DELETE_SQL = "DELETE FROM currency WHERE id = ?";

    private final Connection connection;

    public CurrencyRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Currency> findById(Integer id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(ResultSetMapper.toCurrency(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Unable to get currency with id = " + id, e);
        }
    }

    @Override
    public List<Currency> findAll() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(ResultSetMapper.toCurrency(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new DatabaseException("Unable to get currencies", e);
        }
    }

    @Override
    public Currency save(Currency currency) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.execute();
            currency.setId(preparedStatement.getGeneratedKeys().getInt(1));
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code) {
                throw new ObjectAlreadyExistsException(e.getMessage());
            }
            throw new DatabaseException("Unable to save currency", e);
        }
        return currency;
    }


    @Override
    public void deleteById(Integer id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DatabaseException("Unable to delete currency with id = " + id, e);
        }
    }

    public Optional<Currency> findByCode(String code) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE_SQL)) {
            preparedStatement.setString(1, code);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(ResultSetMapper.toCurrency(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Unable to get currency with code = " + code, e);
        }
    }
}
