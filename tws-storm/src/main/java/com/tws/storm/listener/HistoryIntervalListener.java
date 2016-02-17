package com.tws.storm.listener;

import com.tws.shared.iqfeed.model.HistoryInterval;
import com.tws.storm.spout.HistoryIntervalIqFeedSpout;

/**
 * Created by admin on 2/13/2016.
 */
public class HistoryIntervalListener {

    public void onMessageReceived(HistoryInterval historyInterval) {
        HistoryIntervalIqFeedSpout.queue.offer(historyInterval);
    }
}
