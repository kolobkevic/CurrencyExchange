package ru.kolobkevic.currencyexchange.currency.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kolobkevic.currencyexchange.common.AbstractServlet;
import ru.kolobkevic.currencyexchange.common.exceptions.BadArgumentException;
import ru.kolobkevic.currencyexchange.common.exceptions.DatabaseException;
import ru.kolobkevic.currencyexchange.common.exceptions.ObjectAlreadyExistsException;
import ru.kolobkevic.currencyexchange.common.utils.PathUtils;
import ru.kolobkevic.currencyexchange.currency.CurrencyService;
import ru.kolobkevic.currencyexchange.currency.dto.CurrencyRequestDto;

import java.io.IOException;

import static ru.kolobkevic.currencyexchange.common.Constants.*;

@WebServlet(name = "CurrenciesServlet", value = "/currencies/*")
public class CurrenciesServlet extends AbstractServlet {
    private CurrencyService currencyService;

    @Override
    public void init(ServletConfig config) {
        currencyService = (CurrencyService) config.getServletContext().getAttribute("currencyService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            sendJsonResponse(resp, HttpServletResponse.SC_OK, currencyService.findAll());
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, DEFAULT_ERROR_MESSAGE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String code = req.getParameter("code");
            String fullName = req.getParameter("name");
            String sign = req.getParameter("sign");

            PathUtils.validateStringParams(code, fullName, sign);
            CurrencyRequestDto currency = new CurrencyRequestDto(code, fullName, sign);
            sendJsonResponse(resp, HttpServletResponse.SC_CREATED, currencyService.save(currency));
        } catch (BadArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, BAD_ARGUMENT_MESSAGE);
        } catch (ObjectAlreadyExistsException e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, CURRENCY_EXISTS_MESSAGE);
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, DEFAULT_ERROR_MESSAGE);
        }
    }
}
