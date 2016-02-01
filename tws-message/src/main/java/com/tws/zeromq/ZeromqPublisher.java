package com.tws.zeromq;

import org.springframework.beans.factory.InitializingBean;
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

    private ExecutorService executor;
    private ZMQ.Socket publisher;
    private BlockingQueue<Tuple> queue;


    @Override
    public void afterPropertiesSet() throws Exception {
        ZMQ.Context context = ZMQ.context(1);
        publisher = context.socket(ZMQ.PUB);
        publisher.bind("tcp://*:5678");
        executor = Executors.newFixedThreadPool(5);
        queue = new LinkedBlockingDeque<>();
        executor.submit(new Worker());
    }

    public void send(String channel, String msg) {
        queue.offer(new Tuple(channel, msg));
    }

    class Worker implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {
                    Tuple tuple = queue.take();
                    String message = tuple.getA() + " " + tuple.getB();
                    publisher.send(message.getBytes("US-ASCII"),ZMQ.NOBLOCK);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
