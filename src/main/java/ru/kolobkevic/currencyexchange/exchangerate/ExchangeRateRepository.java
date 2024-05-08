package ru.kolobkevic.currencyexchange.exchangerate;

import lombok.RequiredArgsConstructor;
import org.sqlite.SQLiteErrorCode;
import ru.kolobkevic.currencyexchange.common.ResultSetMapper;
import ru.kolobkevic.currencyexchange.common.exceptions.DatabaseException;
import ru.kolobkevic.currencyexchange.common.exceptions.ObjectAlreadyExistsException;
import ru.kolobkevic.currencyexchange.common.exceptions.ObjectNotFoundException;
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
    private static final String FIND_ALL_SQL = """
            SELECT er.id AS er_id,
            base.id AS b_id,
            base.code AS b_code,
            base.full_name AS b_full_name,
            base.sign AS b_sign,
            target.id AS t_id,
            target.code AS t_code,
            target.full_name AS t_full_name,
            target.sign AS t_sign,
            er.rate AS er_rate
            FROM exchange_rate er
            JOIN currency base ON er.base_currency_id = base.id
            JOIN currency target ON er.target_currency_id = target.id
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT er.id AS er_id,
            base.id AS b_id,
            base.code AS b_code,
            base.full_name AS b_full_name,
            base.sign AS b_sign,
            target.id AS t_id,
            target.code AS t_code,
            target.full_name AS t_full_name,
            target.sign AS t_sign,
            er.rate AS er_rate
            FROM exchange_rate er
            JOIN currency base ON er.base_currency_id = base.id
            JOIN currency target ON er.target_currency_id = target.id WHERE er_id = ?
            """;

    private static final String FIND_BY_CODES_SQL = """
            SELECT er.id AS er_id,
            base.id AS b_id,
            base.code AS b_code,
            base.full_name AS b_full_name,
            base.sign AS b_sign,
            target.id AS t_id,
            target.code AS t_code,
            target.full_name AS t_full_name,
            target.sign AS t_sign,
            er.rate AS er_rate
            FROM exchange_rate er
            JOIN currency base ON er.base_currency_id = base.id
            JOIN currency target ON er.target_currency_id = target.id WHERE b_code = ? AND t_code = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO exchange_rate(base_currency_id, target_currency_id, rate) VALUES(?, ?, ?)
            RETURNING exchange_rate.id;
            """;

    private static final String UPDATE_SQL = """
            UPDATE exchange_rate SET rate =? WHERE base_currency_id = ? AND target_currency_id = ?
            RETURNING exchange_rate.id;
            """;

    private static final String DELETE_SQL = """
            DELETE FROM exchange_rate WHERE id = ?
            """;

    private final Connection connection;

    @Override
    public Optional<ExchangeRate> findById(Integer id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(ResultSetMapper.toExchangeRate(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Unable to get exchange rate with id = " + id, e);
        }
    }

    @Override
    public List<ExchangeRate> findAll() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(ResultSetMapper.toExchangeRate(resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new DatabaseException("Unable to get exchange rates", e);
        }
    }

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setInt(1, exchangeRate.getBaseCurrency().getId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());
            preparedStatement.execute();
            exchangeRate.setId(preparedStatement.getResultSet().getInt(1));
            return exchangeRate;
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code) {
                throw new ObjectAlreadyExistsException(e.getMessage());
            }
            throw new DatabaseException("Unable to save exchange rate", e);
        }
    }


    @Override
    public void deleteById(Integer id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DatabaseException("Unable to delete exchange rate with id = " + id, e);
        }

    }

    public Optional<ExchangeRate> findByExchangeCodes(String baseCode, String targetCode) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODES_SQL)) {
            preparedStatement.setString(1, baseCode);
            preparedStatement.setString(2, targetCode);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return Optional.of(ResultSetMapper.toExchangeRate(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException("Unable to get exchange rate with codes " + baseCode + ", " + targetCode, e);
        }
    }

    public ExchangeRate update(ExchangeRate exchangeRate) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setBigDecimal(1, exchangeRate.getRate());
            preparedStatement.setInt(2, exchangeRate.getBaseCurrency().getId());
            preparedStatement.setInt(3, exchangeRate.getTargetCurrency().getId());
            preparedStatement.execute();
            exchangeRate.setId(preparedStatement.getResultSet().getInt(1));
            return findByExchangeCodes(
                    exchangeRate.getBaseCurrency().getCode(),
                    exchangeRate.getTargetCurrency().getCode())
                    .orElseThrow(() -> new ObjectNotFoundException("Exchange rate not found after update"));
        } catch (SQLException e) {
            throw new DatabaseException("Unable to update exchange rate with codes " +
                    exchangeRate.getBaseCurrency().getCode(), e);
        }
    }
}
