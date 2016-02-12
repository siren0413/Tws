package com.tws.zeromq;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.zeromq.ZMQ;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by admin on 1/31/2016.
 */
public class ZeromqPublisher implements InitializingBean {

    private ZMQ.Socket publisher;
    private int port = 5799;
    private int numIOThreads = 1;

    public void publish(String channel, String msg) {
        publisher.send(channel + " " + msg, ZMQ.NOBLOCK);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ZMQ.Context context = ZMQ.context(numIOThreads);
        this.publisher = context.socket(ZMQ.PUB);
        this.publisher.bind("tcp://*:" + port);
    }

    @Required
    public void setPort(int port) {
        this.port = port;
    }

    public void setNumIOThreads(int numIOThreads) {
        this.numIOThreads = numIOThreads;
    }
}
