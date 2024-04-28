package ru.kolobkevic.currencyexchange.currencyexchange;


import ru.kolobkevic.currencyexchange.currencyexchange.dto.ExchangeRequestDto;
import ru.kolobkevic.currencyexchange.currencyexchange.dto.ExchangeResponseDto;

public interface CurrencyExchangeService {
    ExchangeResponseDto getExchange(ExchangeRequestDto requestDto);
}
