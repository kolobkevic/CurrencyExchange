package ru.kolobkevic.currencyexchange.currencyexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyResponseDto;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeResponseDto {
    private CurrencyResponseDto baseCurrency;
    private CurrencyResponseDto targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;
}
