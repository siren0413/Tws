package com.tws.storm.spout;

import backtype.storm.tuple.Values;
import com.tws.shared.iqfeed.model.HistoryInterval;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * Created by admin on 2/14/2016.
 */
public class HistoryIntervalIqFeedSpout extends HistoryIntervalBase {

    public static BlockingQueue<HistoryInterval> queue = new LinkedBlockingDeque<>();

    @Override
    public void nextTuple() {
        try {
            HistoryInterval historyInterval = HistoryIntervalIqFeedSpout.queue.take();
            spoutOutputCollector.emit(new Values(historyInterval.getRequestId(), historyInterval.getTimestamp(),
                    historyInterval.getHigh(), historyInterval.getLow(), historyInterval.getClose(),
                    historyInterval.getTotalVolume(), historyInterval.getPeriodVolume(), historyInterval.getNumTrades()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
