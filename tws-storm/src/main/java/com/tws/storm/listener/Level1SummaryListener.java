package com.tws.storm.listener;

import com.tws.shared.iqfeed.model.Level1Summary;

/**
 * Created by admin on 2/13/2016.
 */
public class Level1SummaryListener {

    public void onMessageReceived(Level1Summary level1Summary) {
        System.out.println(level1Summary);
    }
}
