package com.tws.simulator;

import com.tws.activemq.ActivemqPublisher;
import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.shared.iqfeed.model.Level1Update;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by admin on 2/21/2016.
 */
public class HistoryIntervalToLevel1UpdatePublisher implements Runnable {

    @Autowired
    private ActivemqPublisher publisher;
    private PriorityBlockingQueue<HistoryIntervalDB> queue;

    public HistoryIntervalToLevel1UpdatePublisher(ActivemqPublisher publisher, PriorityBlockingQueue<HistoryIntervalDB> queue) {
        this.publisher = publisher;
        this.queue = queue;
    }

    private Level1Update convertHistoryIntervalDBToLevel1Update(HistoryIntervalDB historyIntervalDB) {
        Level1Update level1Update = new Level1Update();

        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(historyIntervalDB.getTime()), ZoneId.of("America/New_York"));
        String timestamp = zonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS"));
        String dateStr = zonedDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        String localDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));

        level1Update.setSymbol(historyIntervalDB.getSymbol());
        level1Update.setBid(historyIntervalDB.getClose());
        level1Update.setBidSize(0);
        level1Update.setBidTime(timestamp);
        level1Update.setAsk(historyIntervalDB.getClose());
        level1Update.setAskSize(0);
        level1Update.setAskTime(timestamp);
        level1Update.setLast(historyIntervalDB.getClose());
        level1Update.setLastSize(0);
        level1Update.setLastTime(timestamp);
        level1Update.setExtendedTrade(historyIntervalDB.getClose());
        level1Update.setExtendedTradeTime(timestamp);
        level1Update.setExtendedTradeSize(0);
        level1Update.setExtendedTradeDate(dateStr);
        level1Update.setTotalVolume(historyIntervalDB.getTotalVolume());
        level1Update.setOpen(historyIntervalDB.getOpen());
        level1Update.setLow(historyIntervalDB.getLow());
        level1Update.setHigh(historyIntervalDB.getHigh());
        level1Update.setMessageContent("");
        level1Update.setDelay(0);
        level1Update.setExchangeId("");
        level1Update.setLocalDateTime(localDateTime);
        return level1Update;
    }

    @Override
    public void run() {
        while (!Global.dbQueue.isEmpty()) {
            HistoryIntervalDB historyIntervalDB = Global.dbQueue.poll();
            if (historyIntervalDB != null) {
                Level1Update level1Update = convertHistoryIntervalDBToLevel1Update(historyIntervalDB);
                publisher.publish("Q", level1Update);
            }
        }
    }
}
