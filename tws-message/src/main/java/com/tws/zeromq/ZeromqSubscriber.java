package com.tws.zeromq;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.zeromq.ZMQ;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by admin on 1/31/2016.
 */
public class ZeromqSubscriber implements InitializingBean {

    private String host;
    private String channel;
    private ZeromqMessageListener messageListener;
    private ExecutorService executor;


    @Override
    public void afterPropertiesSet() throws Exception {
        executor = Executors.newFixedThreadPool(2);
        for(int i =0; i < 2; i++) {
            executor.submit(new Worker());
        }
    }

    class Worker implements Runnable {

        private ZMQ.Socket subscriber;

        public Worker() {
            ZMQ.Context context = ZMQ.context(1);
            subscriber = context.socket(ZMQ.SUB);
            subscriber.connect("tcp://" + host + ":5678");
            subscriber.subscribe(channel.getBytes());
        }

        @Override
        public void run() {
            while (true) {
                String msg = subscriber.recvStr();
                String value = msg.split(" ")[1];
                messageListener.onMessageReceived(value);
            }
        }
    }

    @Required
    public void setHost(String host) {
        this.host = host;
    }
    @Required
    public void setChannel(String channel) {
        this.channel = channel;
    }
    @Required
    public void setMessageListener(ZeromqMessageListener messageListener) {
        this.messageListener = messageListener;
    }
}
