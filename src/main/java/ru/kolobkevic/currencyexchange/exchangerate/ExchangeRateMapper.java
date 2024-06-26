package ru.kolobkevic.currencyexchange.exchangerate;

import com.googlecode.jmapper.JMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateRequestDto;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRateMapper {
    private static final JMapper<ExchangeRateResponseDto, ExchangeRate> responseMapper =
            new JMapper<>(ExchangeRateResponseDto.class, ExchangeRate.class);
    private static final JMapper<ExchangeRate, ExchangeRateRequestDto> requestMapper =
            new JMapper<>(ExchangeRate.class, ExchangeRateRequestDto.class);

    public static ExchangeRateResponseDto toDto(ExchangeRate exchangeRate) {
        return responseMapper.getDestination(exchangeRate);
    }

    public static ExchangeRate toModel(ExchangeRateRequestDto exchangeRate) {
        return requestMapper.getDestination(exchangeRate);
    }
}
