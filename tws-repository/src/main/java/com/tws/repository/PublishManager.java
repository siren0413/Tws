package com.tws.repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tws.activemq.ActivemqPublisher;
import com.tws.shared.Constants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2/19/2016.
 */
public class PublishManager implements InitializingBean {

    private BlockingQueue<String> queue;
    final private Cache<String, Long> lastModifiedCache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();
    private Cache<String, String> responseCache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();
    private int queueSize;
    private int cacheSize;

    @Autowired
    private ActivemqPublisher publisher;

    public void publish(String cmd) {
        queue.offer(cmd);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        queue = new LinkedBlockingQueue<>(queueSize);
    }

    @Required
    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    @Required
    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    class Worker implements Runnable{

        @Override
        public void run() {
            while(true){
                synchronized (lastModifiedCache) {
                    if (lastModifiedCache.size() < cacheSize) {
                        try {
                            String cmd = queue.take();
                            Map<String, Long> map = lastModifiedCache.asMap();
                            publisher.publish(Constants.HISTORY_COMMAND_ROUTEKEY_PREFIX, cmd);
                            map.put(cmd, System.currentTimeMillis());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
