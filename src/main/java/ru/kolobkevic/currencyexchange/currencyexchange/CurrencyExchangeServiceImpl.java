package ru.kolobkevic.currencyexchange.currencyexchange;

import ru.kolobkevic.currencyexchange.currency.CurrencyService;
import ru.kolobkevic.currencyexchange.currency.CurrencyServiceImpl;
import ru.kolobkevic.currencyexchange.currencyexchange.dto.ExchangeRequestDto;
import ru.kolobkevic.currencyexchange.currencyexchange.dto.ExchangeResponseDto;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateService;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateServiceImpl;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateResponseDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.Optional;

public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {
    private final ExchangeRateService exchangeRateService;
    private final CurrencyService currencyService;
    private static final String CROSS_CURRENCY = "USD";

    public CurrencyExchangeServiceImpl(Connection connection){
        exchangeRateService = new ExchangeRateServiceImpl(connection);
        currencyService = new CurrencyServiceImpl(connection);
    }

    @Override
    public ExchangeResponseDto getExchange(ExchangeRequestDto requestDto) {
        Optional<ExchangeRateResponseDto> directExchangeRate =
                exchangeRateService.findByExchangeCodes(requestDto.getBaseCurrency(), requestDto.getTargetCurrency());
        if (directExchangeRate.isPresent()) {
            return getDirectExchange(directExchangeRate.get(), requestDto.getAmount());
        }

        Optional<ExchangeRateResponseDto> reverseExchangeRate =
                exchangeRateService.findByExchangeCodes(requestDto.getTargetCurrency(), requestDto.getBaseCurrency());
        if (reverseExchangeRate.isPresent()) {
            return getReverseExchange(reverseExchangeRate.get(), requestDto.getAmount());
        }

        Optional<ExchangeRateResponseDto> currencyFrom =
                exchangeRateService.findByExchangeCodes(CROSS_CURRENCY, requestDto.getBaseCurrency());
        Optional<ExchangeRateResponseDto> currencyTo =
                exchangeRateService.findByExchangeCodes(CROSS_CURRENCY, requestDto.getTargetCurrency());
        if (currencyFrom.isPresent() && currencyTo.isPresent()) {
            return getCrossExchange(currencyFrom.get(), currencyTo.get(), requestDto.getAmount());
        }
        throw new RuntimeException();
    }

    private ExchangeResponseDto getDirectExchange(ExchangeRateResponseDto responseDto, BigDecimal amount) {
        ExchangeResponseDto exchangeResponseDto = new ExchangeResponseDto();
        exchangeResponseDto.setBaseCurrency(currencyService.findById(responseDto.getBaseCurrencyId()).get());
        exchangeResponseDto.setTargetCurrency(currencyService.findById(responseDto.getTargetCurrencyId()).get());
        exchangeResponseDto.setRate(responseDto.getRate());
        exchangeResponseDto.setAmount(amount);
        exchangeResponseDto.setConvertedAmount(convertAmount(amount, responseDto.getRate()));
        return exchangeResponseDto;
    }

    private ExchangeResponseDto getReverseExchange(ExchangeRateResponseDto responseDto, BigDecimal amount) {
        ExchangeResponseDto exchangeResponseDto = new ExchangeResponseDto();
        exchangeResponseDto.setBaseCurrency(currencyService.findById(responseDto.getTargetCurrencyId()).get());
        exchangeResponseDto.setTargetCurrency(currencyService.findById(responseDto.getBaseCurrencyId()).get());
        exchangeResponseDto.setRate(getReverseRate(responseDto.getRate()));
        exchangeResponseDto.setAmount(amount);
        exchangeResponseDto.setConvertedAmount(convertAmount(amount, exchangeResponseDto.getRate()));
        return exchangeResponseDto;
    }

    private ExchangeResponseDto getCrossExchange(ExchangeRateResponseDto base, ExchangeRateResponseDto target,
                                                 BigDecimal amount) {
        ExchangeResponseDto exchangeResponseDto = new ExchangeResponseDto();
        exchangeResponseDto.setBaseCurrency(currencyService.findById(base.getTargetCurrencyId()).get());
        exchangeResponseDto.setTargetCurrency(currencyService.findById(target.getTargetCurrencyId()).get());
        exchangeResponseDto.setRate(getCrossRate(base.getRate(), target.getRate()));
        exchangeResponseDto.setAmount(amount);
        exchangeResponseDto.setConvertedAmount(convertAmount(amount, exchangeResponseDto.getRate()));
        return exchangeResponseDto;
    }

    private BigDecimal getReverseRate(BigDecimal rate) {
        return new BigDecimal(1).divide(rate, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal getCrossRate(BigDecimal fromRate, BigDecimal toRate) {
        return toRate.divide(fromRate, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal convertAmount(BigDecimal amount, BigDecimal rate) {
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
