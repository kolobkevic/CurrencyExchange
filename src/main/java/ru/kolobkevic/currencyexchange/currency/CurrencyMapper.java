package ru.kolobkevic.currencyexchange.currency;

import com.googlecode.jmapper.JMapper;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyRequestDto;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyResponseDto;

public class CurrencyMapper {
    private static final JMapper<CurrencyResponseDto, Currency> responseMapper =
            new JMapper<>(CurrencyResponseDto.class, Currency.class);
    private static final JMapper<Currency, CurrencyRequestDto> requestMapper =
            new JMapper<>(Currency.class, CurrencyRequestDto.class);

    private CurrencyMapper() {
    }

    public static CurrencyResponseDto toDto(Currency currency) {
        return responseMapper.getDestination(currency);
    }

    public static Currency toModel(CurrencyRequestDto currency) {
        return requestMapper.getDestination(currency);
    }
}
