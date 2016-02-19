package com.tws.repository.service;

import com.tws.activemq.ActivemqPublisher;
import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.cassandra.repo.HistoryIntervalRepository;
import com.tws.shared.Constants;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
        HistoryIntervalDB historyIntervalDB = historyIntervalRepository.getMostRecentRecordInTime(symbol, interval);
        logger.info("query getMostRecentRecordInTime, result: {}", historyIntervalDB);
        long mostRecentTime;
        if (historyIntervalDB == null) {
            mostRecentTime = startTime;
        } else {
            mostRecentTime = Long.max(historyIntervalDB.getTime(), startTime);
        }

        Instant instant = Instant.ofEpochMilli(mostRecentTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
        ZonedDateTime startZonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("America/New_York"));
        startZonedDateTime = startZonedDateTime.minusSeconds(interval);
        String beginTime = startZonedDateTime.format(formatter);

        ZonedDateTime endZonedDateTime = startZonedDateTime.plusSeconds(interval * maxDataPoints);

        Map<String, Object> wrappedMap = map.getWrappedMap();
        if (wrappedMap.get(symbol) == null) {
            wrappedMap.put(symbol, new HashMap<String, ZonedDateTime>());
        }
        Map<String, ZonedDateTime> innerMap = (Map<String, ZonedDateTime>) wrappedMap.get(symbol);
        ZonedDateTime lastStartTime = innerMap.get("lastStartTime");
        if (lastStartTime == null || !lastStartTime.equals(startZonedDateTime)) {
            innerMap.put("lastStartTime", startZonedDateTime);
            innerMap.put("lastEndTime", endZonedDateTime);
        } else {
            endZonedDateTime = innerMap.get("lastEndTime");
            if (endZonedDateTime != null) {
                endZonedDateTime = endZonedDateTime.plusSeconds(interval * maxDataPoints);
            } else {
                endZonedDateTime = startZonedDateTime.plusSeconds(interval * maxDataPoints);
            }
            innerMap.put("lastEndTime", endZonedDateTime);
        }

        String endTime = endZonedDateTime.format(formatter);

        if (endZonedDateTime.toEpochSecond() - startZonedDateTime.toEpochSecond() > interval * maxDataPoints * 100) {
            logger.error("Unable to get data for symbol: [{}]. reset retry counter", symbol);
            wrappedMap.remove(symbol);
        }

        String cmd = "";
        if (interval < 86400) {
            cmd = historyCommandService.getTickIntervalCmd(symbol, interval, beginTime, endTime);
        } else {
            cmd = historyCommandService.getDayIntervalCmd(symbol, interval / 86400, beginTime, endTime);
        }
        logger.info("send history command: {}", cmd);
        publisher.publish(Constants.HISTORY_COMMAND_ROUTEKEY_PREFIX, cmd);
    }
}
