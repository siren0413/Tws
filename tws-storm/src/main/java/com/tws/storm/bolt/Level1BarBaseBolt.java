package com.tws.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Time;
import com.tws.shared.common.TimeUtils;
import com.tws.storm.TickAction;
import com.tws.storm.TupleDefinition;
import com.tws.storm.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import static com.tws.shared.Constants.*;

/**
 * Created by chris on 2/24/16.
 */
public abstract class Level1BarBaseBolt extends BaseRichBolt implements TickAction {

    private static final Logger logger = LoggerFactory.getLogger(Level1BarBaseBolt.class);

    private OutputCollector outputCollector;
    private final Map<String, Queue<Tuple>> map = new ConcurrentHashMap<>();
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

            if (!map.containsKey(symbol)) {
                map.put(symbol, new LinkedList<>());
            }
            Queue<Tuple> queue = map.get(symbol);
            queue.add(input);
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
            map.values().forEach(Queue<Tuple>::clear);

        } else if (lastEmitIntervalTimeSec < currIntervalTimeSec) {
            for (Map.Entry<String, Queue<Tuple>> entry : map.entrySet()) {
                String symbol = entry.getKey();
                Queue<Tuple> queue = entry.getValue();

                float total = 0;
                float low = Float.MAX_VALUE;
                float high = 0;
                float open = 0;
                float close = 0;
                int volume = 0;
                int count = 0;

                while (!queue.isEmpty()) {
                    Tuple tuple = queue.poll();
                    float price = (tuple.getFloatByField(BID) + tuple.getFloatByField(ASK)) / 2;

                    total += price;
                    low = Float.min(low, price);
                    high = Float.max(high, price);
                    if (open == 0) {
                        open = price;
                        volume = tuple.getIntegerByField(TOTAL_VOLUME);
                    }
                    if (queue.isEmpty()) {
                        close = price;
                        volume = tuple.getIntegerByField(TOTAL_VOLUME) - volume;
                    }
                    count++;
                }

                String baseTimestamp = ZonedDateTime.ofInstant(Instant.ofEpochMilli(currIntervalTimeSec * getInterval() * 1000), TimeUtils.ZONE_EST).format(TimeUtils.dateTimeSecFormatter);

                if (count != 0) {
                    outputCollector.emit(getStreamId(), new Values(symbol, baseTimestamp, currIntervalTimeSec * getInterval() * 1000, getInterval(), low, high, open, close, volume));
                    System.out.println(new Values(symbol, baseTimestamp, currZonedDateTime.toInstant().toEpochMilli(), getInterval(), low, high, open, close, volume));
                } else {
                    outputCollector.emit(getStreamId(), new Values(symbol, baseTimestamp, currIntervalTimeSec * getInterval() * 1000, getInterval(), Float.NaN, Float.NaN, Float.NaN, Float.NaN, 0));
                    System.out.println(new Values(symbol, baseTimestamp, currZonedDateTime.toInstant().toEpochMilli(), getInterval(), Float.NaN, Float.NaN, Float.NaN, Float.NaN, 0));
                }
            }
            lastEmitIntervalTimeSec = currIntervalTimeSec;
        }
    }
}


