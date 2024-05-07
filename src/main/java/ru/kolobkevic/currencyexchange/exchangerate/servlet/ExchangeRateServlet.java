package ru.kolobkevic.currencyexchange.exchangerate.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.kolobkevic.currencyexchange.common.AbstractServlet;
import ru.kolobkevic.currencyexchange.common.exceptions.BadArgumentException;
import ru.kolobkevic.currencyexchange.common.exceptions.DatabaseException;
import ru.kolobkevic.currencyexchange.common.exceptions.ObjectNotFoundException;
import ru.kolobkevic.currencyexchange.common.utils.PathUtils;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateService;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateRequestDto;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateResponseDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import static ru.kolobkevic.currencyexchange.common.Constants.*;

@WebServlet(name = "ExchangeRateServlet", value = "/exchangeRate/*")
@Slf4j
public class ExchangeRateServlet extends AbstractServlet {
    private ExchangeRateService exchangeRateService;

    @Override
    public void init(ServletConfig config) {
        exchangeRateService =
                (ExchangeRateService) config.getServletContext().getAttribute("exchangeRateService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<String> currencies = PathUtils.getListOfCurrenciesFromRequest(req);
            String baseCurrency = currencies.get(0);
            String targetCurrency = currencies.get(1);

            PathUtils.validateStringParams(baseCurrency, targetCurrency);
            ExchangeRateResponseDto exchangeRateResponseDto =
                    exchangeRateService.findByExchangeCodes(baseCurrency, targetCurrency);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, exchangeRateResponseDto);
        } catch (ObjectNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, CURRENCY_NOT_FOUND_MESSAGE);
        } catch (BadArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, BAD_ARGUMENT_MESSAGE);
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, DEFAULT_ERROR_MESSAGE);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String param = req.getParameter("id");

            PathUtils.validateStringParams(param);
            exchangeRateService.deleteById(Integer.parseInt(param));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException | BadArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, BAD_ARGUMENT_MESSAGE);
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, DEFAULT_ERROR_MESSAGE);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<String> codes = PathUtils.getListOfCurrenciesFromRequest(req);
            String baseCode = codes.get(0);
            String targetCode = codes.get(1);
            String rateParam = req.getParameter("rate");

            PathUtils.validateStringParams(baseCode, targetCode, rateParam);
            BigDecimal rate = new BigDecimal(rateParam, new MathContext(5, RoundingMode.HALF_UP));

            log.info("Updating exchange rate from {} to {}. Rate = {}", baseCode, targetCode, rate);

            ExchangeRateResponseDto existedExchangeRate =
                    exchangeRateService.findByExchangeCodes(baseCode, targetCode);
            ExchangeRateRequestDto requestDto = new ExchangeRateRequestDto(
                    existedExchangeRate.getBaseCurrency().getCode(),
                    existedExchangeRate.getTargetCurrency().getCode(),
                    rate);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, exchangeRateService.update(requestDto));
        } catch (BadArgumentException | NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, BAD_ARGUMENT_MESSAGE);
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
