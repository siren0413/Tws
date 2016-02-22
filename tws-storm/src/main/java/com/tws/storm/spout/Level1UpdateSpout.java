package com.tws.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.tws.shared.iqfeed.model.Level1Summary;
import com.tws.shared.iqfeed.model.Level1Update;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static com.tws.shared.Constants.*;
import static com.tws.shared.Constants.OPEN;


/**
 * Created by admin on 2/14/2016.
 */
public class Level1UpdateSpout extends BaseRichSpout {

    private static final String STREAM_ID = "level1_update";
    public static BlockingQueue<Level1Update> queue = new LinkedBlockingDeque<>();
    private SpoutOutputCollector collector;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        try {
            Level1Update level1Update = Level1UpdateSpout.queue.take();
            collector.emit(STREAM_ID, new Values(
                    level1Update.getSymbol(),
                    level1Update.getBid(),
                    level1Update.getBidSize(),
                    level1Update.getBidTime(),
                    level1Update.getAsk(),
                    level1Update.getAskSize(),
                    level1Update.getAskTime(),
                    level1Update.getLast(),
                    level1Update.getLastSize(),
                    level1Update.getLastTime(),
                    level1Update.getTotalVolume(),
                    level1Update.getLow(),
                    level1Update.getHigh(),
                    level1Update.getOpen()
            ));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(STREAM_ID, new Fields(SYMBOL, BID, BID_SIZE, BID_TIME, ASK, ASK_SIZE, ASK_TIME, LAST, LAST_SIZE, LAST_TIME, TOTAL_VOLUME, LOW, HIGH, OPEN));
    }
}
