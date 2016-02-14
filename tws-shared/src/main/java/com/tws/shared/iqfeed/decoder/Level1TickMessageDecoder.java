package com.tws.shared.iqfeed.decoder;

import com.google.common.base.Splitter;
import com.tws.shared.iqfeed.model.Level1Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by admin on 2/11/2016.
 */
public class Level1TickMessageDecoder<T> implements MessageDecoder<T>  {

    private static final Logger logger = LoggerFactory.getLogger(Level1TickMessageDecoder.class);

    @Override
    public T decode(String msg) {
        Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
        List<String> list = splitter.splitToList(msg);
        if (list.size() < 14) {
            logger.error("unable to decode msg: {}", msg);
            return null;
        }
        Level1Tick level1Tick = new Level1Tick();
        int i = 0;
        try {
            level1Tick.setSymbol(list.get(i++));
            level1Tick.setBid(Float.parseFloat(list.get(i++)));
            level1Tick.setBidSize(Integer.parseInt(list.get(i++)));
            level1Tick.setBidTime(list.get(i++));
            level1Tick.setAsk(Float.parseFloat(list.get(i++)));
            level1Tick.setAskSize(Integer.parseInt(list.get(i++)));
            level1Tick.setAskTime(list.get(i++));
            level1Tick.setLast(Float.parseFloat(list.get(i++)));
            level1Tick.setLastSize(Integer.parseInt(list.get(i++)));
            level1Tick.setLastTime(list.get(i++));
            level1Tick.setTotalVolume(Integer.parseInt(list.get(i++)));
            level1Tick.setLow(Float.parseFloat(list.get(i++)));
            level1Tick.setHigh(Float.parseFloat(list.get(i++)));
            level1Tick.setOpen(Float.parseFloat(list.get(i++)));
        } catch (Exception e) {
            logger.error("unable to decode msg: " + msg, e);
            return null;
        }
        return (T) level1Tick;
    }
}
