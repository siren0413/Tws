package com.tws.data;

import com.ib.client.TickType;
import com.tws.shared.Symbol;
import com.tws.shared.model.Tick;
import org.apache.commons.lang3.SerializationUtils;
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

    public void updateTickPrice(int tickerId, int field, double price) {
        Tick lastTick = tickCache.get(tickerId);
        switch (field) {
            case 1:
                lastTick.setBidPrice(price);
                break;
            case 2:
                lastTick.setAskPrice(price);
                break;
            case 4:
                lastTick.setLastPrice(price);
                break;
            default:
                logger.error("updateTickPrice: unknown ticker field [{}], text: [{}], value: [{}]", field, TickType.getField(field), price);
        }
        long timestamp = System.currentTimeMillis();
        // reset timestamp to force updateTickSize() to update tick.
        if (timestamp == lastTick.getTimestamp()) {
            timestamp = 0;
        }
        lastTick.setTimestamp(timestamp);
    }

    public Tick updateTickSize(int tickerId, int field, int size) {
        Tick lastTick = tickCache.get(tickerId);
        switch (field) {
            case 0:
                lastTick.setBidSize(size);
                break;
            case 3:
                lastTick.setAskSize(size);
                break;
            case 5:
                lastTick.setLastSize(size);
                break;
            case 8:
                break;
            default:
                logger.error("updateTickSize: unknown ticker field [{}], text: [{}], size: [{}]", field, TickType.getField(field), size);
        }
        long timestamp = System.currentTimeMillis();
        if (timestamp == lastTick.getTimestamp()) {
            return null;
        }

        lastTick.setTimestamp(timestamp);
        Tick newTick = SerializationUtils.clone(lastTick);
        resetLastTick(lastTick);
        return newTick;
    }

    public Tick updateTickString(int tickerId, int tickType, String value) {
        Tick lastTick = tickCache.get(tickerId);
        switch (tickType) {
            case 48:
                String[] parts = value.split(";");
                if (parts.length < 6) {
                    logger.error("RTVolume parts [{}] < 6", parts.length);
                    return null;
                }
                int volume = Integer.valueOf(parts[3]);
                lastTick.setTotalVolume(volume);
                break;
            case 45:
                break;
            default:
                logger.error("updateTickString: unknown ticker field [{}], text: [{}], value: [{}]", tickType, TickType.getField(tickType), value);
        }
        long timestamp = System.currentTimeMillis();
        if (timestamp == lastTick.getTimestamp()) {
            return null;
        }
        lastTick.setTimestamp(timestamp);
        return SerializationUtils.clone(lastTick);
    }

    private void resetLastTick(Tick tick) {
        tick.setLastSize(0);
    }

    public void afterPropertiesSet() throws Exception {
        tickCache = new HashMap<Integer, Tick>();
        for (Symbol symbol : Symbol.values()) {
            tickCache.put(symbol.index(), new Tick(symbol.index()));
        }
    }
}
