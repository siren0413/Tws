package com.tws.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Created by chris on 1/17/16.
 */

@Service
public class TwsMessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(final Destination dest, final String text, final int type) {

        this.jmsTemplate.send(dest, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message msg = session.createTextMessage(text);
                msg.setIntProperty("type", type);
                return msg;
            }
        });
    }
}
