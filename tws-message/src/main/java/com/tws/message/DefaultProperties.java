package com.tws.message;

import org.springframework.beans.factory.annotation.Required;

import java.util.Properties;

/**
 * Created by admin on 1/30/2016.
 */
public class DefaultProperties {

    private String serializerClass;
    private String brokerList;
    private String zookeeperConnect;


    @Required
    public void setSerializerClass(String serializerClass) {
        this.serializerClass = serializerClass;
    }

    @Required
    public void setBrokerList(String brokerList) {
        this.brokerList = brokerList;
    }

    @Required
    public void setZookeeperConnect(String zookeeperConnect) {
        this.zookeeperConnect = zookeeperConnect;
    }

    public Properties getDefaultProducer(){
        Properties props = new Properties();
        props.put("serializer.class", serializerClass);
        props.put("metadata.broker.list", brokerList);
        props.put("producer.type", "async");
        props.put("queue.buffering.max.ms", "10");
        return props;
    }

    public Properties getDefaultConsumer(){
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeperConnect);
        props.put("group.id", "iqfeed");
        return props;
    }
}
