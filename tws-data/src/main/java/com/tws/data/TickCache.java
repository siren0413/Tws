package com.tws.data;

import com.ib.client.TickType;
import com.tws.shared.Symbol;
import com.tws.shared.model.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chris on 1/18/16.
 */
public class TickCache implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(TickCache.class);

    Map<Integer, Tick> tickCache;

    public Tick updateTickPrice(int tickerId, int field, double price) {
        Tick tick = tickCache.get(tickerId);
        switch (field) {
            case 1:
                tick.setBidPrice(price);
                break;
            case 2:
                tick.setAskPrice(price);
                break;
            case 4:
                tick.setLastPrice(price);
                break;
            default:
                logger.error("updateTickPrice: unknown ticker field [{}], text: [{}]", field, TickType.getField(field));
        }
        long timestamp = System.currentTimeMillis();
        if (timestamp == tick.getTimestamp()) {
            return null;
        }
        tick.setTimestamp(timestamp);
        return tick;
    }

    public Tick updateTickSize(int tickerId, int field, int size) {
        Tick tick = tickCache.get(tickerId);
        switch (field) {
            case 0:
                tick.setBidSize(size);
                break;
            case 3:
                tick.setAskSize(size);
                break;
            case 5:
                tick.setLastSize(size);
                break;
            case 8:
                break;
            default:
                logger.error("updateTickSize: unknown ticker field [{}], text: [{}], size: [{}]", field, TickType.getField(field), size);
        }
        long timestamp = System.currentTimeMillis();
        if (timestamp == tick.getTimestamp()) {
            return null;
        }
        tick.setTimestamp(timestamp);
        return tick;
    }

    public Tick updateTickString(int tickerId, int tickType, String value) {
        Tick tick = tickCache.get(tickerId);
        switch (tickType) {
            case 48:
                String[] parts = value.split(";");
                if (parts.length < 6){
                    logger.error("RTVolume parts [{}] < 6", parts.length);
                    return null;
                }
                int volume = Integer.valueOf(parts[3]);
                tick.setTotalVolume(volume);
                break;
            case 45:
                break;
            default:
                logger.error("updateTickString: unknown ticker field [{}], text: [{}], value: [{}]", tickType, TickType.getField(tickType), value);
        }
        long timestamp = System.currentTimeMillis();
        if (timestamp == tick.getTimestamp()) {
            return null;
        }
        tick.setTimestamp(timestamp);
        return tick;
    }


    public void afterPropertiesSet() throws Exception {
        tickCache = new HashMap<Integer, Tick>();
        for (Symbol symbol : Symbol.values()) {
            tickCache.put(symbol.index(), new Tick(symbol.index()));
        }
    }
}
