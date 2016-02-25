package com.tws.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.tws.shared.iqfeed.model.Level1Update;
import com.tws.storm.TupleDefinition;

import java.util.Map;
import java.util.concurrent.*;


/**
 * Created by admin on 2/14/2016.
 */
public class TickSpout extends BaseRichSpout {

    private static final String STREAM_ID = "S_TICK";
    public static BlockingQueue<Boolean> queue = new LinkedBlockingDeque<>();
    private SpoutOutputCollector collector;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                queue.offer(true);
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    @Override
    public void nextTuple() {
        try {
            Boolean tick = TickSpout.queue.take();
            collector.emit(STREAM_ID, new Values(true));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(STREAM_ID, new Fields("tick"));
    }
}
