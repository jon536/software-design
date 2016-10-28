package ru.akirakozov.sd.refactoring.servlet;

import org.eclipse.jetty.util.StringUtil;
import org.sqlite.util.StringUtils;
import ru.akirakozov.sd.refactoring.dao.Product;
import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.html.HtmlFormatter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTML;
import java.io.IOException;
import java.sql.*;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        ProductDao productDao = ProductDao.getInstance();
        String res;
        switch (command) {
            case "max":
                res = "<h1>Product with max price: </h1>" + System.lineSeparator();
                res += productDao.getMax().encode();
                break;

            case "min":
                res = "<h1>Product with min price: </h1>" + System.lineSeparator();
                res += productDao.getMin().encode();
                break;

            case "sum":
                res = "Summary price: " + System.lineSeparator();
                res += productDao.getSum().toString() + System.lineSeparator();
                break;

            case "count":
                res = "Number of products: " + System.lineSeparator();
                res += productDao.getCount();
                break;

            default:
                res = "Unknown command: " + command;
                break;
        }

        response.getWriter().print(HtmlFormatter.addHtmlHeader(res));

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
