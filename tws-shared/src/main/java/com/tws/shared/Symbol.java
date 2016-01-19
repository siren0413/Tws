package com.tws.shared;

/**
 * Created by chris on 1/18/16.
 */
public enum Symbol {

    SPX(0, "SPX"),
    AAPL(1, "AAPL"),
    UNKNOWN(Integer.MAX_VALUE, "unknown");

    private int index;
    private String text;

    private Symbol(int index, String text) {
        this.index = index;
        this.text = text;
    }

    public int index() {
        return index;
    }

    public String text() {
        return text;
    }

    public static Symbol get(int index) {
        for (Symbol symbol : values()) {
            if (symbol.index == index) {
                return symbol;
            }
        }
        return UNKNOWN;
    }

    public static String getText(int index){
        return get(index).text;
    }
}
