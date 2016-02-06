package com.tws.repository;

/**
 * Created by admin on 2/2/2016.
 */
public class Level1Tick {
    private float bid;
    private int bidSize;
    private long bidTime;
    private float ask;
    private int askSize;
    private long askTime;
    private float last;
    private int lastSize;
    private long lastTime;

    public float getBid() {
        return bid;
    }

    public void setBid(float bid) {
        this.bid = bid;
    }

    public int getBidSize() {
        return bidSize;
    }

    public void setBidSize(int bidSize) {
        this.bidSize = bidSize;
    }

    public long getBidTime() {
        return bidTime;
    }

    public void setBidTime(long bidTime) {
        this.bidTime = bidTime;
    }

    public float getAsk() {
        return ask;
    }

    public void setAsk(float ask) {
        this.ask = ask;
    }

    public int getAskSize() {
        return askSize;
    }

    public void setAskSize(int askSize) {
        this.askSize = askSize;
    }

    public long getAskTime() {
        return askTime;
    }

    public void setAskTime(long askTime) {
        this.askTime = askTime;
    }

    public float getLast() {
        return last;
    }

    public void setLast(float last) {
        this.last = last;
    }

    public int getLastSize() {
        return lastSize;
    }

    public void setLastSize(int lastSize) {
        this.lastSize = lastSize;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
