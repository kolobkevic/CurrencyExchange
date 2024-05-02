package ru.kolobkevic.currencyexchange.exchangerate.dto;

import com.googlecode.jmapper.annotations.JMap;
import com.googlecode.jmapper.annotations.JMapConversion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kolobkevic.currencyexchange.common.db.DatabaseService;
import ru.kolobkevic.currencyexchange.common.db.DatabaseServiceImpl;
import ru.kolobkevic.currencyexchange.currency.Currency;
import ru.kolobkevic.currencyexchange.currency.CurrencyMapper;
import ru.kolobkevic.currencyexchange.currency.CurrencyService;
import ru.kolobkevic.currencyexchange.currency.CurrencyServiceImpl;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequestDto {
    @JMap("baseCurrency")
    private String baseCurrencyCode;
    @JMap("targetCurrency")
    private String targetCurrencyCode;
    @JMap
    private BigDecimal rate;

    @JMapConversion(from = {"baseCurrencyCode"}, to = {"baseCurrency"})
    public Currency getBaseCurrency(String baseCurrencyCode) {
        DatabaseService databaseService = new DatabaseServiceImpl();
        CurrencyService service = new CurrencyServiceImpl(databaseService.getConnection());
        return CurrencyMapper.fromResponseToModel(service.findByCode(baseCurrencyCode));
    }

    @JMapConversion(from = {"targetCurrencyCode"}, to = {"targetCurrency"})
    public Currency getTargetCurrency(String targetCurrencyCode) {
        DatabaseService databaseService = new DatabaseServiceImpl();
        CurrencyService service = new CurrencyServiceImpl(databaseService.getConnection());
        return CurrencyMapper.fromResponseToModel(service.findByCode(targetCurrencyCode));
    }
}
