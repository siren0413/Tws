package com.tws.storm;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.tws.shared.iqfeed.model.HistoryInterval;
import com.tws.storm.listener.HistoryIntervalListener;

import java.util.Map;

/**
 * Created by admin on 2/10/2016.
 */
public class TestSpout extends BaseRichSpout {

    SpoutOutputCollector spoutOutputCollector;

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("symbol","timestamp","last"));
    }

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector = spoutOutputCollector;
    }

    public void nextTuple() {
        try {
            HistoryInterval historyInterval = HistoryIntervalListener.queue.take();
            spoutOutputCollector.emit(new Values(historyInterval.getRequestId(), historyInterval.getTimestamp(), historyInterval.getClose()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
