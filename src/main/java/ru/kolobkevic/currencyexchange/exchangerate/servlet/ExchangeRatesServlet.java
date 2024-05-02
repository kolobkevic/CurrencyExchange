package ru.kolobkevic.currencyexchange.exchangerate.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kolobkevic.currencyexchange.common.AbstractServlet;
import ru.kolobkevic.currencyexchange.common.db.DatabaseService;
import ru.kolobkevic.currencyexchange.common.db.DatabaseServiceImpl;
import ru.kolobkevic.currencyexchange.common.exceptions.BadArgumentException;
import ru.kolobkevic.currencyexchange.common.exceptions.DatabaseException;
import ru.kolobkevic.currencyexchange.common.exceptions.ObjectAlreadyExistsException;
import ru.kolobkevic.currencyexchange.common.exceptions.ObjectNotFoundException;
import ru.kolobkevic.currencyexchange.common.utils.PathUtils;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateService;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateServiceImpl;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateRequestDto;

import java.io.IOException;
import java.math.BigDecimal;

import static ru.kolobkevic.currencyexchange.common.Constants.*;

@WebServlet(name = "ExchangeRatesServlet", value = "/exchangeRates/*")
public class ExchangeRatesServlet extends AbstractServlet {
    private ExchangeRateService exchangeRateService;

    @Override
    public void init(ServletConfig config) {
        DatabaseService databaseService = new DatabaseServiceImpl();
        exchangeRateService = new ExchangeRateServiceImpl(databaseService.getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            sendJsonResponse(resp, HttpServletResponse.SC_OK, exchangeRateService.findAll());
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, DEFAULT_ERROR_MESSAGE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode = req.getParameter("targetCurrencyCode");
            String rateParam = req.getParameter("rate");

            PathUtils.validateStringParams(baseCurrencyCode, targetCurrencyCode, rateParam);
            BigDecimal rate = BigDecimal.valueOf(Float.parseFloat(rateParam));
            ExchangeRateRequestDto exchangeRateRequestDto =
                    new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rate);
            sendJsonResponse(resp, HttpServletResponse.SC_CREATED, exchangeRateService.save(exchangeRateRequestDto));
        } catch (BadArgumentException | NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, BAD_ARGUMENT_MESSAGE);
        } catch (ObjectAlreadyExistsException e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, CURRENCY_EXISTS_MESSAGE);
        } catch (ObjectNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, CURRENCY_NOT_FOUND_MESSAGE);
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, DEFAULT_ERROR_MESSAGE);
        }
    }

}
