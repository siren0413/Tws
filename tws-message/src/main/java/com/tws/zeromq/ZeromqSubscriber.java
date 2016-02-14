package com.tws.zeromq;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.zeromq.ZMQ;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by admin on 1/31/2016.
 */
public class ZeromqSubscriber implements InitializingBean {

    private String url;
    private String topic;
    private ZeromqMessageListener messageListener;
    private ZMQ.Socket subscriber;
    private int numIOThreads = 1;
    private int poolSize = 1;


    @Override
    public void afterPropertiesSet() throws Exception {
        ZMQ.Context context = ZMQ.context(numIOThreads);
        this.subscriber = context.socket(ZMQ.SUB);
        this.subscriber.connect(url);
        this.subscriber.subscribe(topic.getBytes());
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        for (int i = 0; i < poolSize; i++) {
            executor.submit(new SubWorker());
        }
    }

    class SubWorker implements Runnable {

        @Override
        public void run() {
            while (true) {
                String msg = subscriber.recvStr();
                String content = msg.substring(msg.indexOf(" ") + 1);
                messageListener.onMessageReceived(content);
            }
        }
    }

    @Required
    public void setUrl(String url) {
        this.url = url;
    }

    @Required
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Required
    public void setMessageListener(ZeromqMessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void setNumIOThreads(int numIOThreads) {
        this.numIOThreads = numIOThreads;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
