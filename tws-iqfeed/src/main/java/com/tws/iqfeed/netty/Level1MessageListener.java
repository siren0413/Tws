package com.tws.iqfeed.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by admin on 2/2/2016.
 */
public class Level1MessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(Level1MessageListener.class);

    @Autowired
    private Level1Socket level1Socket;

    @Override
    public void onMessage(Message message) {
        try {
            String msg = ((TextMessage) message).getText();
            if (msg.contains("\\r\\n")) {
                msg = msg.replace("\\r\\n","");
            }
            if(!msg.contains("\r\n")){
                msg = msg + "\r\n";
            }
            logger.debug("send message: {}", msg);
            level1Socket.send(msg);
        } catch (JMSException e) {
            logger.error("unable to send message.", e);
        }
    }
}
