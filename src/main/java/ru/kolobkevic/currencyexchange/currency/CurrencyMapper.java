package ru.kolobkevic.currencyexchange.currency;

import com.googlecode.jmapper.JMapper;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyRequestDto;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyResponseDto;

public class CurrencyMapper {
    public static final JMapper<CurrencyResponseDto, Currency> responseMapper =
            new JMapper<>(CurrencyResponseDto.class, Currency.class);
    public static final JMapper<Currency, CurrencyRequestDto> requestMapper =
            new JMapper<>(Currency.class, CurrencyRequestDto.class);

    private CurrencyMapper() {
    }
}
