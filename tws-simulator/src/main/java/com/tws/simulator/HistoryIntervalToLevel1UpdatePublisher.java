package com.tws.simulator;

import com.tws.activemq.ActivemqPublisher;
import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.shared.iqfeed.model.Level1Update;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by admin on 2/21/2016.
 */
public class HistoryIntervalToLevel1UpdatePublisher implements Runnable {

    private ActivemqPublisher publisher;
    private PriorityBlockingQueue<HistoryIntervalDB> queue = new PriorityBlockingQueue<>();

    public HistoryIntervalToLevel1UpdatePublisher(ActivemqPublisher publisher, PriorityBlockingQueue<HistoryIntervalDB> queue) {
        this.publisher = publisher;
        this.queue = queue;
    }

    private Level1Update convertHistoryIntervalDBToLevel1Update(HistoryIntervalDB historyIntervalDB) {
        Level1Update level1Update = new Level1Update();
        level1Update.setSymbol(historyIntervalDB.getSymbol());
        level1Update.setBid(historyIntervalDB.getClose());
        level1Update.setBidSize(0);
        level1Update.setBidTime("");
        level1Update.setAsk(historyIntervalDB.getClose());
        level1Update.setAskSize(0);
        level1Update.setAskTime("");
        level1Update.setLast(historyIntervalDB.getClose());
        level1Update.setLastSize(0);
        level1Update.setLastTime("");
        level1Update.setTotalVolume(historyIntervalDB.getTotalVolume());
        level1Update.setOpen(historyIntervalDB.getOpen());
        level1Update.setLow(historyIntervalDB.getLow());
        level1Update.setHigh(historyIntervalDB.getHigh());
        return level1Update;
    }

    @Override
    public void run() {
        while (!queue.isEmpty()) {
            try {
                HistoryIntervalDB historyIntervalDB = queue.take();
                Level1Update level1Update = convertHistoryIntervalDBToLevel1Update(historyIntervalDB);
                publisher.publish("Q", level1Update);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
