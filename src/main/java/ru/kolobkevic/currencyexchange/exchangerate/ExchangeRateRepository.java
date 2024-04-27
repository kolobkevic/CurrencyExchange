package ru.kolobkevic.currencyexchange.exchangerate;

import lombok.RequiredArgsConstructor;
import ru.kolobkevic.currencyexchange.common.ResultSetMapper;
import ru.kolobkevic.currencyexchange.common.repositories.CrudRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ExchangeRateRepository implements CrudRepository<ExchangeRate> {

    private final Connection connection;

    @Override
    public Optional<ExchangeRate> findById(Integer id) {
        String query = "SELECT id, basecurrencyid, targetcurrencyid, rate FROM ExchangeRates WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(ResultSetMapper.toExchangeRate(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<ExchangeRate> findAll() {
        String query = "SELECT id, basecurrencyid, targetcurrencyid, rate FROM ExchangeRates";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(ResultSetMapper.toExchangeRate(resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        String query = "INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate) VALUES(?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());
            preparedStatement.execute();
            return findByCurrenciesCodes(exchangeRate.getBaseCurrencyId(),
                    exchangeRate.getTargetCurrencyId()).orElseThrow(RuntimeException::new);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteById(Integer id) {
        String query = "DELETE FROM ExchangeRates WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<ExchangeRate> findByExchangeCodes (String baseCode, String targetCode) {
        String query = "SELECT rates.id, rates.basecurrencyid, rates.targetcurrencyid, rates.rate " +
                "FROM ExchangeRates rates " +
                "JOIN Currencies base ON rates.BaseCurrencyId = base.ID " +
                "JOIN Currencies target ON rates.TargetCurrencyId = target.ID " +
                "WHERE base.Code = ? AND target.Code = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, baseCode);
            preparedStatement.setString(2, targetCode);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(ResultSetMapper.toExchangeRate(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public ExchangeRate update(ExchangeRate exchangeRate){
        String query = "UPDATE ExchangeRates SET Rate =? WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBigDecimal(1, exchangeRate.getRate());
            preparedStatement.setInt(2, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(3, exchangeRate.getTargetCurrencyId());
            preparedStatement.execute();
            return findByCurrenciesCodes(exchangeRate.getBaseCurrencyId(),
                    exchangeRate.getTargetCurrencyId()).orElseThrow(RuntimeException::new);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<ExchangeRate> findByCurrenciesCodes(Integer baseCurrencyId, Integer targetCurrencyId) {
        String query = "SELECT rates.id, rates.basecurrencyid, rates.targetcurrencyid, rates.rate " +
                "FROM ExchangeRates rates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, baseCurrencyId);
            preparedStatement.setInt(2, targetCurrencyId);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(ResultSetMapper.toExchangeRate(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
