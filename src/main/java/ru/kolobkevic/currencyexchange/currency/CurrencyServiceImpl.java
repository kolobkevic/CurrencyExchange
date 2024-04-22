package ru.kolobkevic.currencyexchange.currency;

import ru.kolobkevic.currencyexchange.currency.dto.CurrencyRequestDto;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyResponseDto;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(Connection connection) {
        this.currencyRepository = new CurrencyRepository(connection);
    }

    @Override
    public Optional<CurrencyResponseDto> findById(Integer id) {
        Optional<Currency> currency = currencyRepository.findById(id);
        return currency.map(CurrencyMapper::toDto);
    }

    @Override
    public Optional<CurrencyResponseDto> findByCode(String code) {
        Optional<Currency> currency = currencyRepository.findByCode(code);
        return currency.map(CurrencyMapper::toDto);
    }

    @Override
    public List<CurrencyResponseDto> findAll() {
        List<Currency> currencies = currencyRepository.findAll();
        if (currencies.isEmpty()) {
            return Collections.emptyList();
        }
        return currencies.stream().map(CurrencyMapper::toDto).toList();
    }

    @Override
    public CurrencyResponseDto save(CurrencyRequestDto currencyRequestDto) {
        Currency currency = currencyRepository.save(CurrencyMapper.toModel(currencyRequestDto));
        return CurrencyMapper.toDto(currency);
    }

    @Override
    public void deleteById(Integer id) {
        currencyRepository.deleteById(id);
    }
}
