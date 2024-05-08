package ru.kolobkevic.currencyexchange.currencyexchange.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kolobkevic.currencyexchange.common.AbstractServlet;
import ru.kolobkevic.currencyexchange.common.exceptions.BadArgumentException;
import ru.kolobkevic.currencyexchange.common.exceptions.ObjectNotFoundException;
import ru.kolobkevic.currencyexchange.common.utils.PathUtils;
import ru.kolobkevic.currencyexchange.currencyexchange.CurrencyExchangeService;
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
        exchangeService = (CurrencyExchangeService) config.getServletContext().getAttribute("exchangeService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCurrencyParam = req.getParameter("from");
            String targetCurrencyParam = req.getParameter("to");
            String amountParam = req.getParameter("amount");

            PathUtils.validateStringParams(baseCurrencyParam, targetCurrencyParam, amountParam);
            ExchangeRequestDto requestDto = new ExchangeRequestDto(
                    baseCurrencyParam,
                    targetCurrencyParam,
                    new BigDecimal(amountParam));
            ExchangeResponseDto responseDto = exchangeService.getExchange(requestDto);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, responseDto);
        } catch (BadArgumentException | NumberFormatException e) {
            sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ExceptionDto(e.getMessage()));
        } catch (ObjectNotFoundException e) {
            sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, new ExceptionDto(e.getMessage()));
        } catch (Exception e) {
            sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ExceptionDto(e.getMessage()));
        }
    }
}
