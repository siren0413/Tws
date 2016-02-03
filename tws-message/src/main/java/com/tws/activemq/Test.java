package com.tws.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;

/**
 * Created by admin on 2/1/2016.
 */
public class Test {
    public static void main(String[] args) throws Exception {
//        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://10.0.1.27:61616");
//        TopicConnection connection = factory.createTopicConnection();
//        Session session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
//        Destination destination = session.createTopic("Tick");
//        TextMessage message = session.createTextMessage("fasdfjlaskdf");
//        MessageProducer producer = session.createProducer(destination);
//        producer.send(message);

        ApplicationContext ctx = new ClassPathXmlApplicationContext("message-spring.xml");
        ActivemqPublisher publisher = (ActivemqPublisher) ctx.getBean("publisher");
        publisher.publish("Haha","fsadfsdaf");
        publisher.publish("Tick","fasdfsdaffasdfasdf");

        while(true){
            Thread.sleep(1000);
            publisher.publish("Tick",Thread.currentThread().getName());
        }
    }
}
