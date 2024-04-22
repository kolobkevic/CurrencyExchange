package ru.kolobkevic.currencyexchange.currency.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kolobkevic.currencyexchange.common.AbstractServlet;
import ru.kolobkevic.currencyexchange.common.DatabaseService;
import ru.kolobkevic.currencyexchange.common.DatabaseServiceImpl;
import ru.kolobkevic.currencyexchange.common.PathUtils;
import ru.kolobkevic.currencyexchange.currency.CurrencyService;
import ru.kolobkevic.currencyexchange.currency.CurrencyServiceImpl;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyResponseDto;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "CurrencyServlet", value = "/currency/*")
public class CurrencyServlet extends AbstractServlet {
    private CurrencyService currencyService;
    private DatabaseService databaseService;

    @Override
    public void init(ServletConfig config) {
        databaseService = new DatabaseServiceImpl();
        currencyService = new CurrencyServiceImpl(databaseService.getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String currencyCode = PathUtils.getPathFromRequest(req);
        Optional<CurrencyResponseDto> currencyResponseDto = currencyService.findByCode(currencyCode);
        if (currencyResponseDto.isPresent()) {
            sendJsonResponse(resp, HttpServletResponse.SC_OK, currencyResponseDto.get());
        } else {
            sendJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Currency " + currencyCode + " not found");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String param = req.getParameter("id");
        currencyService.deleteById(Integer.parseInt(param));
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.sendRedirect(req.getContextPath() + "/currency");
    }

}
