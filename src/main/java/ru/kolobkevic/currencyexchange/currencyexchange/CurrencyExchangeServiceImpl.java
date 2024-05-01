package ru.kolobkevic.currencyexchange.currencyexchange;

import ru.kolobkevic.currencyexchange.common.exceptions.ObjectNotFoundException;
import ru.kolobkevic.currencyexchange.currencyexchange.dto.ExchangeRequestDto;
import ru.kolobkevic.currencyexchange.currencyexchange.dto.ExchangeResponseDto;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateService;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateServiceImpl;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateResponseDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;

public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {
    private final ExchangeRateService exchangeRateService;
    private static final String CROSS_CURRENCY = "USD";

    public CurrencyExchangeServiceImpl(Connection connection) {
        exchangeRateService = new ExchangeRateServiceImpl(connection);
    }

    @Override
    public ExchangeResponseDto getExchange(ExchangeRequestDto requestDto) {
        try {
            return getDirectExchange(requestDto);
        } catch (ObjectNotFoundException e) {
            try {
                return getReverseExchange(requestDto);
            } catch (ObjectNotFoundException e1) {
                try {
                    return getCrossExchange(requestDto);
                } catch (ObjectNotFoundException e2) {
                    throw new ObjectNotFoundException("Currency not found", e2);
                }
            }
        }
    }

    private ExchangeResponseDto getDirectExchange(ExchangeRequestDto requestDto) {
        ExchangeRateResponseDto directExchangeRate =
                exchangeRateService.findByExchangeCodes(requestDto.getBaseCurrency(), requestDto.getTargetCurrency());
        ExchangeResponseDto exchangeResponseDto = new ExchangeResponseDto();
        exchangeResponseDto.setBaseCurrency(directExchangeRate.getBaseCurrency());
        exchangeResponseDto.setTargetCurrency(directExchangeRate.getTargetCurrency());
        exchangeResponseDto.setRate(directExchangeRate.getRate());
        exchangeResponseDto.setAmount(requestDto.getAmount());
        exchangeResponseDto.setConvertedAmount(convertAmount(requestDto.getAmount(), directExchangeRate.getRate()));
        return exchangeResponseDto;
    }

    private ExchangeResponseDto getReverseExchange(ExchangeRequestDto requestDto) {
        ExchangeRateResponseDto reverseExchangeRate =
                exchangeRateService.findByExchangeCodes(requestDto.getTargetCurrency(), requestDto.getBaseCurrency());
        ExchangeResponseDto exchangeResponseDto = new ExchangeResponseDto();
        exchangeResponseDto.setBaseCurrency(reverseExchangeRate.getTargetCurrency());
        exchangeResponseDto.setTargetCurrency(reverseExchangeRate.getBaseCurrency());
        exchangeResponseDto.setRate(getReverseRate(reverseExchangeRate.getRate()));
        exchangeResponseDto.setAmount(requestDto.getAmount());
        exchangeResponseDto.setConvertedAmount(convertAmount(requestDto.getAmount(), exchangeResponseDto.getRate()));
        return exchangeResponseDto;
    }

    private ExchangeResponseDto getCrossExchange(ExchangeRequestDto requestDto) {
        ExchangeRateResponseDto currencyFrom =
                exchangeRateService.findByExchangeCodes(CROSS_CURRENCY, requestDto.getBaseCurrency());
        ExchangeRateResponseDto currencyTo =
                exchangeRateService.findByExchangeCodes(CROSS_CURRENCY, requestDto.getTargetCurrency());
        ExchangeResponseDto exchangeResponseDto = new ExchangeResponseDto();
        exchangeResponseDto.setBaseCurrency(currencyFrom.getTargetCurrency());
        exchangeResponseDto.setTargetCurrency(currencyTo.getTargetCurrency());
        exchangeResponseDto.setRate(getCrossRate(currencyFrom.getRate(), currencyTo.getRate()));
        exchangeResponseDto.setAmount(requestDto.getAmount());
        exchangeResponseDto.setConvertedAmount(convertAmount(requestDto.getAmount(), exchangeResponseDto.getRate()));
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
