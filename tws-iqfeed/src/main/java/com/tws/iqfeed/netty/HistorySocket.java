package com.tws.iqfeed.netty;

import com.tws.iqfeed.common.Command;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPool;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by admin on 1/31/2016.
 */
public class HistorySocket implements InitializingBean {
    public static BlockingQueue<String> queue;
    public static final String ATTRIBUTE_KEY = "HISTORY_CMD";

    private ChannelPool pool;
    private ExecutorService executor;
    private int numThreads;

    private void start() {
        for (int i = 0; i < numThreads; i++) {
            executor.submit(new QueueWorker());
        }
    }

    class QueueWorker implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    String elem = queue.take();
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
                                    pool.release(channel);
                                }
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void send(String cmd) {
        queue.offer(cmd);
    }

    private List<String> getInitCmd() {
        List<String> list = new LinkedList<>();
        list.add(Command.COMMON.SET_PROTOCOL());
        return list;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        queue = new LinkedBlockingDeque<>();
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

}
