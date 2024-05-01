package ru.kolobkevic.currencyexchange.currency.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kolobkevic.currencyexchange.common.AbstractServlet;
import ru.kolobkevic.currencyexchange.common.db.DatabaseService;
import ru.kolobkevic.currencyexchange.common.db.DatabaseServiceImpl;
import ru.kolobkevic.currencyexchange.common.exceptions.DatabaseException;
import ru.kolobkevic.currencyexchange.common.exceptions.ObjectNotFoundException;
import ru.kolobkevic.currencyexchange.common.utils.PathUtils;
import ru.kolobkevic.currencyexchange.currency.CurrencyService;
import ru.kolobkevic.currencyexchange.currency.CurrencyServiceImpl;

import java.io.IOException;

import static ru.kolobkevic.currencyexchange.common.Constants.DEFAULT_ERROR_MESSAGE;
import static ru.kolobkevic.currencyexchange.common.Constants.ILLEGAL_ARGUMENT_MESSAGE;
import static ru.kolobkevic.currencyexchange.common.Constants.CURRENCY_NOT_FOUND_MESSAGE;

@WebServlet(name = "CurrencyServlet", value = "/currency/*")
public class CurrencyServlet extends AbstractServlet {
    private transient CurrencyService currencyService;

    @Override
    public void init(ServletConfig config) {
        DatabaseService databaseService = new DatabaseServiceImpl();
        currencyService = new CurrencyServiceImpl(databaseService.getConnection());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String currencyCode = PathUtils.getPathFromRequest(req);
        try {
            sendJsonResponse(resp, HttpServletResponse.SC_OK, currencyService.findByCode(currencyCode));
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
            currencyService.deleteById(Integer.parseInt(param));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ILLEGAL_ARGUMENT_MESSAGE);
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, DEFAULT_ERROR_MESSAGE);
        }
    }
}
