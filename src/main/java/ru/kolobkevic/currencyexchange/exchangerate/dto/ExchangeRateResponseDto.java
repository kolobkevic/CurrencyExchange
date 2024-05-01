package ru.kolobkevic.currencyexchange.exchangerate.dto;

import com.googlecode.jmapper.annotations.JMap;
import com.googlecode.jmapper.annotations.JMapConversion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kolobkevic.currencyexchange.currency.Currency;
import ru.kolobkevic.currencyexchange.currency.CurrencyMapper;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyResponseDto;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponseDto {
    @JMap
    private Integer id;
    @JMap
    private CurrencyResponseDto baseCurrency;
    @JMap
    private CurrencyResponseDto targetCurrency;
    @JMap
    private BigDecimal rate;

    @JMapConversion(from = {"baseCurrency"}, to = {"baseCurrency"})
    public CurrencyResponseDto getBaseCurrencyDto(Currency baseCurrency) {
        return CurrencyMapper.toDto(baseCurrency);
    }

    @JMapConversion(from = {"targetCurrency"}, to = {"targetCurrency"})
    public CurrencyResponseDto getTargetCurrencyDto(Currency targetCurrency) {
        return CurrencyMapper.toDto(targetCurrency);
    }
}
