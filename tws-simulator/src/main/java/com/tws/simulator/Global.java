package com.tws.simulator;

import com.tws.cassandra.model.HistoryIntervalDB;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by admin on 2/21/2016.
 */
public class Global {
    public static final PriorityBlockingQueue<HistoryIntervalDB> dbQueue = new PriorityBlockingQueue<>(999999, new Comparator<HistoryIntervalDB>() {
        @Override
        public int compare(HistoryIntervalDB o1, HistoryIntervalDB o2) {
            return Long.valueOf(o1.getTime()).compareTo(o2.getTime());
        }
    });
}
