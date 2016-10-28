package ru.akirakozov.sd.refactoring.html;

import java.util.List;

/**
 * Created by eugene on 10/15/16.
 */
public class HtmlFormatter {
    public static <T extends HtmlEncodable> String encode(List<T> l) {
        String result = "";

        for (T el: l) {
            result += el.encode();
        }

        return addHtmlHeader(result);
    }

    public static String addHtmlHeader(String s) {
        return "<html><body>" + s + "</body></html>";
    }
}
