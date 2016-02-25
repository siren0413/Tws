package com.tws.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.tws.storm.Utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.tws.shared.Constants.LOCAL_DATE_TIME;

/**
 * Created by chris on 2/24/16.
 */
public class MockTimeUpdateBolt extends BaseRichBolt {

    private boolean mock = false;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        mock = (boolean) stormConf.get("mock");
    }

    @Override
    public void execute(Tuple input) {
        if(mock) {
            Utils.currentZonedDateTime = LocalDateTime.parse(input.getStringByField(LOCAL_DATE_TIME), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")).atZone(ZoneId.of("America/New_York"));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
