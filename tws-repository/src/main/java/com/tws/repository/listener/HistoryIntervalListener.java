package com.tws.repository.listener;

import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.cassandra.repo.HistoryIntervalRepository;
import com.tws.repository.GlobalQueues;
import com.tws.shared.iqfeed.model.HistoryInterval;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;

/**
 * Created by admin on 2/13/2016.
 */
public class HistoryIntervalListener {

    @Autowired
    private HistoryIntervalRepository historyIntervalRepository;

    public void onMessageReceived(HistoryInterval historyInterval) {
        HistoryIntervalDB historyIntervalDB = new HistoryIntervalDB();
        String[] parts = historyInterval.getRequestId().split(".");
        historyIntervalDB.setSymbol(parts[0]);
        historyIntervalDB.setInterval(NumberUtils.toInt(parts[1]));
        SimpleDateFormat sdf = new SimpleDateFormat("");
        historyIntervalDB.setTime();
        historyIntervalRepository.saveIntervalInTime();
        GlobalQueues.historyIntervals.offer(historyInterval);
    }
}
