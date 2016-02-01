package com.tws.iqfeed.feed;

import com.tws.message.MessageListener;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by admin on 1/30/2016.
 */
public class HistoryMessageListener implements MessageListener {

    private HistoryFeed historyFeed;

    @Required
    public void setHistoryFeed(HistoryFeed historyFeed) {
        this.historyFeed = historyFeed;
    }

    @Override
    public void messageReceived(String msg) {
        historyFeed.send(msg);
    }
}
