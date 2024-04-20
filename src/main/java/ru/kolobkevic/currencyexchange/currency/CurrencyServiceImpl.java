package ru.kolobkevic.currencyexchange.currency;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Override
    public Optional<Currency> findById(Integer id) {
        return currencyRepository.findById(id);
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        return currencyRepository.findByCode(code);
    }

    @Override
    public List<Currency> findAll() {
        List<Currency> currencies = currencyRepository.findAll();
        return currencies.isEmpty() ? Collections.emptyList() : currencies;
    }

    @Override
    public Currency save(Currency currency) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {
        currencyRepository.deleteById(id);
    }

    public void delete(Currency currency) {
        currencyRepository.deleteById(currency.getId());
    }
}
