package com.tws.iqfeed.netty;

import com.tws.iqfeed.common.Command;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPool;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by admin on 1/31/2016.
 */
public class HistorySocket implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(HistorySocket.class);

    public static BlockingQueue<String> commandQueue;
    public static BlockingQueue<Channel> channelQueue;
    public static final String ATTRIBUTE_KEY = "HISTORY_CMD";

    private ChannelPool pool;
    private ExecutorService executor;
    private int numThreads;
    private int poolSize;

    private void start() {
        executor.submit(new QueueWorker());
        executor.submit(new ChannelQueueWorker());
    }

    class QueueWorker implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    String elem = commandQueue.take();
                    Future<Channel> future = pool.acquire();
                    future.addListener(new GenericFutureListener<Future<? super Channel>>() {
                        @Override
                        public void operationComplete(Future<? super Channel> future) throws Exception {
                            if (future.isSuccess()) {
                                Channel channel = (Channel) future.getNow();
                                if (channel != null) {
                                    Attribute attr = channel.attr(AttributeKey.valueOf(ATTRIBUTE_KEY));
                                    if (attr.get() == null) {
                                        for (String cmd : getInitCmd()) {
                                            channel.writeAndFlush(Unpooled.wrappedBuffer(cmd.getBytes()));
                                        }
                                        attr.set(getInitCmd());
                                    }
                                    channel.writeAndFlush(Unpooled.wrappedBuffer(elem.getBytes()));
                                    channelQueue.offer(channel);
                                }
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    logger.error("error take element from blocking queue.", e);
                }
            }
        }
    }

    class ChannelQueueWorker implements Runnable{

        private Queue<Channel> queue = new LinkedList<>();

        @Override
        public void run() {
            while(true){
                try {
                    Channel channel = channelQueue.take();
                    queue.add(channel);
                    if(queue.size() == poolSize){
                        logger.info("ChannelQueue is full with size [{}], pop first channel back to pool.", poolSize);
                        Channel polledChannel = queue.poll();
                        if(polledChannel != null) {
                            pool.release(polledChannel);
                        }
                    }
                } catch (InterruptedException e) {
                    logger.error("error take element from blocking queue.", e);
                }
            }
        }
    }

    public void send(String cmd) {
        commandQueue.offer(cmd);
    }

    private List<String> getInitCmd() {
        List<String> list = new LinkedList<>();
        list.add(Command.COMMON.SET_PROTOCOL());
        return list;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("History socket initialized with following parameters:");
        logger.info("pool size = {}", poolSize);
        logger.info("number of executor threads = {}", numThreads);
        channelQueue = new LinkedBlockingDeque<>();
        commandQueue = new LinkedBlockingDeque<>();
        executor = Executors.newFixedThreadPool(numThreads);
        start();
    }

    @Required
    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    @Required
    public void setPool(ChannelPool pool) {
        this.pool = pool;
    }

    @Required
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
