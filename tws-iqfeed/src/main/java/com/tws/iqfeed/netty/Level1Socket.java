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
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by admin on 1/31/2016.
 */
@Component
public class Level1Socket implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(Level1Socket.class);

    public static BlockingQueue<String> symbolQueue;
    public static BlockingQueue<String> commandQueue;
    public static BlockingQueue<Channel> channelQueue;
    public static final String ATTRIBUTE_KEY_SYMBOL = "SYMBOL_SET";
    public static final String ATTRIBUTE_KEY_INITCMD = "INIT_CMD";

    private ChannelPool pool;
    private ExecutorService executor;
    private int numThreads;
    private int poolSize;
    private String symbolList;


    private void start() {
        executor.submit(new SymbolQueueWorker());
        executor.submit(new CommandQueueWorker());
        executor.submit(new ChannelQueueWorker());
    }

    class SymbolQueueWorker implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    String elem = symbolQueue.take();
                    Future<Channel> future = pool.acquire();
                    future.addListener(new GenericFutureListener<Future<? super Channel>>() {
                        @Override
                        public void operationComplete(Future<? super Channel> future) throws Exception {
                            if (future.isSuccess()) {
                                Channel channel = (Channel) future.getNow();
                                if (channel != null) {
                                    Attribute cmdAttr = channel.attr(AttributeKey.valueOf(ATTRIBUTE_KEY_INITCMD));
                                    if (cmdAttr.get() == null) {
                                        for (String cmd : getInitCmd()) {
                                            channel.writeAndFlush(Unpooled.wrappedBuffer(cmd.getBytes()));
                                        }
                                        cmdAttr.set(getInitCmd());
                                    }
                                    String msg = String.format("w%s\r\n", elem);
                                    channel.writeAndFlush(Unpooled.wrappedBuffer(msg.getBytes()));
                                    logger.info("Request feed of symbol [{}], channel: {}", elem, channel.toString());
                                    Attribute<Set<String>> attr = channel.attr(AttributeKey.valueOf(ATTRIBUTE_KEY_SYMBOL));
                                    Set<String> value = attr.get();
                                    if (value == null) {
                                        value = new HashSet<>();
                                        attr.set(value);
                                    }
                                    value.add(elem);
                                    channelQueue.offer(channel);
                                }
                            } else {
                                symbolQueue.offer(elem);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    logger.error("error take element from blocking queue.", e);
                }
            }
        }
    }

    class CommandQueueWorker implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    String cmd = commandQueue.take();
                    Future<Channel> future = pool.acquire();
                    future.addListener(new GenericFutureListener<Future<? super Channel>>() {
                        @Override
                        public void operationComplete(Future<? super Channel> future) throws Exception {
                            if (future.isSuccess()) {
                                Channel channel = (Channel) future.getNow();
                                if (channel != null) {
                                    logger.info("Send command: [{}], channel: {}", cmd.replace("\r\n", ""), channel.toString());
                                    channel.writeAndFlush((Unpooled.wrappedBuffer(cmd.getBytes())));
                                    pool.release(channel);
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

    class ChannelQueueWorker implements Runnable {

        private Queue<Channel> queue = new LinkedList<>();

        @Override
        public void run() {
            while (true) {
                try {
                    Channel channel = channelQueue.take();
                    queue.add(channel);
                    if (queue.size() == poolSize) {
                        Attribute<Set<String>> attr = channel.attr(AttributeKey.valueOf(ATTRIBUTE_KEY_SYMBOL));
                        logger.debug("ChannelQueue is full with size [{}], pop first channel back to pool. attr size {}", poolSize, attr.get().size());
                        Channel polledChannel = queue.poll();
                        if (polledChannel != null) {
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
        list.add(Command.COMMON.CONNECT());
        list.add(Command.LEVEL1.NEWS_ON());
        list.add(Command.COMMON.SET_PROTOCOL());
        list.add(Command.LEVEL1.SELECT_UPDATE_FIELD());
        list.add(Command.LEVEL1.TIMESTAMP_ON());
        return list;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Level1 socket initialized with following parameters:");
        logger.info("pool size = {}", poolSize);
        logger.info("number of executor threads = {}", numThreads);
        logger.info("symbol list: {}", symbolList);
        symbolQueue = new LinkedBlockingDeque<>();
        commandQueue = new LinkedBlockingDeque<>();
        channelQueue = new LinkedBlockingDeque<>();
        executor = Executors.newFixedThreadPool(3);
        StringTokenizer tokenizer = new StringTokenizer(symbolList, ",");
        while (tokenizer.hasMoreTokens()) {
            symbolQueue.add(tokenizer.nextToken());
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

    @Required
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
