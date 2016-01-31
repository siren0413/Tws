package com.tws;

import com.tws.message.DefaultProperties;
import com.tws.message.KafkaConsumer;
import com.tws.message.KafkaProducer;
import com.tws.message.MessageListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.UUID;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("message-spring.xml");
        KafkaConsumer consumer = (KafkaConsumer) ctx.getBean("consumer");
        consumer.consume("tick");


        new Thread(new Runnable() {
            @Override
            public void run() {
                KafkaProducer producer = (KafkaProducer) ctx.getBean("producer");
                int i = 1;
                while(true) {
                    producer.produce("tick", "message_"+ UUID.randomUUID()+"  "+i);
                    i = i+1;
//                    try {
//                        Thread.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }).start();

    }
}
