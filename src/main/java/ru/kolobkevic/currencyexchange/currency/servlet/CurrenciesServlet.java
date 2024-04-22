package ru.kolobkevic.currencyexchange.currency.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kolobkevic.currencyexchange.common.AbstractServlet;
import ru.kolobkevic.currencyexchange.common.DatabaseService;
import ru.kolobkevic.currencyexchange.common.DatabaseServiceImpl;
import ru.kolobkevic.currencyexchange.currency.CurrencyService;
import ru.kolobkevic.currencyexchange.currency.CurrencyServiceImpl;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyRequestDto;

import java.io.IOException;

@WebServlet(name = "CurrenciesServlet", value = "/currencies/*")
public class CurrenciesServlet extends AbstractServlet {
    private CurrencyService currencyService;
    private DatabaseService databaseService;

    @Override
    public void init(ServletConfig config) {
        databaseService = new DatabaseServiceImpl();
        currencyService = new CurrencyServiceImpl(databaseService.getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        sendJsonResponse(resp, HttpServletResponse.SC_OK, currencyService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        String fullName = req.getParameter("name");
        String sign = req.getParameter("sign");
        CurrencyRequestDto currency = new CurrencyRequestDto(code, fullName, sign);
        sendJsonResponse(resp, HttpServletResponse.SC_CREATED, currencyService.save(currency));
    }
}
