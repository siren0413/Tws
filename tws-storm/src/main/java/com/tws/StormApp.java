package com.tws;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import com.tws.storm.TestSpout;
import com.tws.storm.bolt.Level1IntervalFilterBolt;
import com.tws.storm.spout.Level1SummarySpout;
import com.tws.storm.spout.Level1UpdateSpout;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import storm.trident.TridentTopology;

/**
 * Hello world!
 *
 */
public class StormApp
{
    public static void main( String[] args )  {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("storm-spring.xml");

        Config conf = new Config();
        conf.setDebug(false);
        conf.setNumWorkers(1);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("C_LEVEL1_SUMMARY_SPOUT", new Level1SummarySpout(), 5);
        builder.setSpout("C_LEVEL1_UPDATE_SPOUT", new Level1UpdateSpout(), 5);
        builder.setBolt("C_LEVEL1_ONE_SEC_BOLT", new Level1IntervalFilterBolt(5)).fieldsGrouping("C_LEVEL1_UPDATE_SPOUT","S_LEVEL1_UPDATE",new Fields("symbol"));

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());


    }
}
