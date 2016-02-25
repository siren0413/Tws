package com.tws.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.tws.storm.TickAction;
import com.tws.storm.Utils;
import com.tws.storm.job.BarMonitorJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private long lastEmitTimeSec = 0;

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
        declarer.declareStream(STREAM_ID + "_" + interval, new Fields(SYMBOL, "timestamp", "time", "last"));
    }

    @Override
    public void action() {
        ZonedDateTime currZonedDateTime = Utils.getCurrentZonedDateTime(mock);
        long currTimeSec = currZonedDateTime.toInstant().toEpochMilli() / (interval * 1000);
        if (lastEmitTimeSec == 0) {
            lastEmitTimeSec = currTimeSec;
        } else if (lastEmitTimeSec != currTimeSec) {
            for (Map.Entry<String, Queue<Tuple>> entry : map.entrySet()) {
                String symbol = entry.getKey();
                Queue<Tuple> queue = entry.getValue();
                float total = 0;
                int count = 0;
                while (!queue.isEmpty()) {
                    Tuple tuple = queue.poll();
                    total += (tuple.getFloatByField(BID) + tuple.getFloatByField(ASK)) / 2;
                    count++;
                }
                if (count != 0) {
                    float avg = total / count;
                    String baseTimestamp = currZonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
                    outputCollector.emit(STREAM_ID, new Values(symbol, baseTimestamp, currZonedDateTime.toInstant().toEpochMilli(), avg));
                    System.out.println(new Values(symbol, baseTimestamp, currZonedDateTime.toInstant().toEpochMilli(), avg));
                } else {
                    outputCollector.emit(STREAM_ID, new Values(symbol, currZonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), currZonedDateTime.toInstant().toEpochMilli(), Float.NaN));
                    System.out.println(new Values(symbol, currZonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), currZonedDateTime.toInstant().toEpochMilli(), Float.NaN));
                }
            }
            lastEmitTimeSec = currTimeSec;
        }
    }
}


