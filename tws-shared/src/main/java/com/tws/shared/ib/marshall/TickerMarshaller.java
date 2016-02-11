package com.tws.shared.ib.marshall;

import com.tws.shared.Marshaller;
import com.tws.shared.ib.MsgType;
import com.tws.shared.ib.model.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chris on 1/18/16.
 */
public class TickerMarshaller implements Marshaller {

    private static final Logger logger = LoggerFactory.getLogger(TickerMarshaller.class);
    private static final String DELIMITER = ":";

    public String marshal(Object obj) {
        Tick tick = (Tick) obj;
        String msgType = String.valueOf(MsgType.TICK.index());
        String symbol = String.valueOf(tick.getSymbol());
        String bid = String.valueOf(tick.getBidPrice());
        String ask = String.valueOf(tick.getAskPrice());
        String last = String.valueOf(tick.getLastPrice());
        String bidSize = String.valueOf(tick.getBidSize());
        String askSize = String.valueOf(tick.getAskSize());
        String lastSize = String.valueOf(tick.getLastSize());
        String totalVolume = String.valueOf(tick.getTotalVolume());
        String timestamp = String.valueOf(tick.getTimestamp());
        return String.join(DELIMITER, msgType, symbol, bid, ask, last, bidSize, askSize, lastSize, totalVolume, timestamp);
    }

    public Object unmarshal(String text) {
        String[] parts = text.split(DELIMITER);
        if (parts.length < 10) {
            logger.error("Unable to unmarshal tick string [{}], parts [{}]", text, parts.length);
            return null;
        }
        int msgType = Integer.valueOf(parts[0]);
        int symbol = Integer.valueOf(parts[1]);
        double bid = Double.valueOf(parts[2]);
        double ask = Double.valueOf(parts[3]);
        double last = Double.valueOf(parts[4]);
        int bidSize = Integer.valueOf(parts[5]);
        int askSize = Integer.valueOf(parts[6]);
        int lastSize = Integer.valueOf(parts[7]);
        int totalVolume = Integer.valueOf(parts[8]);
        long timestamp = Long.valueOf(parts[9]);
        Tick tick = new Tick();
        tick.setSymbol(symbol);
        tick.setBidPrice(bid);
        tick.setAskPrice(ask);
        tick.setLastPrice(last);
        tick.setBidSize(bidSize);
        tick.setAskSize(askSize);
        tick.setLastSize(lastSize);
        tick.setTotalVolume(totalVolume);
        tick.setTimestamp(timestamp);
        return tick;
    }
}
