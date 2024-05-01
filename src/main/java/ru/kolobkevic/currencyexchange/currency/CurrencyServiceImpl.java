package ru.kolobkevic.currencyexchange.currency;

import ru.kolobkevic.currencyexchange.common.exceptions.ObjectNotFoundException;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyRequestDto;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyResponseDto;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(Connection connection) {
        this.currencyRepository = new CurrencyRepository(connection);
    }

    @Override
    public CurrencyResponseDto findById(Integer id) {
        Currency currency = currencyRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Currency not found"));
        return CurrencyMapper.toDto(currency);
    }

    @Override
    public CurrencyResponseDto findByCode(String code) {
        Currency currency = currencyRepository.findByCode(code).orElseThrow(() ->
                new ObjectNotFoundException("Currency not found"));
        return CurrencyMapper.toDto(currency);
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
