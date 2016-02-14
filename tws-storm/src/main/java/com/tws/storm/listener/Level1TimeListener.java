package com.tws.storm.listener;

import com.tws.shared.iqfeed.model.Level1Timestamp;

/**
 * Created by admin on 2/13/2016.
 */
public class Level1TimeListener implements Listener{

    @Override
    public void onMessageReceived(Object obj) {
        Level1Timestamp level1Timestamp = (Level1Timestamp) obj;
        System.out.println(level1Timestamp);
    }
}
