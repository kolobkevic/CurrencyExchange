package ru.kolobkevic.currencyexchange.currency;

import java.util.Optional;

public interface CurrencyService {
    Optional<Currency> findById(Integer id);
    Optional<Currency> findByCode(String code);
    Iterable<Currency> findAll();
    Currency save(Currency currency);
    void deleteById(Integer id);
}
