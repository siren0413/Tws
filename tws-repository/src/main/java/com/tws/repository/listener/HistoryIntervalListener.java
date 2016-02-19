package com.tws.repository.listener;

import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.cassandra.repo.HistoryIntervalRepository;
import com.tws.repository.GlobalQueues;
import com.tws.shared.iqfeed.model.HistoryInterval;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by admin on 2/13/2016.
 */
public class HistoryIntervalListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(HistoryIntervalListener.class);

    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                Serializable serializable = objectMessage.getObject();
                if (serializable instanceof HistoryInterval) {
                    GlobalQueues.historyIntervals.offer((HistoryInterval) serializable);
                    return;
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        logger.error("Received unsupported message: {}", message);
    }
}
