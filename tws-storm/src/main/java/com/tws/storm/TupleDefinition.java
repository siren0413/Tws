package com.tws.storm;

import backtype.storm.tuple.Fields;

import static com.tws.shared.Constants.*;

/**
 * Created by chris on 2/24/16.
 */
public interface TupleDefinition {
    Fields S_LEVEL1_UPDATE = new Fields(SYMBOL, BID, BID_SIZE, BID_TIME, ASK, ASK_SIZE, ASK_TIME, LAST, LAST_SIZE, LAST_TIME, TOTAL_VOLUME, EXTENDED_TRADE,
            EXTENDED_TRADE_SIZE, EXTENDED_TRADE_DATE, EXTENDED_TRADE_TIME, LOW, HIGH, OPEN, MESSAGE_CONTENT, DELAY, EXCHANGE_ID, LOCAL_DATE_TIME);

    Fields S_LEVEL1_BAR = new Fields(SYMBOL, TIMESTAMP, TIME, LOW, HIGH, OPEN, CLOSE, PERIOD_VOLUME);

}
