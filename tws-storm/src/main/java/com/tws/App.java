package com.tws;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import com.tws.storm.TestSpout;
import com.tws.storm.spout.Level1SummarySpout;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import storm.trident.TridentTopology;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )  {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("storm-spring.xml");

        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(2);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("level1Summary", new Level1SummarySpout(), 5);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());


    }
}
