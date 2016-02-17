package com.tws.repository;

import com.tws.shared.iqfeed.model.HistoryInterval;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by admin on 2/16/2016.
 */
public class GlobalQueues {
    public static BlockingQueue<String> symbolQueue = new LinkedBlockingDeque<>();
    public static BlockingQueue<HistoryInterval> historyIntervals = new LinkedBlockingDeque<>();
}
