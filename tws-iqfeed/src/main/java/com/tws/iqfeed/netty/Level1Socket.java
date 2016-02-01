package com.tws.iqfeed.netty;

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
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by admin on 1/31/2016.
 */
public class Level1Socket implements InitializingBean {

    public static BlockingQueue<String> queue;
    public static final String ATTRIBUTE_KEY = "SYMBOL_SET";

    private ChannelPool pool;
    private ExecutorService executor;
    private int numThreads;
    private String symbolList;


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
                                    String msg = String.format("w%s\r\n", elem);
                                    channel.writeAndFlush(Unpooled.wrappedBuffer(msg.getBytes()));
                                    Attribute<Set<String>> attr = channel.attr(AttributeKey.valueOf(ATTRIBUTE_KEY));
                                    Set<String> value = attr.get();
                                    if(value == null){
                                        value = new HashSet<>();
                                        attr.set(value);
                                    }
                                    value.add(elem);
                                    pool.release(channel);
                                }
                            }else{
                                queue.offer(elem);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        queue = new LinkedBlockingDeque<>();
        executor = Executors.newFixedThreadPool(numThreads);
        StringTokenizer tokenizer = new StringTokenizer(symbolList,",");
        while(tokenizer.hasMoreTokens()){
            queue.add(tokenizer.nextToken());
        }
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
    public void setSymbolList(String symbolList) {
        this.symbolList = symbolList;
    }
}
