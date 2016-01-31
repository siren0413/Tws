package com.tws.iqfeed.feed;

import org.glassfish.grizzly.*;
import org.glassfish.grizzly.connectionpool.SingleEndpointPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;


/**
 * Created by admin on 1/30/2016.
 */
public class HistoryFeed implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(HistoryFeed.class);

    private SingleEndpointPool pool;

    @Required
    public void setPool(SingleEndpointPool pool) {
        this.pool = pool;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public void send(String cmd) {
        pool.take(new FeedCompletionHandler(cmd));
    }

    class FeedCompletionHandler implements CompletionHandler<Connection> {

        private String cmd;

        public FeedCompletionHandler(String cmd) {
            this.cmd = cmd;
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
            connection.write(cmd);
        }

        @Override
        public void updated(Connection result) {
            System.out.println("updated");
        }
    }
}
