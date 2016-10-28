package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.html.HtmlFormatter;

import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static ru.akirakozov.sd.refactoring.dao.Product.NAME;
import static ru.akirakozov.sd.refactoring.dao.Product.PRICE;

/**
 * Created by eugene on 10/15/16.
 */
public class ProductDao {
    private static ProductDao instance = new ProductDao();

    private ProductDao() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            try (Statement stmt = c.createStatement()) {
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private <R> R doRequest(String request, Function<ResultSet, R> fun) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(request)) {

            return fun.apply(rs);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private List<Product> readProducts(ResultSet rs) {
        try {
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("name");
                long price = rs.getLong("price");
                Product product = new Product(name, price);

                products.add(product);
            }

            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Product> getProducts() {
        return doRequest("SELECT * FROM PRODUCT", this::readProducts);
    }

    private Long sumProducts(ResultSet rs) {
        try {
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new RuntimeException("next fail");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Long getSum() {
        return doRequest("SELECT SUM(price) FROM PRODUCT", this::sumProducts);
    }

    private Product maxProduct(ResultSet rs) {
        try {
            if (rs.next()) {
                String name = rs.getString("name");
                long price = rs.getLong("price");

                return new Product(name, price);
            } else {
                throw new RuntimeException("max");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Product getMax() {
        return doRequest("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", this::maxProduct);
    }

    public Product getMin() {
        return doRequest("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1", this::maxProduct);
    }

    private Long countProducts(ResultSet rs) {
        try {
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new RuntimeException("can't get count of products");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Long getCount() {
        return doRequest("SELECT COUNT(*) FROM PRODUCT", this::countProducts);
    }

    public static ProductDao getInstance() {
        return instance;
    }

    public void insert(Product product) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
             Statement stmt = c.createStatement()) {

            String sql = "INSERT INTO PRODUCT " +
                    "(" + NAME + ", " + PRICE + ") VALUES " +
                    "(\"" + product.name + "\", " + product.price + ")";
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
