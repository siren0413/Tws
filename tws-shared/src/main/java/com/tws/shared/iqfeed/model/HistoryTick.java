package com.tws.shared.iqfeed.model;

import java.io.Serializable;

/**
 * Created by admin on 2/10/2016.
 */
public class HistoryTick implements Serializable {

    private String requestId;
    private String timestamp;
    private float last;
    private int lastSize;
    private int totalVolume;
    private float bid;
    private float ask;
    private int tickId;
    private String basisForLast;
    private int marketCenter;
    private String tradeCondition;

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

    public int getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(int totalVolume) {
        this.totalVolume = totalVolume;
    }

    public float getBid() {
        return bid;
    }

    public void setBid(float bid) {
        this.bid = bid;
    }

    public float getAsk() {
        return ask;
    }

    public void setAsk(float ask) {
        this.ask = ask;
    }

    public int getTickId() {
        return tickId;
    }

    public void setTickId(int tickId) {
        this.tickId = tickId;
    }

    public String getBasisForLast() {
        return basisForLast;
    }

    public void setBasisForLast(String basisForLast) {
        this.basisForLast = basisForLast;
    }

    public int getMarketCenter() {
        return marketCenter;
    }

    public void setMarketCenter(int marketCenter) {
        this.marketCenter = marketCenter;
    }

    public String getTradeCondition() {
        return tradeCondition;
    }

    public void setTradeCondition(String tradeCondition) {
        this.tradeCondition = tradeCondition;
    }
}
