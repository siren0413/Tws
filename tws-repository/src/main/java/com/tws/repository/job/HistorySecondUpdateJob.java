package com.tws.repository.job;

import com.tws.repository.GlobalQueues;
import com.tws.repository.service.HistoryIntervalStoreService;
import com.tws.repository.service.HistoryIntervalUpdateService;
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

/**
 * Created by admin on 2/17/2016.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class HistorySecondUpdateJob extends QuartzJobBean implements StatefulJob {

    private static final Logger logger = LoggerFactory.getLogger(HistorySecondUpdateJob.class);

    private ZonedDateTime startDate;
    private int interval;
    private int maxDataPoints;
    private List<String> symbolList;
    private UpdateMode mode;

    @Autowired
    private HistoryIntervalUpdateService dbUpdateService;


    private void init(JobDataMap map, JobKey jobKey) {
        String intervalString = map.getString("interval");
        String startString = map.getString("startDate");
        String symbolString = map.getString("symbolList");
        String maxDataPointsString = map.getString("maxDataPoints");
        String updateModeString = map.getString("updateMode");

        symbolList = new LinkedList<>();
        StringTokenizer tokenizer = new StringTokenizer(symbolString, ",");
        while (tokenizer.hasMoreTokens()) {
            symbolList.add(tokenizer.nextToken());
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
        LocalDateTime localDateTime = LocalDateTime.parse(startString, dtf);
        startDate = localDateTime.atZone(ZoneId.of("America/New_York"));
        interval = NumberUtils.toInt(intervalString);
        maxDataPoints = NumberUtils.toInt(maxDataPointsString);
        mode = UpdateMode.valueOf(updateModeString);


        logger.debug("History interval update job init: ");
        logger.debug("job name and group: {}", jobKey);
        logger.debug("start: {}", startString);
        logger.debug("interval: {}", interval);
        logger.debug("max data points: {}", maxDataPoints);
        logger.debug("symbol list: {}", symbolList);
        logger.debug("update mode: {}", mode);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobKey jobKey = context.getJobDetail().getKey();
            logger.info("Start run job: {}", jobKey);

            JobDataMap map = context.getJobDetail().getJobDataMap();
            init(map, jobKey);

            for (String symbol : symbolList) {
                do {
                    Thread.sleep(100);
                }while (System.currentTimeMillis() - HistoryIntervalStoreService.lastUpdateTime < 500);
                dbUpdateService.update(map, symbol, interval, startDate.toInstant().toEpochMilli(), maxDataPoints);
            }
        } catch (Exception e) {
            JobExecutionException jobExecutionException = new JobExecutionException(e);
            jobExecutionException.setRefireImmediately(true);
            logger.error("[SEVERE] job execution error. will refire immediately", e);
            throw jobExecutionException;
        }
    }
}
