package com.tws.storm.listener;

import com.tws.shared.iqfeed.model.Level1Update;

/**
 * Created by admin on 2/13/2016.
 */
public class Level1UpdateListener implements Listener{

    @Override
    public void onMessageReceived(Object obj) {
        Level1Update level1Update = (Level1Update) obj;
        System.out.println(level1Update);
    }
}
