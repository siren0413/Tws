package com.tws.iqfeed.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by admin on 2/9/2016.
 */
public class HistoryMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(HistoryMessageListener.class);

    @Autowired
    private HistorySocket historySocket;


    @Override
    public void onMessage(Message message) {
        try {
            String msg = ((TextMessage) message).getText();
            if (msg.contains("\\r\\n")) {
                msg = msg.replaceAll("\\r\\n","");
            }
            if(!msg.contains("\r\n")){
                msg = msg + "\r\n";
            }
            logger.info("send message: {}.", msg);
            historySocket.send(msg);
        } catch (JMSException e) {
            logger.error("unable to send message.", e);
        }
    }
}
