package com.tws.storm;

import com.tws.shared.iqfeed.decoder.HistoryTickMessageDecoder;
import com.tws.shared.iqfeed.model.HistoryTick;
import com.tws.zeromq.ZeromqMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by admin on 2/10/2016.
 */
public class HistoryMessageListener implements ZeromqMessageListener {

    private HistoryTickMessageDecoder historyTickMessageDecoder;

    @Override
    public void onMessageReceived(String message) {
        HistoryTick historyTick = (HistoryTick) historyTickMessageDecoder.decode(message);
//        System.out.println(historyTick);
    }

    @Required
    public void setHistoryTickMessageDecoder(HistoryTickMessageDecoder historyTickMessageDecoder) {
        this.historyTickMessageDecoder = historyTickMessageDecoder;
    }
}
