package com.tws.repository;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("repository-spring.xml");
        final MessageHandler messageHandler = (MessageHandler) context.getBean("messageHandler");
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(new Runnable() {
            public void run() {
                while (true) {
                    messageHandler.handle();
                }
            }
        });

    }
}
