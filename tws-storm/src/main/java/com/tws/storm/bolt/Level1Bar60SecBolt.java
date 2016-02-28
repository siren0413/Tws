package com.tws.storm.bolt;

/**
 * Created by chris on 2/27/16.
 */
public class Level1Bar60SecBolt extends Level1BarBaseBolt {

    public static final String STREAM_ID = "S_BAR_1_MIN";
    public static final String COMPONENT_ID = "C_LEVEL1_ONE_MINUTE_BAR_BOLT";

    private static final int interval = 60;

    @Override
    protected int getInterval() {
        return interval;
    }

    @Override
    protected String getStreamId() {
        return STREAM_ID;
    }
}
