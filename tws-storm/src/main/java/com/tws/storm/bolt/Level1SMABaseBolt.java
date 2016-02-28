package com.tws.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.datastax.driver.mapping.Result;
import com.google.common.collect.MinMaxPriorityQueue;
import com.google.common.util.concurrent.ListenableFuture;
import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.cassandra.repo.HistoryIntervalRepository;
import com.tws.shared.common.TimeUtils;
import com.tws.storm.TupleDefinition;
import com.tws.storm.Utils;
import com.tws.storm.model.SMAData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.tws.shared.Constants.*;

/**
 * Created by chris on 2/26/16.
 */
public abstract class Level1SMABaseBolt extends BaseRichBolt {

    private static final Logger logger = LoggerFactory.getLogger(Level1SMABaseBolt.class);

    private boolean mock = false;
    private OutputCollector outputCollector;
    private final Map<String, MinMaxPriorityQueue<SMAData>> map = new ConcurrentHashMap<>();

    private HistoryIntervalRepository repository;


    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        mock = (boolean) stormConf.get("mock");
        List<String> symbolList = (List<String>) stormConf.get("symbolList");

        ApplicationContext ctx = new ClassPathXmlApplicationContext("storm-spring.xml");
        repository = ctx.getBean(HistoryIntervalRepository.class);

        this.outputCollector = collector;

        ZonedDateTime currZonedDateTime = Utils.getCurrentZonedDateTime(mock);

        for (String symbol : symbolList) {
            MinMaxPriorityQueue<SMAData> queue = MinMaxPriorityQueue.<SMAData>orderedBy(new Comparator<SMAData>() {
                @Override
                public int compare(SMAData o1, SMAData o2) {
                    return Long.valueOf(o1.getTime()).compareTo(o2.getTime());
                }
            }).create();
            map.put(symbol, queue);

            ListenableFuture<Result<HistoryIntervalDB>> future = repository.getIntervalInTimeForPoints(symbol, getDBQueryInterval(), currZonedDateTime.toInstant().toEpochMilli(), getInterval() / getDBQueryInterval());
            try {
                Result<HistoryIntervalDB> resultSet = future.get(getInterval(), TimeUnit.SECONDS);
                for (HistoryIntervalDB historyIntervalDB : resultSet) {
                    queue.add(new SMAData(historyIntervalDB.getTime(), historyIntervalDB.getClose()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void execute(Tuple input) {
        ZonedDateTime currZonedDateTime = LocalDateTime.parse(input.getStringByField(TIMESTAMP), TimeUtils.dateTimeSecFormatter).atZone(TimeUtils.ZONE_EST);
        ZonedDateTime startZonedDateTime = currZonedDateTime.minusSeconds(getInterval());

        int barInterval = input.getIntegerByField(INTERVAL);
        Float close = input.getFloatByField(CLOSE);
        long time = input.getLongByField(TIME);
        String symbol = input.getStringByField(SYMBOL);
        MinMaxPriorityQueue<SMAData> queue = map.get(symbol);

        while (!queue.isEmpty() && queue.peekFirst().getTime() <= startZonedDateTime.toInstant().toEpochMilli()) {
            queue.pollFirst();
        }

        while (!queue.isEmpty() && ((queue.peekLast().getTime() / (barInterval * 1000)) == (time / (barInterval * 1000)))) {
            queue.pollLast();
        }

        if (!close.isNaN()) {
            queue.offer(new SMAData(time, close));
        }

        int count = 0;
        float total = 0;
        for (SMAData data : queue) {
            total += data.getClose();
            count++;
        }
        float confidence = (float) count * barInterval / (float) getInterval();
        float avg = total / count;
        outputCollector.emit(getStreamId(), new Values(symbol, currZonedDateTime.format(TimeUtils.dateTimeSecFormatter), currZonedDateTime.toInstant().toEpochMilli(), avg, confidence));
        System.out.println(new Values(symbol, currZonedDateTime.format(TimeUtils.dateTimeSecFormatter), currZonedDateTime.toInstant().toEpochMilli(), avg, confidence));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(getStreamId(), TupleDefinition.S_LEVEL1_SMA);
    }

    protected abstract int getInterval();

    protected abstract String getStreamId();

    protected abstract int getDBQueryInterval();
}