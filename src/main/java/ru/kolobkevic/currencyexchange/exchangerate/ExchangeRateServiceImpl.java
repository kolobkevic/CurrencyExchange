package ru.kolobkevic.currencyexchange.exchangerate;

import lombok.RequiredArgsConstructor;
import ru.kolobkevic.currencyexchange.common.exceptions.ObjectNotFoundException;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateRequestDto;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateResponseDto;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateServiceImpl(Connection connection) {
        exchangeRateRepository = new ExchangeRateRepository(connection);
    }

    @Override
    public ExchangeRateResponseDto findById(Integer id) {
        ExchangeRate exchangeRate = exchangeRateRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Exchange rate with id " + id + " not found"));
        return ExchangeRateMapper.toDto(exchangeRate);
    }

    @Override
    public List<ExchangeRateResponseDto> findAll() {
        List<ExchangeRate> exchangeRate = exchangeRateRepository.findAll();
        if (exchangeRate.isEmpty()) {
            return Collections.emptyList();
        }
        return exchangeRate.stream().map(ExchangeRateMapper::toDto).toList();
    }

    @Override
    public ExchangeRateResponseDto save(ExchangeRateRequestDto exchangeRateRequestDto) {
        ExchangeRate exchangeRate = exchangeRateRepository.save(ExchangeRateMapper.toModel(exchangeRateRequestDto));
        return ExchangeRateMapper.toDto(exchangeRate);
    }

    @Override
    public void deleteById(Integer id) {
        exchangeRateRepository.deleteById(id);
    }

    @Override
    public ExchangeRateResponseDto findByExchangeCodes(String baseCode, String targetCode) {
        ExchangeRate exchangeRate = exchangeRateRepository.findByExchangeCodes(baseCode, targetCode).orElseThrow(
                () -> new ObjectNotFoundException("Exchange rate with code " + baseCode + " not found")
        );
        return ExchangeRateMapper.toDto(exchangeRate);
    }

    @Override
    public ExchangeRateResponseDto update(ExchangeRateRequestDto exchangeRateRequestDto) {
        ExchangeRate exchangeRate = exchangeRateRepository.update(ExchangeRateMapper.toModel(exchangeRateRequestDto));
        return ExchangeRateMapper.toDto(exchangeRate);
    }
}
