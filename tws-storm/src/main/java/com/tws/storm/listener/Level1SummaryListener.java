package com.tws.storm.listener;

import com.tws.shared.iqfeed.model.Level1Summary;

/**
 * Created by admin on 2/13/2016.
 */
public class Level1SummaryListener implements Listener{

    @Override
    public void onMessageReceived(Object obj) {
        Level1Summary level1Summary = (Level1Summary) obj;
        System.out.println(level1Summary);
    }
}
