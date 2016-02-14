package com.tws.storm.listener;

import com.tws.shared.iqfeed.model.Level1Timestamp;

/**
 * Created by admin on 2/13/2016.
 */
public class Level1TimeListener {

    public void onMessageReceived(Level1Timestamp level1Timestamp) {
        System.out.println(level1Timestamp);
    }
}
