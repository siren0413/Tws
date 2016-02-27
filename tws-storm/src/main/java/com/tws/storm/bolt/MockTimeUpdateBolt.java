package com.tws.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.tws.shared.common.TimeUtils;
import com.tws.storm.Utils;

import java.time.LocalDateTime;
import java.util.Map;

import static com.tws.shared.Constants.LOCAL_DATE_TIME;

/**
 * Created by chris on 2/24/16.
 */
public class MockTimeUpdateBolt extends BaseRichBolt {

    public static final String COMPONENT_ID = "C_MOCK_TIME_UPDATE_BOLT";

    private boolean mock = false;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        mock = (boolean) stormConf.get("mock");
    }

    @Override
    public void execute(Tuple input) {
        if (mock) {
            Utils.currentZonedDateTime = LocalDateTime.parse(input.getStringByField(LOCAL_DATE_TIME), TimeUtils.dateTimeMicroSecFormatter).atZone(TimeUtils.ZONE_EST);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
