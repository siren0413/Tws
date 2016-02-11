package com.tws.storm;

import com.tws.shared.iqfeed.decoder.HistoryTickMessageDecoder;
import com.tws.shared.iqfeed.model.HistoryTick;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by admin on 2/10/2016.
 */
public class HistoryMessageListener implements MessageListener {

    @Autowired
    private HistoryTickMessageDecoder historyTickMessageDecoder;

    public void onMessage(Message message) {
        try {
            String msg = ((TextMessage)message).getText();
            HistoryTick historyTick = (HistoryTick) historyTickMessageDecoder.decode(msg);
            System.out.println(historyTick);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
