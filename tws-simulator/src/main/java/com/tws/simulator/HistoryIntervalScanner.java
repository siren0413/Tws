package com.tws.simulator;

import com.datastax.driver.mapping.Result;
import com.google.common.util.concurrent.ListenableFuture;
import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.cassandra.repo.HistoryIntervalRepository;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
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
    private int dataPoints;
    private JobDataMap map;

    private HistoryIntervalRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(HistoryIntervalSimulatorJob.class);

    public HistoryIntervalScanner(JobDataMap map, long startTime, long endTime, String symbol, int dataPoints, HistoryIntervalRepository repository) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.symbol = symbol;
        this.repository = repository;
        this.dataPoints = dataPoints;
        this.map = map;
    }

    @Override
    public void run() {

        Map<String, Object> wrapperMap =  map.getWrappedMap();
        Long lastEndTime = (Long) wrapperMap.get(symbol);
        if(lastEndTime != null){
            startTime = lastEndTime;
        }
        long thisEndTime = startTime;

        ListenableFuture<Result<HistoryIntervalDB>> listenableFuture = repository.getIntervalInTime(symbol, 1, startTime, dataPoints);
        try {
            Result<HistoryIntervalDB> result = listenableFuture.get(10000, TimeUnit.SECONDS);
            for (HistoryIntervalDB historyIntervalDB : result) {
                if (historyIntervalDB.getTime() < endTime) {
                    Global.dbQueue.put(historyIntervalDB);
                    thisEndTime = historyIntervalDB.getTime();
                }
            }
//            logger.info("symbol: {}, startTime: {}, endTime: {}", symbol, startTime, thisEndTime);
            wrapperMap.put(symbol,thisEndTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
