package ru.kolobkevic.currencyexchange.common;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import ru.kolobkevic.currencyexchange.common.db.DatabaseServiceImpl;
import ru.kolobkevic.currencyexchange.currency.CurrencyService;
import ru.kolobkevic.currencyexchange.currency.CurrencyServiceImpl;
import ru.kolobkevic.currencyexchange.currencyexchange.CurrencyExchangeService;
import ru.kolobkevic.currencyexchange.currencyexchange.CurrencyExchangeServiceImpl;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateService;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateServiceImpl;

import java.sql.Connection;

public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();

        Connection connection = new DatabaseServiceImpl().getConnection();

        CurrencyService currencyService = new CurrencyServiceImpl(connection);
        context.setAttribute("currencyService", currencyService);

        ExchangeRateService exchangeRateService = new ExchangeRateServiceImpl(connection);
        context.setAttribute("exchangeRateService", exchangeRateService);

        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeServiceImpl(connection);
        context.setAttribute("exchangeService", currencyExchangeService);
    }
}
