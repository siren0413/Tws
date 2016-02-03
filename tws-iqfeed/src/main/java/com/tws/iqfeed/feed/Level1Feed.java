package com.tws.iqfeed.feed;

import com.tws.iqfeed.common.Command;
import org.glassfish.grizzly.*;
import org.glassfish.grizzly.connectionpool.SingleEndpointPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by admin on 1/30/2016.
 */
public class Level1Feed implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(Level1Feed.class);

    private String symbolString;
    private BlockingDeque<Set<String>> queue;
    private SingleEndpointPool pool;
    private ExecutorService service;
    private Set<String> defaultCmdSet;

    @Override
    public void afterPropertiesSet() throws Exception {
        // load symbols
        queue = new LinkedBlockingDeque<>();
        Set<String> symbolSet = new HashSet<>();
        StringTokenizer tokenizer = new StringTokenizer(symbolString, ",");
        while (tokenizer.hasMoreTokens()) {
            String symbol = tokenizer.nextToken();
            symbolSet.add(symbol);
        }
        if (!queue.offer(symbolSet)) {
            logger.error("blocking symbolQueue is full. symbolQueue size [{}]", queue.size());
        }
        // init default
        defaultCmdSet = new HashSet<>();
        defaultCmdSet.add(Command.Level1.CONNECT());
        defaultCmdSet.add(Command.Level1.SET_PROTOCOL());
        defaultCmdSet.add(Command.Level1.TIMESTAMP_ON());
        defaultCmdSet.add(Command.Level1.NEWS_ON());
        defaultCmdSet.add(Command.Level1.SELECT_UPDATE_FIELD());
    }

    public void startFeed() {
        service = Executors.newSingleThreadExecutor();
        service.submit(new QueueMonitor());
    }

    @Required
    public void setSymbolString(String symbolString) {
        this.symbolString = symbolString;
    }

    @Required
    public void setPool(SingleEndpointPool pool) {
        this.pool = pool;
    }

    class FeedCompletionHandler implements CompletionHandler<Connection> {

        private Set<String> symbolSet;
        private Set<String> cmdSet;

        public FeedCompletionHandler(Set<String> symbolSet, Set<String> cmdSet) {
            this.symbolSet = symbolSet;
            this.cmdSet = cmdSet;
        }

        @Override
        public void cancelled() {
            System.out.println("cancelled");
        }

        @Override
        public void failed(Throwable throwable) {
            System.out.println("failed");
            throwable.printStackTrace();
        }

        @Override
        public void completed(Connection connection) {
            if (cmdSet != null && !cmdSet.isEmpty()) {
                cmdSet.forEach(connection::write);
            }
            if (symbolSet == null) {
                connection.closeSilently();
            }
            for (String symbol : symbolSet) {
                connection.write(String.format("w%s\r\n", symbol));
            }
            connection.addCloseListener(new CloseListener() {
                @Override
                public void onClosed(Closeable closeable, ICloseType iCloseType) throws IOException {
                    System.out.println("closed");
                    queue.offer(symbolSet);
                    pool.release(connection);
                }

            });
        }

        @Override
        public void updated(Connection result) {
            System.out.println("updated");
        }
    }


    class QueueMonitor implements Runnable {
        @Override
        public void run() {
            while (true)
                try {
                    Set<String> symbolSet = queue.take();
                    if (symbolSet == null || symbolSet.isEmpty()) {
                        throw new RuntimeException("No symbol found.");
                    }
                    pool.take(new FeedCompletionHandler(symbolSet, defaultCmdSet));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }


}
