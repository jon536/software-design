package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.html.HtmlEncodable;

/**
 * Created by eugene on 10/15/16.
 */
public class Product implements HtmlEncodable {
    public static final String NAME = "NAME";
    public static final String PRICE = "PRICE";
    String name;
    long   price;

    public Product(String name, long price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String encode() {
        return name + "\t" + price + "</br>";
    }
}
