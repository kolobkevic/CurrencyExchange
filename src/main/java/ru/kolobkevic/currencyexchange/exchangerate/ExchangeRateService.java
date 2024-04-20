package ru.kolobkevic.currencyexchange.exchangerate;

import java.util.Optional;

public interface ExchangeRateService {
    Optional<ExchangeRate> findById(Integer id);
    Iterable<ExchangeRate> findAll();
    ExchangeRate save(ExchangeRate exchangeRate);
    void deleteById(Integer id);
}
