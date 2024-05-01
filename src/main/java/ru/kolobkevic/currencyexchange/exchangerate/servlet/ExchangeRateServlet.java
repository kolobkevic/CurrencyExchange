package ru.kolobkevic.currencyexchange.exchangerate.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.kolobkevic.currencyexchange.common.AbstractServlet;
import ru.kolobkevic.currencyexchange.common.db.DatabaseService;
import ru.kolobkevic.currencyexchange.common.db.DatabaseServiceImpl;
import ru.kolobkevic.currencyexchange.common.exceptions.DatabaseException;
import ru.kolobkevic.currencyexchange.common.exceptions.ObjectNotFoundException;
import ru.kolobkevic.currencyexchange.common.utils.PathUtils;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateService;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateServiceImpl;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateRequestDto;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateResponseDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static ru.kolobkevic.currencyexchange.common.Constants.*;

@WebServlet(name = "ExchangeRateServlet", value = "/exchangeRate/*")
@Slf4j
public class ExchangeRateServlet extends AbstractServlet {
    private ExchangeRateService exchangeRateService;

    @Override
    public void init(ServletConfig config) {
        DatabaseService databaseService = new DatabaseServiceImpl();
        exchangeRateService = new ExchangeRateServiceImpl(databaseService.getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<String> currencies = PathUtils.getListOfCurrenciesFromRequest(req);
        String baseCurrency = currencies.get(0);
        String targetCurrency = currencies.get(1);
        try {
            ExchangeRateResponseDto exchangeRateResponseDto =
                    exchangeRateService.findByExchangeCodes(baseCurrency, targetCurrency);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, exchangeRateResponseDto);
        } catch (ObjectNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, CURRENCY_NOT_FOUND_MESSAGE);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ILLEGAL_ARGUMENT_MESSAGE);
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, DEFAULT_ERROR_MESSAGE);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");
        try {
            exchangeRateService.deleteById(Integer.parseInt(param));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ILLEGAL_ARGUMENT_MESSAGE);
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, DEFAULT_ERROR_MESSAGE);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> codes = PathUtils.getListOfCurrenciesFromRequest(req);
        String baseCode = codes.get(0);
        String targetCode = codes.get(1);
        log.info("Updating exchange rate from {} to {}", baseCode, targetCode);
        log.info(req.getParameter("rate"));
        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(req.getParameter("rate")));
        try {
            ExchangeRateResponseDto existedExchangeRate =
                    exchangeRateService.findByExchangeCodes(baseCode, targetCode);
            ExchangeRateRequestDto requestDto = new ExchangeRateRequestDto(
                    existedExchangeRate.getBaseCurrency().getCode(),
                    existedExchangeRate.getTargetCurrency().getCode(),
                    rate);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, exchangeRateService.update(requestDto));
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ILLEGAL_ARGUMENT_MESSAGE);
        } catch (ObjectNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, CURRENCY_NOT_FOUND_MESSAGE);
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, DEFAULT_ERROR_MESSAGE);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            this.doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }
}
