package com.tws.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.tws.shared.common.TimeUtils;
import com.tws.storm.TickAction;
import com.tws.storm.TupleDefinition;
import com.tws.storm.Utils;
import com.tws.storm.model.BarData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tws.shared.Constants.*;

/**
 * Created by chris on 2/24/16.
 */
public abstract class Level1BarBaseBolt extends BaseRichBolt implements TickAction {

    private static final Logger logger = LoggerFactory.getLogger(Level1BarBaseBolt.class);

    private OutputCollector outputCollector;
    private final Map<String, BarData> map = new ConcurrentHashMap<>();
    private boolean mock = false;
    private long lastEmitIntervalTimeSec = 0;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        mock = (boolean) stormConf.get("mock");
        this.outputCollector = collector;
    }

    @Override
    public void execute(Tuple input) {
        if (!input.getSourceStreamId().equals("S_TICK")) {
            String symbol = input.getStringByField(SYMBOL);

            float newPrice = (input.getFloatByField(BID) + input.getFloatByField(ASK)) / 2;
            int newVolume = input.getIntegerByField(TOTAL_VOLUME);

            if (!map.containsKey(symbol) || map.get(symbol) == null) {
                BarData barData = new BarData();
                barData.setLow(newPrice);
                barData.setHigh(newPrice);
                barData.setOpen(newPrice);
                barData.setClose(newPrice);
                barData.setOpenVolume(newVolume);
                barData.setVolume(0);
                map.put(symbol, barData);
            } else {
                BarData oldBarData = map.get(symbol);
                BarData newBarData = new BarData();
                newBarData.setLow(Float.min(oldBarData.getLow(), newPrice));
                newBarData.setHigh(Float.max(oldBarData.getHigh(), newPrice));
                newBarData.setOpen(oldBarData.getOpen());
                newBarData.setClose(newPrice);
                newBarData.setOpenVolume(oldBarData.getOpenVolume());
                newBarData.setVolume(newVolume - oldBarData.getOpenVolume());
                map.put(symbol, newBarData);
            }
        }
        action();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(getStreamId(), TupleDefinition.S_LEVEL1_BAR);
    }

    protected abstract int getInterval();

    protected abstract String getStreamId();

    @Override
    public void action() {
        ZonedDateTime currZonedDateTime = Utils.getCurrentZonedDateTime(mock);
        long currIntervalTimeSec = currZonedDateTime.toInstant().toEpochMilli() / (getInterval() * 1000);
        if (lastEmitIntervalTimeSec == 0) {

            lastEmitIntervalTimeSec = currIntervalTimeSec;
            map.clear();

        } else if (lastEmitIntervalTimeSec < currIntervalTimeSec) {
            for (Map.Entry<String, BarData> entry : map.entrySet()) {
                String symbol = entry.getKey();
                BarData barData = entry.getValue();

                String baseTimestamp = ZonedDateTime.ofInstant(Instant.ofEpochMilli(currIntervalTimeSec * getInterval() * 1000), TimeUtils.ZONE_EST).format(TimeUtils.dateTimeSecFormatter);

                if (barData != null) {
                    outputCollector.emit(getStreamId(), new Values(symbol, baseTimestamp, currIntervalTimeSec * getInterval() * 1000, getInterval(), barData.getLow(), barData.getHigh(), barData.getOpen(), barData.getClose(), barData.getVolume()));
                    System.out.println(new Values(symbol, baseTimestamp, currIntervalTimeSec * getInterval() * 1000, getInterval(), barData.getLow(), barData.getHigh(), barData.getOpen(), barData.getClose(), barData.getVolume()));
                } else {
                    outputCollector.emit(getStreamId(), new Values(symbol, baseTimestamp, currIntervalTimeSec * getInterval() * 1000, getInterval(), Float.NaN, Float.NaN, Float.NaN, Float.NaN, 0));
                    System.out.println(new Values(symbol, baseTimestamp, currIntervalTimeSec * getInterval() * 1000, getInterval(), Float.NaN, Float.NaN, Float.NaN, Float.NaN, 0));
                }
                map.remove(symbol);
            }
            lastEmitIntervalTimeSec = currIntervalTimeSec;
        }
    }
}


