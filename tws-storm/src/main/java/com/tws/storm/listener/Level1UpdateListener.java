package com.tws.storm.listener;

import com.tws.shared.common.Utils;
import com.tws.shared.iqfeed.model.Level1Update;
import com.tws.storm.spout.Level1UpdateSpout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
public class Level1UpdateListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(Level1UpdateListener.class);

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                Level1Update level1Update = Utils.getGson().fromJson(textMessage.getText(), Level1Update.class);
                Level1UpdateSpout.queue.offer(level1Update);
                return;
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        logger.error("Received unsupported message: {}", message);
    }
}
