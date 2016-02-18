package com.tws.repository.listener;

import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.cassandra.repo.HistoryIntervalRepository;
import com.tws.repository.GlobalQueues;
import com.tws.shared.iqfeed.model.HistoryInterval;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by admin on 2/13/2016.
 */
public class HistoryIntervalListener {

    public void onMessageReceived(HistoryInterval historyInterval) {
        GlobalQueues.historyIntervals.offer(historyInterval);
    }
}
