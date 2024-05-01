package ru.kolobkevic.currencyexchange.currency;

import com.googlecode.jmapper.JMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyRequestDto;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyMapper {
    private static final JMapper<CurrencyResponseDto, Currency> responseMapper =
            new JMapper<>(CurrencyResponseDto.class, Currency.class);
    private static final JMapper<Currency, CurrencyRequestDto> requestMapper =
            new JMapper<>(Currency.class, CurrencyRequestDto.class);
    private static final JMapper<Currency, CurrencyResponseDto> currencyFromResponseMapper =
            new JMapper<>(Currency.class, CurrencyResponseDto.class);

    public static CurrencyResponseDto toDto(Currency currency) {
        return responseMapper.getDestination(currency);
    }

    public static Currency toModel(CurrencyRequestDto currency) {
        return requestMapper.getDestination(currency);
    }

    public static Currency fromResponseToModel(CurrencyResponseDto currency) {
        return currencyFromResponseMapper.getDestination(currency);
    }
}
