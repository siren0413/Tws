package com.tws;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.tws.storm.bolt.Level1BarFilterBolt;
import com.tws.storm.bolt.Level1BarFilterBolt2;
import com.tws.storm.bolt.Level1IntervalFilterBolt;
import com.tws.storm.bolt.MockTimeUpdateBolt;
import com.tws.storm.spout.Level1SummarySpout;
import com.tws.storm.spout.Level1UpdateSpout;
import com.tws.storm.spout.TickSpout;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 */
public class StormApp {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("storm-spring.xml");

        Config conf = new Config();
        conf.setDebug(false);
        conf.setNumWorkers(1);
        conf.put("mock", true);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("C_LEVEL1_SUMMARY_SPOUT", new Level1SummarySpout(), 5);
        builder.setSpout("C_LEVEL1_UPDATE_SPOUT", new Level1UpdateSpout(), 5);
        builder.setSpout("C_TICK_SPOUT", new TickSpout(), 1).setNumTasks(1);
        builder.setBolt("C_LEVEL1_FIVE_SECOND_BAR_BOLT", new Level1BarFilterBolt2(60)).fieldsGrouping("C_LEVEL1_UPDATE_SPOUT", "S_LEVEL1_UPDATE", new Fields("symbol")).shuffleGrouping("C_TICK_SPOUT","S_TICK");
        builder.setBolt("C_MOCK_TIME_UPDATE_BOLT", new MockTimeUpdateBolt()).shuffleGrouping("C_LEVEL1_UPDATE_SPOUT", "S_LEVEL1_UPDATE");

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());


    }
}
