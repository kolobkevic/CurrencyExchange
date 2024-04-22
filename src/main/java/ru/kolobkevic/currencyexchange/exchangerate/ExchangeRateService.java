package ru.kolobkevic.currencyexchange.exchangerate;

import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateRequestDto;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateResponseDto;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateService {
    Optional<ExchangeRateResponseDto> findById(Integer id);

    List<ExchangeRateResponseDto> findAll();

    ExchangeRateResponseDto save(ExchangeRateRequestDto exchangeRate);

    void deleteById(Integer id);
}
