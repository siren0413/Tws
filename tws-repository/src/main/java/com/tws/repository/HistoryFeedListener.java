package com.tws.repository;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by admin on 2/2/2016.
 */
public class HistoryFeedListener implements MessageListener {

    @Autowired
    private HistoryTickRepository repository;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    @Override
    public void onMessage(Message message) {
        try {
            String text = ((TextMessage) message).getText();
            Splitter splitter = Splitter.on(System.getProperty("line.separator")).trimResults().omitEmptyStrings();
            Splitter lineSplitter = Splitter.on(",").trimResults().omitEmptyStrings();
            for (String line : splitter.split(text)) {
                HistoryTick historyTick = parse(line, lineSplitter);
                if (historyTick != null) {
                    repository.save(historyTick);
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private HistoryTick parse(String line, Splitter splitter) {
        Iterable<String> iter = splitter.split(line);
        List<String> list = Lists.newLinkedList(iter);
        if (list.size() < 11) {
            System.out.println("field missing for history feed: " + line);
            return null;
        }
        HistoryTick historyTick = new HistoryTick();
        historyTick.setId(list.get(0));
        historyTick.setTimestamp(list.get(1));
        historyTick.setLast(Float.parseFloat(list.get(2)));
        historyTick.setLastSize(Integer.parseInt(list.get(3)));
        historyTick.setTotalVolume(Integer.parseInt(list.get(4)));
        historyTick.setBid(Float.parseFloat(list.get(5)));
        historyTick.setAsk(Float.parseFloat(list.get(6)));
        historyTick.setTickId(Integer.parseInt(list.get(7)));
        historyTick.setBasis(list.get(8));
        historyTick.setMarket(Integer.parseInt(list.get(9)));
        historyTick.setCondition(list.get(10));
        return historyTick;
    }
}
