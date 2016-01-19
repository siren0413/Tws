package com.tws.repository;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("repository-spring.xml");
        MessageHandler messageHandler = (MessageHandler) context.getBean("messageHandler");
        while(true) {
            messageHandler.handle();
        }
    }
}
