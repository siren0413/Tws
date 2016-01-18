package com.tws.cassandra.model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Created by yijunmao on 1/17/16.
 */

@Table(keyspace = "stock", name = "tick", readConsistency = "ANY", writeConsistency = "ANY")
public class Tick {
    @PartitionKey
    private String symbol;
    @ClusteringColumn
    private long timestamp;
    private double lastPrice;
    private double bidPrice;
    private double askPrice;
    private int bidSize;
    private int askSize;
    private int lastSize;
    private int totalVolume;


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    public int getBidSize() {
        return bidSize;
    }

    public void setBidSize(int bidSize) {
        this.bidSize = bidSize;
    }

    public int getAskSize() {
        return askSize;
    }

    public void setAskSize(int askSize) {
        this.askSize = askSize;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
