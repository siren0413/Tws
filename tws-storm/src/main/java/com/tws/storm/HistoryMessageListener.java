package com.tws.storm;

import com.tws.shared.iqfeed.decoder.HistoryIntervalMessageDecoder;
import com.tws.shared.iqfeed.decoder.HistoryTickMessageDecoder;
import com.tws.shared.iqfeed.model.HistoryInterval;
import com.tws.shared.iqfeed.model.HistoryTick;
import com.tws.zeromq.ZeromqMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by admin on 2/10/2016.
 */
public class HistoryMessageListener implements ZeromqMessageListener {

    private HistoryIntervalMessageDecoder historyIntervalMessageDecoder;
    public static BlockingQueue<HistoryInterval> queue = new LinkedBlockingDeque<>(10000);

    @Override
    public void onMessageReceived(String message) {
        HistoryInterval historyInterval = (HistoryInterval) historyIntervalMessageDecoder.decode(message);
        if (historyInterval != null) {
            queue.offer(historyInterval);
        }
    }

    @Required
    public void setHistoryIntervalMessageDecoder(HistoryIntervalMessageDecoder historyIntervalMessageDecoder) {
        this.historyIntervalMessageDecoder = historyIntervalMessageDecoder;
    }
}
