package com.tws.storm;

import com.tws.shared.iqfeed.model.HistoryTick;

import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
public class HistoryListener {

    public void onMessageReceived(HistoryTick historyTick){
        System.out.println(historyTick);
    }
}
