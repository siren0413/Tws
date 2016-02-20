package com.tws.repository.service;

import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.cassandra.repo.HistoryIntervalRepository;
import com.tws.repository.GlobalQueues;
import com.tws.shared.iqfeed.model.HistoryInterval;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by admin on 2/17/2016.
 */
public class HistoryIntervalStoreService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(HistoryIntervalStoreService.class);
    public static long lastUpdateTime = 0;

    @Autowired
    private HistoryIntervalRepository historyIntervalRepository;

    private ExecutorService executor;

    @Override
    public void afterPropertiesSet() throws Exception {
        int poolSize = 10;
        executor = Executors.newFixedThreadPool(poolSize);
        for(int i = 0; i < poolSize; i++){
            executor.submit(new Worker());
        }
        logger.info("History interval store service started.");
    }

    class Worker implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    HistoryInterval historyInterval = GlobalQueues.historyIntervals.take();
                    lastUpdateTime = System.currentTimeMillis();
                    if (historyInterval != null) {
                        save(historyInterval);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void save(HistoryInterval historyInterval) {
            HistoryIntervalDB historyIntervalDB = new HistoryIntervalDB();
            String[] parts = historyInterval.getRequestId().split("\\.");
            historyIntervalDB.setSymbol(parts[0]);
            historyIntervalDB.setInterval(NumberUtils.toInt(parts[1]));
            if (historyInterval.getTimestamp() == null) {
                return;
            }
            ZonedDateTime zonedDateTime;
            try {
                LocalDateTime localDateTime = LocalDateTime.parse(historyInterval.getTimestamp().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                zonedDateTime = localDateTime.atZone(ZoneId.of("America/New_York"));
            } catch (DateTimeParseException e) {
                try {
                    LocalDate localDate = LocalDate.parse(historyInterval.getTimestamp().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    zonedDateTime = localDate.atStartOfDay(ZoneId.of("America/New_York"));
                } catch (DateTimeParseException e1) {
                    logger.error("unable to parse date: {}", historyInterval.getTimestamp(),e);
                    return;
                }
            }
            historyIntervalDB.setTime(zonedDateTime.toInstant().toEpochMilli());
            historyIntervalDB.setTimestamp(historyInterval.getTimestamp());
            historyIntervalDB.setClose(historyInterval.getClose());
            historyIntervalDB.setHigh(historyInterval.getHigh());
            historyIntervalDB.setLow(historyInterval.getLow());
            historyIntervalDB.setNumTrades(historyInterval.getNumTrades());
            historyIntervalDB.setOpen(historyInterval.getOpen());
            historyIntervalDB.setPeriodVolume(historyInterval.getPeriodVolume());
            historyIntervalDB.setTotalVolume(historyInterval.getTotalVolume());
            historyIntervalRepository.saveIntervalInTime(historyIntervalDB);
        }
    }
}
