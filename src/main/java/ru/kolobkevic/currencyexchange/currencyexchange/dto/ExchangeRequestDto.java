package ru.kolobkevic.currencyexchange.currencyexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRequestDto {
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal amount;
}
