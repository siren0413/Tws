package com.tws.simulator;

import com.tws.cassandra.model.HistoryIntervalDB;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by admin on 2/21/2016.
 */
public class Global {
    public static final PriorityBlockingQueue<HistoryIntervalDB> dbQueue = new PriorityBlockingQueue<>(100000);
}
