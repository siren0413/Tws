package com.tws.shared.ib;

/**
 * Created by chris on 1/18/16.
 */
public enum Exchange {

    SMART(0, "SMART"),
    CBOE(1, "CBOE"),
    NASDAQ(2, "NASDAQ"),
    ARCA(3, "ARCA"),
    BATS(4, "BATS"),
    BEX(5, "BEX"),
    BTIG(6, "BTIG"),
    BTIGALGO(7, "BTIGALGO"),
    BYX(8, "BYX"),
    CHX(9, "CHX"),
    CITADEL(10, "CITADEL"),
    CITADELDP(11, "CITADELDP"),
    COWEN(12, "COWEN"),
    NYSE(12, "NYSE"),
    UNKNOWN(Integer.MAX_VALUE, "unknown");

    private int index;
    private String text;

    private Exchange(int index, String text) {
        this.index = index;
        this.text = text;
    }

    public int index() {
        return index;
    }

    public String text() {
        return text;
    }

    public static Exchange get(int index) {
        for (Exchange exchange : values()) {
            if (exchange.index == index) {
                return exchange;
            }
        }
        return UNKNOWN;
    }

    public static String getText(int index) {
        return get(index).text;
    }
}
