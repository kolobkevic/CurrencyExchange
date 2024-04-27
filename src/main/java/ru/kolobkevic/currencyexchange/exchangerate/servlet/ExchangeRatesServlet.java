package ru.kolobkevic.currencyexchange.exchangerate.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kolobkevic.currencyexchange.common.AbstractServlet;
import ru.kolobkevic.currencyexchange.common.DatabaseService;
import ru.kolobkevic.currencyexchange.common.DatabaseServiceImpl;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateService;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateServiceImpl;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateRequestDto;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "ExchangeRatesServlet", value = "/exchangeRates/*")
public class ExchangeRatesServlet extends AbstractServlet {
    private ExchangeRateService exchangeRateService;
    private DatabaseService databaseService;

    @Override
    public void init(ServletConfig config) {
        databaseService = new DatabaseServiceImpl();
        exchangeRateService = new ExchangeRateServiceImpl(databaseService.getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        sendJsonResponse(resp, HttpServletResponse.SC_OK, exchangeRateService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer baseId = Integer.parseInt(req.getParameter("baseCurrencyId"));
        Integer targetId = Integer.parseInt(req.getParameter("targetCurrencyId"));
        BigDecimal rate = BigDecimal.valueOf(Float.parseFloat(req.getParameter("rate")));
        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseId, targetId, rate);
        sendJsonResponse(resp, HttpServletResponse.SC_CREATED, exchangeRateService.save(exchangeRateRequestDto));
    }

}
