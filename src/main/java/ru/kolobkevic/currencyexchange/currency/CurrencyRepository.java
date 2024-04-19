package ru.kolobkevic.currencyexchange.currency;

import lombok.RequiredArgsConstructor;
import ru.kolobkevic.currencyexchange.common.repositories.CrudRepository;
import ru.kolobkevic.currencyexchange.common.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CurrencyRepository implements CrudRepository<Currency> {

    private final Connection connection;

    @Override
    public Optional<Currency> findById(Integer id) {
        String query = "SELECT * FROM Currencies WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(ResultSetMapper.toCurrency(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Currency> findAll() {
        String query = "SELECT * FROM Currencies";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(ResultSetMapper.toCurrency(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Currency save(Currency currency) {
        String query = "INSERT INTO Currencies(Code, FullName, Sign) VALUES(?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public void deleteById(Integer id) {
        String query = "DELETE FROM Currencies WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<Currency> findByCode(String code) {
        String query = "SELECT * FROM Currencies WHERE Code = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(ResultSetMapper.toCurrency(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
