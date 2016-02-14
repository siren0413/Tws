package com.tws.shared.iqfeed.decoder;

import com.google.common.base.Splitter;
import com.tws.shared.iqfeed.model.Level1Summary;
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
        Level1Summary level1Summary = new Level1Summary();
        int i = 0;
        try {
            level1Summary.setSymbol(list.get(i++));
            level1Summary.setBid(Float.parseFloat(list.get(i++)));
            level1Summary.setBidSize(Integer.parseInt(list.get(i++)));
            level1Summary.setBidTime(list.get(i++));
            level1Summary.setAsk(Float.parseFloat(list.get(i++)));
            level1Summary.setAskSize(Integer.parseInt(list.get(i++)));
            level1Summary.setAskTime(list.get(i++));
            level1Summary.setLast(Float.parseFloat(list.get(i++)));
            level1Summary.setLastSize(Integer.parseInt(list.get(i++)));
            level1Summary.setLastTime(list.get(i++));
            level1Summary.setTotalVolume(Integer.parseInt(list.get(i++)));
            level1Summary.setLow(Float.parseFloat(list.get(i++)));
            level1Summary.setHigh(Float.parseFloat(list.get(i++)));
            level1Summary.setOpen(Float.parseFloat(list.get(i++)));
        } catch (Exception e) {
            logger.error("unable to decode msg: " + msg, e);
            return null;
        }
        return (T) level1Summary;
    }
}
