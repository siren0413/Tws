package com.tws.shared.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by admin on 2/20/2016.
 */
public class ZkClient implements FactoryBean<CuratorFramework>{

    private String zookeeperUrl;

    @Override
    public CuratorFramework getObject() throws Exception {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, Integer.MAX_VALUE);
        CuratorFramework curator = CuratorFrameworkFactory.newClient(zookeeperUrl, retryPolicy);
        curator.start();
        curator.getZookeeperClient().blockUntilConnectedOrTimedOut();
        return curator;
    }

    @Override
    public Class<?> getObjectType() {
        return CuratorFramework.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Required
    public void setZookeeperUrl(String zookeeperUrl) {
        this.zookeeperUrl = zookeeperUrl;
    }
}
