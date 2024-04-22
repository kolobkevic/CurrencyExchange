package ru.kolobkevic.currencyexchange.exchangerate.dto;

import com.googlecode.jmapper.annotations.JMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequestDto {
    @JMap
    private Integer baseCurrencyId;
    @JMap
    private Integer targetCurrencyId;
    @JMap
    private BigDecimal rate;
}
