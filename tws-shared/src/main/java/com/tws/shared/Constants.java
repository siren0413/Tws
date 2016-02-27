package com.tws.shared;

/**
 * Created by admin on 2/13/2016.
 */
public class Constants {
    public static final String ROUTEKEY_DELIMETER = ".";

    public static final String LEVEL1_EXCHANGE = "LEVEL1";
    public static final String LEVEL1_FUNDAMENTAL_ROUTEKEY_PREFIX = "F";
    public static final String LEVEL1_SUMMARY_ROUTEKEY_PREFIX = "P";
    public static final String LEVEL1_UPDATE_ROUTEKEY_PREFIX = "Q";
    public static final String LEVEL1_REGIONAL_ROUTEKEY_PREFIX = "R";
    public static final String LEVEL1_NEWS_ROUTEKEY_PREFIX = "N";
    public static final String LEVEL1_SYSTEM_ROUTEKEY_PREFIX = "S";
    public static final String LEVEL1_TIMESTAMP_ROUTEKEY_PREFIX = "T";

    public static final String LEVEL1_SYSTEM_WATCH_KEY = "WATCHES";

    public static final String HISTORY_EXCHANGE = "HISTORY";
    public static final String HISTORY_TICK_ROUTEKEY_PREFIX = "HT";
    public static final String HISTORY_INTERVAL_ROUTEKEY_PREFIX = "HI";
    public static final String HISTORY_COMMAND_ROUTEKEY_PREFIX = "CMD";
    public static final String HISTORY_INTERVAL_RESPONSE_ROUTEKEY_PREFIX = "HI.RESPONSE";



//    level1update
    public static final String SYMBOL = "symbol";
    public static final String BID = "bid";
    public static final String BID_SIZE = "bidSize";
    public static final String BID_TIME = "bidTime";
    public static final String ASK = "ask";
    public static final String ASK_SIZE = "askSize";
    public static final String ASK_TIME = "askTime";
    public static final String LAST = "last";
    public static final String LAST_SIZE = "lastSize";
    public static final String LAST_TIME = "lastTime";

    public static final String EXTENDED_TRADE = "extendedTrade";
    public static final String EXTENDED_TRADE_SIZE = "extendedTradeSize";
    public static final String EXTENDED_TRADE_DATE = "extendedTradeDate";
    public static final String EXTENDED_TRADE_TIME = "extendedTradeTime";

    public static final String TOTAL_VOLUME = "totalVolume";
    public static final String LOW = "low";
    public static final String HIGH = "high";
    public static final String OPEN = "open";

    public static final String MESSAGE_CONTENT = "messageContent";
    public static final String DELAY = "delay";
    public static final String EXCHANGE_ID = "exchangeId";
    public static final String LOCAL_DATE_TIME = "localDateTime";


    // level1bar
    public static final String TIMESTAMP = "timestamp";
    public static final String TIME = "time";
    public static final String CLOSE = "close";
    public static final String PERIOD_VOLUME = "periodVolume";


}
