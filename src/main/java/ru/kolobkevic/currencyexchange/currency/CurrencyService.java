package ru.kolobkevic.currencyexchange.currency;

import ru.kolobkevic.currencyexchange.currency.dto.CurrencyRequestDto;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyResponseDto;

import java.util.List;

public interface CurrencyService {
    CurrencyResponseDto findById(Integer id);

    CurrencyResponseDto findByCode(String code);

    List<CurrencyResponseDto> findAll();

    CurrencyResponseDto save(CurrencyRequestDto currency);

    void deleteById(Integer id);
}
