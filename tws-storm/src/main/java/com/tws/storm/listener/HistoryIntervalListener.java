package com.tws.storm.listener;

import com.tws.shared.iqfeed.model.HistoryInterval;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by admin on 2/13/2016.
 */
public class HistoryIntervalListener {

    public static BlockingQueue<HistoryInterval> queue = new LinkedBlockingDeque<>();

    public void onMessageReceived(HistoryInterval historyInterval) {
        queue.offer(historyInterval);
    }
}
