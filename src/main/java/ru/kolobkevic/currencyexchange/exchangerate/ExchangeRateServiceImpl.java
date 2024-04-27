package ru.kolobkevic.currencyexchange.exchangerate;

import lombok.RequiredArgsConstructor;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateRequestDto;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateResponseDto;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateServiceImpl(Connection connection) {
        exchangeRateRepository = new ExchangeRateRepository(connection);
    }

    @Override
    public Optional<ExchangeRateResponseDto> findById(Integer id) {
        Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findById(id);
        return exchangeRate.map(ExchangeRateMapper::toDto);
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
    public Optional<ExchangeRateResponseDto> findByExchangeCodes(String baseCode, String targetCode) {
        Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findByExchangeCodes(baseCode, targetCode);
        return exchangeRate.map(ExchangeRateMapper::toDto);
    }

    @Override
    public ExchangeRateResponseDto update(ExchangeRateRequestDto exchangeRateRequestDto) {
        ExchangeRate exchangeRate = exchangeRateRepository.update(ExchangeRateMapper.toModel(exchangeRateRequestDto));
        return ExchangeRateMapper.toDto(exchangeRate);
    }
}
