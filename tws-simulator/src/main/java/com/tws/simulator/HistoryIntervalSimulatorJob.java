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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
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

        // scan
        ExecutorService scannerExecutor = Executors.newFixedThreadPool(10);
        for (String symbol : symbolList) {
            scannerExecutor.submit(new HistoryIntervalScanner(map,startTime, endTime, symbol, dataPoints, repository));
        }

        // publish
        int num = 10;
        ExecutorService publisherExecutor = Executors.newFixedThreadPool(num);
        for (int i = 0; i < num; i++) {
            publisherExecutor.submit(new HistoryIntervalToLevel1UpdatePublisher(publisher, queue));
        }


        try {
            scannerExecutor.shutdown();
            scannerExecutor.awaitTermination(60, TimeUnit.SECONDS);

            if(Global.dbQueue.isEmpty()){
                publisherExecutor.shutdownNow();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
