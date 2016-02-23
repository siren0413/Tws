package com.tws.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

import static com.tws.shared.Constants.*;

/**
 * Created by admin on 2/21/2016.
 */
public class Level1IntervalFilterBolt extends BaseRichBolt{

    public static final String STREAM_ID = "INTERVAL";
    private int interval;
    private OutputCollector outputCollector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        outputCollector.emit(new Values(tuple));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declareStream("haha", new Fields(SYMBOL, BID, BID_SIZE, BID_TIME, ASK, ASK_SIZE, ASK_TIME, LAST, LAST_SIZE, LAST_TIME, TOTAL_VOLUME, LOW, HIGH, OPEN));
    }
}
