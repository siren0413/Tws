package com.tws.shared.model;

/**
 * Created by chris on 1/18/16.
 */
public enum Currency {

    USD(0, "USD"),
    UNKNOWN(Integer.MAX_VALUE, "unknown");

    private int index;
    private String text;

    private Currency(int index, String text) {
        this.index = index;
        this.text = text;
    }

    public int index() {
        return index;
    }

    public String text() {
        return text;
    }

    public static Currency get(int index) {
        for (Currency currency : values()) {
            if (currency.index == index) {
                return currency;
            }
        }
        return UNKNOWN;
    }

    public static String getText(int index) {
        return get(index).text;
    }
}
