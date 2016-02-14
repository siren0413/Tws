package com.tws.storm.listener;

import com.tws.shared.iqfeed.model.Level1Update;

/**
 * Created by admin on 2/13/2016.
 */
public class Level1UpdateListener{

    public void onMessageReceived(Level1Update level1Update) {
        System.out.println(level1Update);
    }
}
