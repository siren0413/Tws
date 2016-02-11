package com.tws.shared.ib;

import com.ib.client.Types;
import com.tws.shared.ib.model.Currency;

/**
 * Created by chris on 1/18/16.
 */
public enum Symbol {

    SPX(0, "SPX", Types.SecType.IND, Exchange.SMART, Exchange.CBOE, Currency.USD),
    SPY(1, "SPY", Types.SecType.STK, Exchange.SMART, Exchange.ARCA, Currency.USD),
    DJX(2, "DJX", Types.SecType.IND, Exchange.SMART, Exchange.CBOE, Currency.USD),
    QQQ(3, "QQQ", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    AAPL(10, "AAPL", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    GOOG(11, "GOOG", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    FB(12, "FB", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    YHOO(13, "YHOO", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    VZ(14, "VZ", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    MSFT(15, "MSFT", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    NOK(16, "NOK", Types.SecType.STK, Exchange.SMART, Exchange  .NASDAQ, Currency.USD),
    LNKD(17, "LNKD", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    CME(18, "CME", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    NKE(19, "NKE", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    AMZN(20, "AMZN", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    VEEV(21, "VEEV", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    SBUX(22, "SBUX", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    EA(23, "EA", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    TWTR(24, "TWTR", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    SAP(25, "SAP", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    ATVI(26, "ATVI", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    BABA(27, "BABA", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    JPM(28, "JPM", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    C(29, "C", Types.SecType.STK, Exchange.SMART, Exchange.NASDAQ, Currency.USD),
    AXP(30, "AXP", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    GPRO(31, "GPRO", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    CTRP(32, "CTRP", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    FIT(33, "FIT", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    QIHU(34, "QIHU", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    DIS(35, "DIS", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    F(36, "F", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    GE(37, "GE", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    GM(38, "GM", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    GS(39, "GS", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    HD(40, "HD", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    IBM(41, "IBM", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    PYPL(42, "PYPL", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    QCOM(43, "QCOM", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    T(44, "T", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    TGT(45, "TGT", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    TWX(46, "TWX", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD),
    V(47, "V", Types.SecType.STK, Exchange.SMART, Exchange.NYSE, Currency.USD);

    private int index;
    private String text;
    private Types.SecType secType;
    private Exchange exchange;
    private Exchange primaryExchange;
    private Currency currency;

    private Symbol(int index, String text, Types.SecType secType, Exchange exchange, Exchange primaryExchange, Currency currency) {
        this.index = index;
        this.text = text;
        this.secType = secType;
        this.exchange = exchange;
        this.primaryExchange = primaryExchange;
        this.currency = currency;
    }

    public int index() {
        return index;
    }

    public String text() {
        return text;
    }

    public String secType() {
        return secType.toString();
    }

    public String exchange() {
        return exchange.text();
    }

    public String primaryExchange() {
        return primaryExchange.text();
    }

    public String currency() {
        return currency.text();
    }

    public static Symbol get(int index) {
        for (Symbol symbol : values()) {
            if (symbol.index == index) {
                return symbol;
            }
        }
        return null;
    }

    public static Symbol get(String text) {
        for (Symbol symbol : values()) {
            if (symbol.text.equals(text)) {
                return symbol;
            }
        }
        return null;
    }

    public static String getText(int index) {
        return get(index).text;
    }
}
