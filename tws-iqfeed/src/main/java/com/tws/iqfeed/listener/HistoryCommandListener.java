package com.tws.iqfeed.listener;

import com.tws.iqfeed.netty.HistorySocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2/13/2016.
 */
public class HistoryCommandListener {

    private static final Logger logger = LoggerFactory.getLogger(HistoryCommandListener.class);

    @Autowired
    private HistorySocket historySocket;

    public void onMessageReceived(Object message) {
        String msg;
        if(message instanceof byte[]) {
            msg = new String((byte[]) message);
        }else{
            msg = (String)message;
        }
        System.out.println(msg);
        if (msg.contains("\\r\\n")) {
            msg = msg.replace("\\r\\n", "");
        }
        if (!msg.contains("\r\n")) {
            msg = msg + "\r\n";
        }
        logger.debug("publish message: {}", msg);
        historySocket.send(msg);
    }
}
