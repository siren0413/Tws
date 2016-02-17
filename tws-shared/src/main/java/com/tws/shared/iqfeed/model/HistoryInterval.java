package com.tws.shared.iqfeed.model;

import java.io.Serializable;

/**
 * Created by admin on 2/10/2016.
 */
public class HistoryInterval implements Serializable{
    private String requestId;
    private String timestamp;
    private float high;
    private float low;
    private float open;
    private float close;
    private int totalVolume;
    private int periodVolume;
    private int numTrades;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
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

    public int getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(int totalVolume) {
        this.totalVolume = totalVolume;
    }

    public int getPeriodVolume() {
        return periodVolume;
    }

    public void setPeriodVolume(int periodVolume) {
        this.periodVolume = periodVolume;
    }

    public int getNumTrades() {
        return numTrades;
    }

    public void setNumTrades(int numTrades) {
        this.numTrades = numTrades;
    }

    @Override
    public String toString() {
        return "HistoryInterval{" +
                "requestId='" + requestId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", high=" + high +
                ", low=" + low +
                ", open=" + open +
                ", close=" + close +
                ", totalVolume=" + totalVolume +
                ", periodVolume=" + periodVolume +
                ", numTrades=" + numTrades +
                '}';
    }
}
