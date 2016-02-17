package com.tws.iqfeed.listener;

import com.tws.iqfeed.netty.HistorySocket;
import com.tws.iqfeed.netty.Level1Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2/13/2016.
 */
public class Level1CommandListener {

    private static final Logger logger = LoggerFactory.getLogger(Level1CommandListener.class);

    @Autowired
    private Level1Socket level1Socket;

    public void onMessageReceived(Object message) {
        String msg = new String((byte[]) message);
        System.out.println(msg);
        if (msg.contains("\\r\\n")) {
            msg = msg.replace("\\r\\n", "");
        }
        if (!msg.contains("\r\n")) {
            msg = msg + "\r\n";
        }
        logger.debug("publish message: {}", msg);
        level1Socket.send(msg);
    }
}
