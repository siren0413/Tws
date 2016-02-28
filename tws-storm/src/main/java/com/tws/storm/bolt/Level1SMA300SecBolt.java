package com.tws.storm.bolt;

/**
 * Created by chris on 2/27/16.
 */
public class Level1SMA300SecBolt extends Level1SMABaseBolt {

    public static final String STREAM_ID = "S_SMA_5_MIN";
    public static final String COMPONENT_ID = "C_LEVEL1_FIVE_MINUTES_SMA_BOLT";

    private static final int interval = 300;

    @Override
    protected int getInterval() {
        return interval;
    }

    @Override
    protected String getStreamId() {
        return STREAM_ID;
    }

    @Override
    protected int getDBQueryInterval() {
        return 60;
    }
}
