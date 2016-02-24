package com.tws.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.tws.shared.Constants.*;

/**
 * Created by admin on 2/21/2016.
 */
public class Level1IntervalFilterBolt extends BaseRichBolt {

    private static final Logger logger = LoggerFactory.getLogger(Level1IntervalFilterBolt.class);

    public static final String STREAM_ID = "S_INTERVAL";
    private int interval;
    private OutputCollector outputCollector;

    private Map<String, StatusHolder> map = new HashMap<>();

    public Level1IntervalFilterBolt(int interval) {
        this.interval = interval;
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        // data from stream
        String symbol = tuple.getStringByField(SYMBOL);
        long lastTime =  LocalDateTime.parse(tuple.getStringByField(LAST_TIME),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")).atZone(ZoneId.of("America/New_York")).toInstant().toEpochMilli();
        float lastPrice = tuple.getFloatByField(LAST);

        StatusHolder stat = map.get(symbol);
        if (stat == null) {
            stat = new StatusHolder();
            map.put(symbol, stat);
        }

        long timeDiff = lastTime - stat.previousLastTime;
        if (timeDiff < 0) {
            logger.error("Bad ordering of data, lastTime: {}, currentTime: {}.", stat.previousLastTime, lastTime);
            return;
        }
        if ((timeDiff / 1000) < interval) {
            stat.total += lastPrice;
            stat.count++;
        } else {
            // emit old data
            float avg = stat.total / (float) stat.count;
            long time = (stat.previousLastTime / 1000) * 1000;
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.of("America/New_York"));
            String timeString = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            outputCollector.emit(new Values(symbol, timeString, time, avg));
            System.out.println(new Values(symbol, timeString, time, avg));
            // inject new data
            stat.count = 1;
            stat.previousLastTime = lastTime;
            stat.total = lastPrice;
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declareStream(STREAM_ID + "_" + interval, new Fields(SYMBOL, "timestamp", "time", "last"));
    }

    class StatusHolder {
        public float total = 0f;
        public int count = 0;
        public long previousLastTime = 0;
    }
}
