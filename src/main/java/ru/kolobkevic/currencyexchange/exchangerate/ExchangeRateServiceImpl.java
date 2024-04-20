package ru.kolobkevic.currencyexchange.exchangerate;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public Optional<ExchangeRate> findById(Integer id) {
        return exchangeRateRepository.findById(id);
    }

    @Override
    public List<ExchangeRate> findAll() {
        List<ExchangeRate> exchangeRate = exchangeRateRepository.findAll();
        return exchangeRate.isEmpty() ? Collections.emptyList() : exchangeRate;
    }

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {
        exchangeRateRepository.deleteById(id);
    }

    public void delete(ExchangeRate exchangeRate) {
        exchangeRateRepository.deleteById(exchangeRate.getId());
    }
}
