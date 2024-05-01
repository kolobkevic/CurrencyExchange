package ru.kolobkevic.currencyexchange.currencyexchange.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kolobkevic.currencyexchange.common.AbstractServlet;
import ru.kolobkevic.currencyexchange.common.db.DatabaseService;
import ru.kolobkevic.currencyexchange.common.db.DatabaseServiceImpl;
import ru.kolobkevic.currencyexchange.common.exceptions.ObjectNotFoundException;
import ru.kolobkevic.currencyexchange.currencyexchange.CurrencyExchangeService;
import ru.kolobkevic.currencyexchange.currencyexchange.CurrencyExchangeServiceImpl;
import ru.kolobkevic.currencyexchange.currencyexchange.dto.ExceptionDto;
import ru.kolobkevic.currencyexchange.currencyexchange.dto.ExchangeRequestDto;
import ru.kolobkevic.currencyexchange.currencyexchange.dto.ExchangeResponseDto;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "CurrencyExchangeServlet", value = "/exchange/*")
public class CurrencyExchangeServlet extends AbstractServlet {
    private CurrencyExchangeService exchangeService;

    @Override
    public void init(ServletConfig config) {
        DatabaseService databaseService = new DatabaseServiceImpl();
        exchangeService = new CurrencyExchangeServiceImpl(databaseService.getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ExchangeRequestDto requestDto = new ExchangeRequestDto();
        requestDto.setBaseCurrency(req.getParameter("from"));
        requestDto.setTargetCurrency(req.getParameter("to"));
        requestDto.setAmount(BigDecimal.valueOf(Double.parseDouble(req.getParameter("amount"))));
        try {
            ExchangeResponseDto responseDto = exchangeService.getExchange(requestDto);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, responseDto);
        } catch (ObjectNotFoundException e) {
            sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, new ExceptionDto(e.getMessage()));
        } catch (Exception e) {
            sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ExceptionDto(e.getMessage()));
        }
    }
}
