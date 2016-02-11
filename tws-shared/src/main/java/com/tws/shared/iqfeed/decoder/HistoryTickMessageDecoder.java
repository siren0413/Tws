package com.tws.shared.iqfeed.decoder;

import com.google.common.base.Splitter;
import com.tws.shared.iqfeed.model.HistoryTick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 2/10/2016.
 */
@Component
public class HistoryTickMessageDecoder<T> implements MessageDecoder<T> {

    private static final Logger logger = LoggerFactory.getLogger(HistoryTickMessageDecoder.class);

    public T decode(String msg) {
        Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
        List<String> list = splitter.splitToList(msg);
        if (list.size() < 10) {
            logger.error("unable to decode msg: {}", msg);
            return null;
        }
        HistoryTick historyTick = new HistoryTick();
        int i = 0;
        try {
            if (list.size() == 11) {
                historyTick.setRequestId(list.get(i++));
            }
            historyTick.setTimestamp(list.get(i++));
            historyTick.setLast(Float.parseFloat(list.get(i++)));
            historyTick.setLastSize(Integer.parseInt(list.get(i++)));
            historyTick.setTotalVolume(Integer.parseInt(list.get(i++)));
            historyTick.setBid(Float.parseFloat(list.get(i++)));
            historyTick.setAsk(Float.parseFloat(list.get(i++)));
            historyTick.setTickId(Integer.parseInt(list.get(i++)));
            historyTick.setBasisForLast(list.get(i++));
            historyTick.setMarketCenter(Integer.parseInt(list.get(i++)));
            historyTick.setTradeCondition(list.get(i++));
        } catch (Exception e) {
            logger.error("unable to decode msg: " + msg, e);
            return null;
        }
        return (T) historyTick;
    }
}
