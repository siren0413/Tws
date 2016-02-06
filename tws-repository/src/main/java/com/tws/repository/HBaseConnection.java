package com.tws.repository;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by admin on 2/2/2016.
 */
public class HBaseConnection implements FactoryBean<Connection>{

    private String zookeeperQuorum;

    @Override
    public Connection getObject() throws Exception {
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", zookeeperQuorum);
        return ConnectionFactory.createConnection(conf);
    }

    @Override
    public Class<?> getObjectType() {
        return Connection.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
