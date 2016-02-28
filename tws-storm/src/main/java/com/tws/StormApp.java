package com.tws;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.tws.shared.Constants;
import com.tws.shared.iqfeed.model.Level1Update;
import com.tws.storm.StormConfiguration;
import com.tws.storm.bolt.*;
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
        StormConfiguration configuration = ctx.getBean(StormConfiguration.class);

        Config conf = new Config();
        conf.setDebug(false);
        conf.setNumWorkers(1);
        conf.put("mock", configuration.isMock());
        conf.put("symbolList", configuration.getSymbolList());

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout(Level1SummarySpout.COMPONENT_ID, new Level1SummarySpout(), 5);
        builder.setSpout(Level1UpdateSpout.COMPONENT_ID, new Level1UpdateSpout(), 5);
        builder.setSpout(TickSpout.COMPONENT_ID, new TickSpout(), 1).setNumTasks(1);

//        builder.setBolt(Level1Bar1SecBolt.COMPONENT_ID, new Level1Bar1SecBolt()).fieldsGrouping(Level1UpdateSpout.COMPONENT_ID, Level1UpdateSpout.STREAM_ID, new Fields(Constants.SYMBOL)).shuffleGrouping(TickSpout.COMPONENT_ID, TickSpout.STREAM_ID);
//        builder.setBolt(Level1Bar5SecBolt.COMPONENT_ID, new Level1Bar5SecBolt()).fieldsGrouping(Level1UpdateSpout.COMPONENT_ID, Level1UpdateSpout.STREAM_ID, new Fields(Constants.SYMBOL)).shuffleGrouping(TickSpout.COMPONENT_ID, TickSpout.STREAM_ID);
        builder.setBolt(Level1Bar60SecBolt.COMPONENT_ID, new Level1Bar60SecBolt()).fieldsGrouping(Level1UpdateSpout.COMPONENT_ID, Level1UpdateSpout.STREAM_ID, new Fields(Constants.SYMBOL)).shuffleGrouping(TickSpout.COMPONENT_ID, TickSpout.STREAM_ID);
//        builder.setBolt(Level1Bar300SecBolt.COMPONENT_ID, new Level1Bar300SecBolt()).fieldsGrouping(Level1UpdateSpout.COMPONENT_ID, Level1UpdateSpout.STREAM_ID, new Fields(Constants.SYMBOL)).shuffleGrouping(TickSpout.COMPONENT_ID, TickSpout.STREAM_ID);
//        builder.setBolt(Level1Bar600SecBolt.COMPONENT_ID, new Level1Bar600SecBolt()).fieldsGrouping(Level1UpdateSpout.COMPONENT_ID, Level1UpdateSpout.STREAM_ID, new Fields(Constants.SYMBOL)).shuffleGrouping(TickSpout.COMPONENT_ID, TickSpout.STREAM_ID);
//        builder.setBolt(Level1Bar3600SecBolt.COMPONENT_ID, new Level1Bar3600SecBolt()).fieldsGrouping(Level1UpdateSpout.COMPONENT_ID, Level1UpdateSpout.STREAM_ID, new Fields(Constants.SYMBOL)).shuffleGrouping(TickSpout.COMPONENT_ID, TickSpout.STREAM_ID);

//        builder.setBolt(Level1SMA60SecBolt.COMPONENT_ID, new Level1SMA60SecBolt()).fieldsGrouping(Level1Bar1SecBolt.COMPONENT_ID, Level1Bar1SecBolt.STREAM_ID, new Fields(Constants.SYMBOL));
//        builder.setBolt(Level1SMA300SecBolt.COMPONENT_ID, new Level1SMA300SecBolt()).fieldsGrouping(Level1Bar60SecBolt.COMPONENT_ID, Level1Bar60SecBolt.STREAM_ID, new Fields(Constants.SYMBOL));
        builder.setBolt(Level1SMA600SecBolt.COMPONENT_ID, new Level1SMA600SecBolt()).fieldsGrouping(Level1Bar60SecBolt.COMPONENT_ID, Level1Bar60SecBolt.STREAM_ID, new Fields(Constants.SYMBOL));
//        builder.setBolt(Level1SMA3600SecBolt.COMPONENT_ID, new Level1SMA3600SecBolt()).fieldsGrouping(Level1Bar60SecBolt.COMPONENT_ID, Level1Bar60SecBolt.STREAM_ID, new Fields(Constants.SYMBOL));
//        builder.setBolt(Level1SMA86400SecBolt.COMPONENT_ID, new Level1SMA86400SecBolt()).fieldsGrouping(Level1Bar3600SecBolt.COMPONENT_ID, Level1Bar3600SecBolt.STREAM_ID, new Fields(Constants.SYMBOL));

        builder.setBolt(MockTimeUpdateBolt.COMPONENT_ID, new MockTimeUpdateBolt()).shuffleGrouping(Level1UpdateSpout.COMPONENT_ID, Level1UpdateSpout.STREAM_ID);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());

    }
}
