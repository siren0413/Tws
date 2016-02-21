package com.tws.repository.job;

import com.tws.repository.service.HistoryIntervalUpdateService;
import com.tws.repository.service.HistoryRealTimeUpdateService;
import com.tws.repository.service.UpdateMode;
import org.apache.commons.lang3.math.NumberUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by admin on 2/17/2016.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class HistoryRealTimeUpdateJob extends QuartzJobBean implements StatefulJob {

    private static final Logger logger = LoggerFactory.getLogger(HistoryRealTimeUpdateJob.class);

    private int interval;
    private int maxDataPoints;
    private List<String> symbolList;
    private BlockingQueue<String> queue = new LinkedBlockingDeque<>();

    @Autowired
    private HistoryRealTimeUpdateService dbUpdateService;


    private void init(JobDataMap map, JobKey jobKey) {
        String intervalString = map.getString("interval");
        String symbolString = map.getString("symbolList");
        String maxDataPointsString = map.getString("maxDataPoints");

        symbolList = new LinkedList<>();
        StringTokenizer tokenizer = new StringTokenizer(symbolString, ",");
        while (tokenizer.hasMoreTokens()) {
            try {
                queue.put(tokenizer.nextToken());
            } catch (InterruptedException e) {
                logger.error("error add symbol to queue.", e);
                return;
            }
        }

        interval = NumberUtils.toInt(intervalString);
        maxDataPoints = NumberUtils.toInt(maxDataPointsString);

        logger.info("History interval update job init: ");
        logger.info("job name and group: {}", jobKey);
        logger.info("interval: {}", interval);
        logger.info("max data points: {}", maxDataPoints);
        logger.info("symbol list: {}", symbolList);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobKey jobKey = context.getJobDetail().getKey();
            logger.info("Start run job: {}", jobKey);

            JobDataMap map = context.getJobDetail().getJobDataMap();
            init(map, jobKey);

            ExecutorService executor = Executors.newFixedThreadPool(20);

            while (true) {
                String symbol = queue.take();
                executor.submit(new Worker(symbol, map));
            }

        } catch (Exception e) {
            JobExecutionException jobExecutionException = new JobExecutionException(e);
            jobExecutionException.setRefireImmediately(true);
            logger.error("[SEVERE] job execution error. will refire immediately", e);
            throw jobExecutionException;
        }
    }

    class Worker implements Runnable {

        private String symbol;
        private JobDataMap map;

        public Worker(String symbol, JobDataMap map) {
            this.symbol = symbol;
            this.map = map;
        }

        @Override
        public void run() {
            try {
                dbUpdateService.update(map, symbol, interval,  maxDataPoints);
            } catch (Exception e) {
                logger.error("[SEVERE] error update, symbol: {}, interval: {} ", symbol, interval);
            } finally {
                try {
                    queue.put(symbol);
                } catch (InterruptedException e) {
                    throw new RuntimeException("error add symbol to queue.", e);
                }
            }
        }
    }
}
