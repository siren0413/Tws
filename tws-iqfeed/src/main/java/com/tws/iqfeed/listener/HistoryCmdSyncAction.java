package com.tws.iqfeed.listener;

import com.tws.iqfeed.common.SequenceAction;
import com.tws.iqfeed.common.SequenceContext;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by yijunmao on 2/19/16.
 */
public class HistoryCmdSyncAction implements SequenceAction<String>, InitializingBean {

    private CuratorFramework client;
    private String zookeeperUrl;

    @Override
    public boolean execute(SequenceContext ctx, String item) {
        //pull out from queue
        //create node
        return false;
    }


    @Required
    public void setZookeeperUrl(String zookeeperUrl) {
        this.zookeeperUrl = zookeeperUrl;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperUrl, retryPolicy);
        client.start();
    }
}
