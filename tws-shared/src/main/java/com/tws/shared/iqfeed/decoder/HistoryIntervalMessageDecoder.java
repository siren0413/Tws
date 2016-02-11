package com.tws.shared.iqfeed.decoder;

import com.google.common.base.Splitter;
import com.tws.shared.iqfeed.model.HistoryInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by admin on 2/10/2016.
 */
public class HistoryIntervalMessageDecoder<T> implements MessageDecoder<T> {

    private static final Logger logger = LoggerFactory.getLogger(HistoryIntervalMessageDecoder.class);

    @Override
    public T decode(String msg) {
        Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
        List<String> list = splitter.splitToList(msg);
        if (list.size() < 8) {
            logger.error("unable to decode msg: {}", msg);
            return null;
        }
        HistoryInterval historyInterval = new HistoryInterval();
        int i = 0;
        try {
            if (list.size() == 9) {
                historyInterval.setRequestId(list.get(i++));
            }
            historyInterval.setTimestamp(list.get(i++));
            historyInterval.setHigh(Float.parseFloat(list.get(i++)));
            historyInterval.setLow(Float.parseFloat(list.get(i++)));
            historyInterval.setOpen(Float.parseFloat(list.get(i++)));
            historyInterval.setClose(Float.parseFloat(list.get(i++)));
            historyInterval.setTotalVolume(Integer.parseInt(list.get(i++)));
            historyInterval.setPeriodVolume(Integer.parseInt(list.get(i++)));
            historyInterval.setNumTrades(Integer.parseInt(list.get(i++)));
        } catch (Exception e) {
            logger.error("unable to decode msg: " + msg, e);
            return null;
        }
        return (T) historyInterval;
    }
}
