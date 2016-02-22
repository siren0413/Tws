package com.tws.simulator;

import com.datastax.driver.mapping.Result;
import com.google.common.util.concurrent.ListenableFuture;
import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.cassandra.repo.HistoryIntervalRepository;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by admin on 2/21/2016.
 */
public class HistoryIntervalScanner implements Runnable {
    private long startTime;
    private long endTime;
    private String symbol;

    private HistoryIntervalRepository repository;

    public HistoryIntervalScanner(long startTime, long endTime, String symbol, HistoryIntervalRepository repository) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.symbol = symbol;
        this.repository = repository;
    }

    @Override
    public void run() {
        ListenableFuture<Result<HistoryIntervalDB>> listenableFuture;
        if (endTime == -1) {
            listenableFuture = repository.getIntervalInTime(symbol, 1, startTime);
        } else {
            listenableFuture = repository.getIntervalInTime(symbol, 1, startTime, endTime);
        }
        try {
            Result<HistoryIntervalDB> result = listenableFuture.get(10000, TimeUnit.SECONDS);
            for (HistoryIntervalDB historyIntervalDB : result) {
                Global.dbQueue.put(historyIntervalDB);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
