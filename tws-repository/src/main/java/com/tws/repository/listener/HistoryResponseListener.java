package com.tws.repository.listener;

import com.google.common.base.Splitter;
import com.tws.repository.GlobalQueues;
import com.tws.shared.iqfeed.model.HistoryInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2/20/2016.
 */
public class HistoryResponseListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(HistoryResponseListener.class);

    @Override
    public void onMessage(Message message) {

        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                Serializable serializable = objectMessage.getObject();
                if (serializable instanceof List) {
                    List<String> list = (List<String>) serializable;
                    String requestorId = list.get(0);
                    Map<String,String> map = GlobalQueues.responseCache.asMap();
                    if(map.containsKey(requestorId)){
                        map.remove(requestorId);
                        logger.info("Received response message for key: {}", requestorId);
                    }else{
                        logger.error("requestor id: [{}] not found in map.", requestorId);
                    }
                    return;
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        logger.error("Received unsupported message: {}", message);
    }
}
