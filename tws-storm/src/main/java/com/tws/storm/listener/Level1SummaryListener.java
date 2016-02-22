package com.tws.storm.listener;


import com.tws.shared.iqfeed.model.Level1Summary;
import com.tws.shared.iqfeed.model.Level1Update;
import com.tws.storm.spout.Level1SummarySpout;
import com.tws.storm.spout.Level1UpdateSpout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;

/**
 * Created by admin on 2/13/2016.
 */
public class Level1SummaryListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(Level1SummaryListener.class);

    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                Serializable serializable = objectMessage.getObject();
                if (serializable instanceof Level1Summary) {
                    Level1SummarySpout.queue.offer((Level1Summary) serializable);
                    return;
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        logger.error("Received unsupported message: {}", message);
    }
}
