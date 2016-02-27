package com.tws.storm.listener;


import com.tws.shared.common.Utils;
import com.tws.shared.iqfeed.model.Level1Summary;
import com.tws.shared.iqfeed.model.Level1Update;
import com.tws.storm.spout.Level1SummarySpout;
import com.tws.storm.spout.Level1UpdateSpout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Serializable;

/**
 * Created by admin on 2/13/2016.
 */
public class Level1SummaryListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(Level1SummaryListener.class);

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                Level1Summary level1Summary = Utils.getGson().fromJson(textMessage.getText(), Level1Summary.class);
                Level1SummarySpout.queue.offer(level1Summary);
                return;
            } catch (JMSException e) {
                logger.error("Unable to parse message: {}", message, e);
            }
        }
        logger.error("Received unsupported message: {}", message);
    }
}
