package com.tws.message;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.util.Properties;

/**
 * Created by admin on 1/30/2016.
 */
public class KafkaProducer implements InitializingBean {

    private Producer<Integer, String> producer;
    private DefaultProperties defaultProperties;

    @Required
    public void setDefaultProperties(DefaultProperties defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Properties prop = defaultProperties.getDefaultProducer();
        producer = new kafka.javaapi.producer.Producer<>(new ProducerConfig(prop));
    }

    public void produce(String topic, String msg){
        producer.send(new KeyedMessage<>(topic, msg));
    }


}
