package com.tws.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.tws.storm.job.BarMonitorJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import static com.tws.shared.Constants.*;

/**
 * Created by chris on 2/24/16.
 */
public class Level1BarFilterBolt extends BaseRichBolt {

    private static final Logger logger = LoggerFactory.getLogger(Level1IntervalFilterBolt.class);
    public static final String STREAM_ID = "S_BAR";

    private int interval;
    private OutputCollector outputCollector;
    private final Map<String, Queue<Tuple>> map = new ConcurrentHashMap<>();
    private Scheduler scheduler;
    private boolean mock = false;

    public Level1BarFilterBolt(int interval) {
        this.interval = interval;
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        mock = (boolean) stormConf.get("mock");
        this.outputCollector = collector;
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                .build();
        JobDetail job = JobBuilder.newJob(BarMonitorJob.class).build();
        JobDataMap jobDataMap = job.getJobDataMap();
        jobDataMap.put("map", map);
        jobDataMap.put("interval", interval);
        jobDataMap.put("outputCollector", outputCollector);
        jobDataMap.put("streamId", STREAM_ID);
        jobDataMap.put("mock", mock);
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error("unable to start quartz scheduler.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(Tuple input) {
        String symbol = input.getStringByField(SYMBOL);

        if (!map.containsKey(symbol)) {
            map.put(symbol, new LinkedList<>());
        }
        Queue<Tuple> queue = map.get(symbol);
        queue.add(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(STREAM_ID + "_" + interval, new Fields(SYMBOL, "timestamp", "time", "last"));
    }
}


