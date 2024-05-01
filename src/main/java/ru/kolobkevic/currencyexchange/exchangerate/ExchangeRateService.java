package ru.kolobkevic.currencyexchange.exchangerate;

import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateRequestDto;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateResponseDto;

import java.util.List;

public interface ExchangeRateService {
    ExchangeRateResponseDto findById(Integer id);

    List<ExchangeRateResponseDto> findAll();

    ExchangeRateResponseDto findByExchangeCodes(String baseCode, String targetCode);

    ExchangeRateResponseDto save(ExchangeRateRequestDto exchangeRate);

    ExchangeRateResponseDto update(ExchangeRateRequestDto exchangeRate);

    void deleteById(Integer id);
}
