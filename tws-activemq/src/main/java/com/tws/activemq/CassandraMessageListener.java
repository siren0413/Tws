package com.tws.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by yijunmao on 1/17/16.
 */
public class CassandraMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(CassandraMessageListener.class);
    private LinkedBlockingQueue<TextMessage> linkedBlockingQueue = new LinkedBlockingQueue<TextMessage>(1024);

    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            boolean result = linkedBlockingQueue.offer(textMessage);
            if (!result) {
                logger.error("blocking queue offer failed. queue size = [{}]", linkedBlockingQueue.size());
            }
        } else {
            try {
                logger.error("unknown message type [{}]", message.getJMSType());
            } catch (JMSException e) {
                logger.error("error get jms type.", e);
            }
        }
    }

    public TextMessage take() {
        try {
            return linkedBlockingQueue.take();
        } catch (InterruptedException e) {
            logger.error("error take element from queue.", e);
        }
        return null;
    }
}
