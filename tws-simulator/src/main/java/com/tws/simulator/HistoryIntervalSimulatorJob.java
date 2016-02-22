package com.tws.simulator;

import com.tws.activemq.ActivemqPublisher;
import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.cassandra.repo.HistoryIntervalRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2/21/2016.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class HistoryIntervalSimulatorJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(HistoryIntervalSimulatorJob.class);

    private long startTime;
    private long endTime;
    private List<String> symbolList;
    private int dataPoints;
    private final PriorityBlockingQueue<HistoryIntervalDB> queue = new PriorityBlockingQueue<>();

    @Autowired
    private HistoryIntervalRepository repository;

    @Autowired
    private ActivemqPublisher publisher;


    private void init(JobDataMap map, JobKey jobKey) {
        String startTimeString = map.getString("startTime");
        String endTimeString = map.getString("endTime");
        String symbolString = map.getString("symbolList");
        String dataPointsString = map.getString("dataPoints");

        dataPoints = NumberUtils.toInt(dataPointsString);
        symbolList = new LinkedList<>();
        StringTokenizer tokenizer = new StringTokenizer(symbolString, ",");
        while (tokenizer.hasMoreTokens()) {
            symbolList.add(tokenizer.nextToken());
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
        LocalDateTime localDateTime = LocalDateTime.parse(startTimeString, dtf);
        ZonedDateTime startDateTime = localDateTime.atZone(ZoneId.of("America/New_York"));
        startTime = startDateTime.toInstant().toEpochMilli();
        if (endTimeString == null) {
            endTime = -1;
        } else {
            LocalDateTime endLocalDateTime = LocalDateTime.parse(endTimeString, dtf);
            ZonedDateTime endDateTime = endLocalDateTime.atZone(ZoneId.of("America/New_York"));
            endTime = endDateTime.toInstant().toEpochMilli();
        }

        logger.info("History interval simulate job init: ");
        logger.info("job name and group: {}", jobKey);
        logger.info("start: {}", startTimeString);
        logger.info("end: {}", endTimeString);
        logger.info("symbol list: {}", symbolList);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        logger.info("Start run job: {}", jobKey);

        JobDataMap map = context.getJobDetail().getJobDataMap();
        init(map, jobKey);

        Map<String, Object> wrapperMap = map.getWrappedMap();
        Long lastEndTime = (Long) wrapperMap.get("lastEndTime");
        if (lastEndTime != null) {
            startTime = lastEndTime;
        }

        // adjust start time
        startTime = adjustStartTime(startTime);

        // calculate this job's end time
        long thisJobEndTime = startTime + dataPoints * 1000;

        if(thisJobEndTime > endTime){
            thisJobEndTime = endTime;
        }

        logger.info("history interval start: {}, end: {}", startTime, thisJobEndTime);

        // scan
        ExecutorService scannerExecutor = Executors.newFixedThreadPool(10);
        for (String symbol : symbolList) {
            scannerExecutor.submit(new HistoryIntervalScanner(startTime, thisJobEndTime, symbol, repository));
        }

        try {
            scannerExecutor.shutdown();
            scannerExecutor.awaitTermination(60, TimeUnit.SECONDS);

            // publish
            int num = 1;
            ExecutorService publisherExecutor = Executors.newFixedThreadPool(num);
            for (int i = 0; i < num; i++) {
                publisherExecutor.submit(new HistoryIntervalToLevel1UpdatePublisher(publisher, queue));
            }

            publisherExecutor.shutdown();
            publisherExecutor.awaitTermination(60, TimeUnit.SECONDS);

            wrapperMap.put("lastEndTime", thisJobEndTime);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private long adjustStartTime(long startTime) {

        Instant instant = Instant.ofEpochMilli(startTime);
        ZonedDateTime startZonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("America/New_York"));

        boolean modified = true;
        while (modified) {
            modified = false;
            if (startZonedDateTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
                startZonedDateTime = startZonedDateTime.plusDays(1);
                modified = true;
            }
            if (startZonedDateTime.getHour() >= 20 || startZonedDateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
                startZonedDateTime = startZonedDateTime.plusDays(1).withHour(4).withMinute(0).withSecond(0).withNano(0);
                modified = true;
            }

            if (startZonedDateTime.getHour() < 4) {
                startZonedDateTime = startZonedDateTime.withHour(4).withMinute(0).withSecond(0).withNano(0);
                modified = true;
            }
        }
        return startZonedDateTime.toInstant().toEpochMilli();
    }

}
