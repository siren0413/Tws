package com.tws.message;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by admin on 1/30/2016.
 */
public class KafkaConsumer{

    private ConsumerConnector consumer;
    private int NUM_THREADS = 2;
    private ExecutorService executor;
    private MessageListener messageListener;
    private DefaultProperties defaultProperties;

    @Required
    public void setDefaultProperties(DefaultProperties defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    @Required
    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void consume(String topic){
        Properties prop = defaultProperties.getDefaultConsumer();
        ConsumerConfig consumerConfig = new ConsumerConfig(prop);
        consumer = Consumer.createJavaConsumerConnector(consumerConfig);
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, NUM_THREADS);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        // now launch all the threads
        //
        executor = Executors.newFixedThreadPool(NUM_THREADS);

        // now create an object to consume the messages
        //
        for (final KafkaStream stream : streams) {
            executor.submit(new ConsumerTest(stream ,messageListener));
        }
    }

    class ConsumerTest implements Runnable {
        private KafkaStream m_stream;
        private MessageListener messageListener;

        public ConsumerTest(KafkaStream a_stream, MessageListener messageListener) {
            m_stream = a_stream;
            this.messageListener = messageListener;
        }

        public void run() {
            ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
            while (it.hasNext()){
                messageListener.messageReceived(new String(it.next().message()));
            }
        }
    }
}
