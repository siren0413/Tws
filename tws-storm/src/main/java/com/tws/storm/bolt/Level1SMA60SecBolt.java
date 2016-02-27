package com.tws.storm.bolt;

/**
 * Created by chris on 2/27/16.
 */
public class Level1SMA60SecBolt extends Level1SMABaseBolt {

    public static final String STREAM_ID = "S_SMA_60_SEC";
    public static final String COMPONENT_ID = "C_LEVEL1_SIXTY_SECOND_SMA_BOLT";

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
