package com.tws.activemq;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
        sender.send(tickQuote, "tickquote sent.");
    }
}
