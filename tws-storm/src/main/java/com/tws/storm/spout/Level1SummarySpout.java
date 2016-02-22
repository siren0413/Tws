package com.tws.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import static com.tws.shared.Constants.*;

import com.tws.shared.iqfeed.model.Level1Summary;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * Created by admin on 2/14/2016.
 */
public class Level1SummarySpout extends BaseRichSpout {

    private static final String STREAM_ID = "level1_summary";
    public static BlockingQueue<Level1Summary> queue = new LinkedBlockingDeque<>();
    private SpoutOutputCollector collector;


    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        try {
            Level1Summary level1Summary = Level1SummarySpout.queue.take();
            collector.emit(STREAM_ID, new Values(
                    level1Summary.getSymbol(),
                    level1Summary.getBid(),
                    level1Summary.getBidSize(),
                    level1Summary.getBidTime(),
                    level1Summary.getAsk(),
                    level1Summary.getAskSize(),
                    level1Summary.getAskTime(),
                    level1Summary.getLast(),
                    level1Summary.getLastSize(),
                    level1Summary.getLastTime(),
                    level1Summary.getTotalVolume(),
                    level1Summary.getLow(),
                    level1Summary.getHigh(),
                    level1Summary.getOpen()
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
