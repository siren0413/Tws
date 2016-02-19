package com.tws.iqfeed.listener;

import com.tws.iqfeed.netty.HistorySocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by admin on 2/13/2016.
 */
public class HistoryCommandListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(HistoryCommandListener.class);

    @Autowired
    private HistorySocket historySocket;

    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage){
            TextMessage textMessage = (TextMessage) message;
            try {
                String msg = textMessage.getText();
                if (msg.contains("\\r\\n")) {
                    msg = msg.replace("\\r\\n", "");
                }
                if (!msg.contains("\r\n")) {
                    msg = msg + "\r\n";
                }
                logger.debug("publish message: {}", msg);
                historySocket.send(msg);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
