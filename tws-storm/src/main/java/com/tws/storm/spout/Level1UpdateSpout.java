package com.tws.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Values;
import com.tws.shared.iqfeed.model.Level1Update;
import com.tws.storm.TupleDefinition;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;



/**
 * Created by admin on 2/14/2016.
 */
public class Level1UpdateSpout extends BaseRichSpout {

    private static final String STREAM_ID = "S_LEVEL1_UPDATE";
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
                    level1Update.getExtendedTrade(),
                    level1Update.getExtendedTradeSize(),
                    level1Update.getExtendedTradeDate(),
                    level1Update.getExtendedTradeTime(),
                    level1Update.getLow(),
                    level1Update.getHigh(),
                    level1Update.getOpen(),
                    level1Update.getMessageContent(),
                    level1Update.getDelay(),
                    level1Update.getExchangeId(),
                    level1Update.getLocalDateTime()
            ));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(STREAM_ID, TupleDefinition.S_LEVEL1_UPDATE);
    }
}
