package com.tws.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.AmqpIOException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin on 2/13/2016.
 */
public class RabbitmqPublisher {

    private static final Logger logger = LoggerFactory.getLogger(RabbitmqPublisher.class);

    @Autowired
    private RabbitTemplate template;

    public void publish(String exchange, String routeKey, Object message){
        try {
            template.convertAndSend(exchange, routeKey, message, new MessagePostProcessor() {
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
                    return message;
                }
            });
        }catch (AmqpIOException | AmqpConnectException e){

        }
    }

}
