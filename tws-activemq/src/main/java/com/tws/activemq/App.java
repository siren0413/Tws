package com.tws.activemq;

import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.command.Message;
import org.apache.activemq.network.jms.SimpleJmsMessageConvertor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessagingMessageConverter;

import javax.jms.Topic;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        ApplicationContext context = new ClassPathXmlApplicationContext("activemq-spring.xml");
        TwsMessageSender sender = (TwsMessageSender)context.getBean("twsMessageSender");
        Topic tickQuote = new ActiveMQTopic("tickQuote");
        sender.send(tickQuote, "tickquote sent.",0);


        JmsTemplate template = (JmsTemplate) context.getBean("myJmsTemplate");
        JmsMessagingTemplate t = new JmsMessagingTemplate();
    }
}
