package com.tws.repository.job;

import com.tws.repository.GlobalQueues;
import com.tws.repository.service.HistoryIntervalUpdateService;
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

    @Autowired
    private HistoryIntervalUpdateService dbUpdateService;


    private void init(JobDataMap map) {
        String intervalString = map.getString("interval");
        String startString = map.getString("startDate");
        String symbolString = map.getString("symbolList");
        String maxDataPointsString = map.getString("maxDataPoints");

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


        logger.info("History Second interval update job init: ");
        logger.info("start: {}", startString);
        logger.info("interval: {}", interval);
        logger.info("max data points: {}", maxDataPoints);
        logger.info("symbol list: {}", symbolList);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (!GlobalQueues.historyIntervals.isEmpty()) {
            return;
        }
        JobKey jobKey = context.getJobDetail().getKey();
        logger.info("start run job: {}", jobKey);

        JobDataMap map = context.getJobDetail().getJobDataMap();
        init(map);

        for (String symbol : symbolList) {
            dbUpdateService.update(map, symbol, interval, startDate.toInstant().toEpochMilli(), maxDataPoints);
        }
    }
}
