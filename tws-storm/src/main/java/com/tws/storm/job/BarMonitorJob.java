package com.tws.storm.job;

import backtype.storm.task.OutputCollector;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.tws.storm.Utils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Queue;

import static com.tws.shared.Constants.ASK;
import static com.tws.shared.Constants.BID;
import static com.tws.shared.Constants.LOCAL_DATE_TIME;

/**
 * Created by chris on 2/24/16.
 */
public class BarMonitorJob implements Job {

    private Map<String, Queue<Tuple>> map;
    private int interval;
    private OutputCollector outputCollector;
    private String streamId;
    private boolean mock;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        for (Map.Entry<String, Queue<Tuple>> entry : map.entrySet()) {
            String symbol = entry.getKey();
            Queue<Tuple> queue = entry.getValue();
            float total = 0;
            int count = 0;
            long baseTime = 0;
            while (!queue.isEmpty()) {
                Tuple tuple = queue.poll();
                total += (tuple.getFloatByField(BID) + tuple.getFloatByField(ASK)) / 2;
                count++;
            }
            if (count != 0) {
                float avg = total / count;
                ZonedDateTime zonedDateTime = Utils.getCurrentZonedDateTime(mock);
                String baseTimestamp = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
                outputCollector.emit(streamId, new Values(symbol, baseTimestamp, baseTime, avg));
                System.out.println(new Values(symbol, baseTimestamp, zonedDateTime.toInstant().toEpochMilli(), avg));
            }
        }
    }

    public void setMap(Map<String, Queue<Tuple>> map) {
        this.map = map;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setOutputCollector(OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public void setMock(boolean mock) {
        this.mock = mock;
    }
}