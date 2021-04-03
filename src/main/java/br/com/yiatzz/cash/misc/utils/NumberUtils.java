package br.com.yiatzz.cash.misc.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtils {

    private static final NumberFormat format = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

    public static String format(double number) {
        return format.format(roundDouble(number));
    }

    private static double roundDouble(double base) {
        return new BigDecimal(base).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
