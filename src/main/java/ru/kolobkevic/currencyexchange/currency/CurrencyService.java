package ru.kolobkevic.currencyexchange.currency;

import ru.kolobkevic.currencyexchange.currency.dto.CurrencyRequestDto;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyResponseDto;

import java.util.Optional;

public interface CurrencyService {
    Optional<CurrencyResponseDto> findById(Integer id);
    Optional<CurrencyResponseDto> findByCode(String code);
    Iterable<CurrencyResponseDto> findAll();
    CurrencyResponseDto save(CurrencyRequestDto currency);
    void deleteById(Integer id);
}
