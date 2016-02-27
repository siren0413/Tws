package com.tws.repository.service;

import com.tws.activemq.ActivemqPublisher;
import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.cassandra.repo.HistoryIntervalRepository;
import com.tws.repository.GlobalQueues;
import com.tws.shared.Constants;
import com.tws.shared.common.TimeUtils;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


/**
 * Created by admin on 2/16/2016.
 */

public class HistoryIntervalUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(HistoryIntervalUpdateService.class);

    @Autowired
    private HistoryIntervalRepository historyIntervalRepository;

    @Autowired
    private HistoryCommandService historyCommandService;

    @Autowired
    private ActivemqPublisher publisher;

    public void update(JobDataMap map, String symbol, int interval, long startTime, int maxDataPoints) {


//        HistoryIntervalDB historyIntervalDB = historyIntervalRepository.getMostRecentRecordInTime(symbol, interval);
//        logger.info("query getMostRecentRecordInTime, result: {}", historyIntervalDB);
//        long mostRecentTime;
//        if (historyIntervalDB == null) {
//            mostRecentTime = startTime;
//        } else {
//            mostRecentTime = Long.max(historyIntervalDB.getTime(), startTime);
//        }

        // determine start time
        ZonedDateTime startZonedDateTime;
        Map<String, Object> wrapperMap = map.getWrappedMap();
        ZonedDateTime lastEndZonedDateTime = (ZonedDateTime) wrapperMap.get(symbol);
        if (lastEndZonedDateTime == null) {
            Instant instant = Instant.ofEpochMilli(startTime);
            startZonedDateTime = ZonedDateTime.ofInstant(instant, TimeUtils.ZONE_EST);
        } else {
            startZonedDateTime = lastEndZonedDateTime;
        }

        // adjust start time to skip 20pm-4am and SAT-SUN
        startZonedDateTime = adjustStartTime(startZonedDateTime);

        // determine end time
        startZonedDateTime = startZonedDateTime.minusSeconds(interval);
        ZonedDateTime endZonedDateTime = startZonedDateTime.plusSeconds(interval * maxDataPoints);


        String cmd = "";
        if (interval < 86400) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
            String beginTime = startZonedDateTime.format(formatter);
            String endTime = endZonedDateTime.format(formatter);
            cmd = historyCommandService.getTickIntervalCmd(symbol, interval, beginTime, endTime);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String beginTime = startZonedDateTime.format(formatter);
            String endTime = endZonedDateTime.format(formatter);
            cmd = historyCommandService.getDayIntervalCmd(symbol, interval, beginTime, endTime);
        }
        logger.info("send history command: {}", cmd);
        String requestId = String.join(".", symbol, String.valueOf(interval));
        Map<String, String> responseMap = GlobalQueues.responseCache.asMap();
        responseMap.put(requestId, "");
        publisher.publish(Constants.HISTORY_COMMAND_ROUTEKEY_PREFIX, cmd);

        // only wait for 30 seconds for the response
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 30000) {
            if (responseMap.get(requestId) == null) {
                wrapperMap.put(symbol, endZonedDateTime);
                return;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private ZonedDateTime adjustStartTime(ZonedDateTime startZonedDateTime) {
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

            if(startZonedDateTime.isAfter(ZonedDateTime.now(TimeUtils.ZONE_EST))){
                startZonedDateTime = ZonedDateTime.now(TimeUtils.ZONE_EST);
                break;
            }
        }
        return startZonedDateTime;
    }

}
