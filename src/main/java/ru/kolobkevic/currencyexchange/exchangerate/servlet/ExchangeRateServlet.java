package ru.kolobkevic.currencyexchange.exchangerate.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.kolobkevic.currencyexchange.common.AbstractServlet;
import ru.kolobkevic.currencyexchange.common.DatabaseService;
import ru.kolobkevic.currencyexchange.common.DatabaseServiceImpl;
import ru.kolobkevic.currencyexchange.common.PathUtils;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateService;
import ru.kolobkevic.currencyexchange.exchangerate.ExchangeRateServiceImpl;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateRequestDto;
import ru.kolobkevic.currencyexchange.exchangerate.dto.ExchangeRateResponseDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "ExchangeRateServlet", value = "/exchangeRate/*")
@Slf4j
public class ExchangeRateServlet extends AbstractServlet {
    private ExchangeRateService exchangeRateService;
    private DatabaseService databaseService;

    @Override
    public void init(ServletConfig config) {
        databaseService = new DatabaseServiceImpl();
        exchangeRateService = new ExchangeRateServiceImpl(databaseService.getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<String> currencies = PathUtils.getListOfCurrenciesFromRequest(req);
        String baseCurrency = currencies.get(0);
        String targetCurrency = currencies.get(1);
        Optional<ExchangeRateResponseDto> exchangeRateResponseDto =
                exchangeRateService.findByExchangeCodes(baseCurrency, targetCurrency);
        if (exchangeRateResponseDto.isPresent()) {
            sendJsonResponse(resp, HttpServletResponse.SC_OK, exchangeRateResponseDto.get());
        } else {
            sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    "Exchange rate " + baseCurrency + targetCurrency + " not found");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");
        exchangeRateService.deleteById(Integer.parseInt(param));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> codes = PathUtils.getListOfCurrenciesFromRequest(req);
        String baseCode = codes.get(0);
        String targetCode = codes.get(1);
        log.info("Updating exchange rate from {} to {}", baseCode, targetCode);
        log.info(req.getParameter("rate"));
        BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(req.getParameter("rate")));
        Optional<ExchangeRateResponseDto> exchangeRateResponseDto =
                exchangeRateService.findByExchangeCodes(baseCode, targetCode);
        if (exchangeRateResponseDto.isPresent()) {
            ExchangeRateResponseDto exchangeRateDto = exchangeRateResponseDto.get();
            ExchangeRateRequestDto requestDto = new ExchangeRateRequestDto(exchangeRateDto.getBaseCurrencyId(),
                    exchangeRateDto.getTargetCurrencyId(), rate);
            ExchangeRateResponseDto responseDto = exchangeRateService.update(requestDto);
            if (responseDto != null) {
                sendJsonResponse(resp, HttpServletResponse.SC_OK, responseDto);
            } else {
                sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Failed to save exchange rate");
            }
        } else {
            sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Failed to save exchange rate");
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().toUpperCase().equals("PATCH")) {
            this.doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }
}
