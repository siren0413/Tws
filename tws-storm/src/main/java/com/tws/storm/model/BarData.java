package com.tws.storm.model;

/**
 * Created by chris on 2/28/16.
 */
public class BarData {
    private float low;
    private float high;
    private float open;
    private float close;
    private int volume;
    private int openVolume;

    public BarData() {
    }

    public BarData(float low, float high, float open, float close, int volume, int cumulatedVolume) {
        this.low = low;
        this.high = high;
        this.open = open;
        this.close = close;
        this.volume = volume;
        this.openVolume = cumulatedVolume;
    }

    public BarData(float low, float high, float close, int volume, int cumulatedVolume) {
        this.low = low;
        this.high = high;
        this.close = close;
        this.volume = volume;
        this.openVolume = cumulatedVolume;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getOpenVolume() {
        return openVolume;
    }

    public void setOpenVolume(int openVolume) {
        this.openVolume = openVolume;
    }
}
