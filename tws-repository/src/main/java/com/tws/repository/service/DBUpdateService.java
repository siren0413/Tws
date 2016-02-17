package com.tws.repository.service;

import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.cassandra.repo.HistoryIntervalRepository;
import com.tws.rabbitmq.RabbitmqPublisher;
import com.tws.shared.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by admin on 2/16/2016.
 */

public class DBUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(DBUpdateService.class);

    @Autowired
    private HistoryIntervalRepository historyIntervalRepository;

    @Autowired
    private HistoryCommandService historyCommandService;

    @Autowired
    private RabbitmqPublisher publisher;

    public void update(String symbol, int interval, long startTime) {
        HistoryIntervalDB historyIntervalDB = historyIntervalRepository.getMostRecentRecordInTime(symbol, interval);
        logger.info("query getMostRecentRecordInTime, result: {}", historyIntervalDB);
        long mostRecentTime;
        if (historyIntervalDB == null) {
            mostRecentTime = startTime;
        } else {
            mostRecentTime = historyIntervalDB.getTime();
        }

        String cmd = historyCommandService.getCommand(symbol, interval, mostRecentTime);
        logger.info("send history command: {}", cmd);

        publisher.publish(Constants.HISTORY_EXCHANGE, Constants.HISTORY_COMMAND_ROUTEKEY_PREFIX, cmd);
    }
}
