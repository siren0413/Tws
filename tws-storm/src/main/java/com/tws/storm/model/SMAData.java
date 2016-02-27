package com.tws.storm.model;

/**
 * Created by chris on 2/26/16.
 */
public class SMAData {
    private long time;
    private float close;

    public SMAData() {
    }

    public SMAData(long time, float close) {
        this.time = time;
        this.close = close;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }
}
