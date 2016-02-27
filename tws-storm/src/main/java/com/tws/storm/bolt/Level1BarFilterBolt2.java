package com.tws.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.tws.shared.common.TimeUtils;
import com.tws.storm.TickAction;
import com.tws.storm.TupleDefinition;
import com.tws.storm.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import static com.tws.shared.Constants.*;

/**
 * Created by chris on 2/24/16.
 */
public class Level1BarFilterBolt2 extends BaseRichBolt implements TickAction {

    private static final Logger logger = LoggerFactory.getLogger(Level1IntervalFilterBolt.class);
    public static final String STREAM_ID = "S_BAR";

    private int interval;
    private OutputCollector outputCollector;
    private final Map<String, Queue<Tuple>> map = new ConcurrentHashMap<>();
    private boolean mock = false;
    private long lastEmitIntervalTimeSec = 0;

    public Level1BarFilterBolt2(int interval) {
        this.interval = interval;
    }

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
        declarer.declareStream(STREAM_ID + "_" + interval, TupleDefinition.S_LEVEL1_BAR);
    }

    @Override
    public void action() {
        ZonedDateTime currZonedDateTime = Utils.getCurrentZonedDateTime(mock);
        long currIntervalTimeSec = currZonedDateTime.toInstant().toEpochMilli() / (interval * 1000);
        if (lastEmitIntervalTimeSec == 0) {

            lastEmitIntervalTimeSec = currIntervalTimeSec;
            map.values().forEach(Queue<Tuple>::clear);

        } else if (lastEmitIntervalTimeSec != currIntervalTimeSec) {
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

                String baseTimestamp = currZonedDateTime.format(TimeUtils.dateTimeSecFormatter);

                if (count != 0) {
                    outputCollector.emit(STREAM_ID, new Values(symbol, baseTimestamp, currZonedDateTime.toInstant().toEpochMilli(), low, high, open, close, volume));
                    System.out.println(new Values(symbol, baseTimestamp, currZonedDateTime.toInstant().toEpochMilli(), low, high, open, close, volume));
                } else {
                    outputCollector.emit(STREAM_ID, new Values(symbol, baseTimestamp, currZonedDateTime.toInstant().toEpochMilli(), Float.NaN, Float.NaN, Float.NaN, Float.NaN, 0));
                    System.out.println(new Values(symbol, baseTimestamp, currZonedDateTime.toInstant().toEpochMilli(), Float.NaN, Float.NaN, Float.NaN, Float.NaN, 0));
                }
            }
            lastEmitIntervalTimeSec = currIntervalTimeSec;
        }
    }
}


