package com.tws.shared.iqfeed.model;

import java.io.Serializable;

/**
 * Created by admin on 2/11/2016.
 */
public class Level1Summary implements Serializable {
    private String symbol;
    private float bid;
    private int bidSize;
    private String bidTime;
    private float ask;
    private int askSize;
    private String askTime;
    private float last;
    private int lastSize;
    private String lastTime;
    private float extendedTrade;
    private String extendedTradeDate;
    private String extendedTradeTime;
    private int extendedTradeSize;
    private int totalVolume;
    private float low;
    private float high;
    private float open;
    private String messageContent;
    private String exchangeId;
    private int delay;
    private String localDateTime;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

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

    public String getBidTime() {
        return bidTime;
    }

    public void setBidTime(String bidTime) {
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

    public String getAskTime() {
        return askTime;
    }

    public void setAskTime(String askTime) {
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

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public int getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(int totalVolume) {
        this.totalVolume = totalVolume;
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

    public float getExtendedTrade() {
        return extendedTrade;
    }

    public void setExtendedTrade(float extendedTrade) {
        this.extendedTrade = extendedTrade;
    }

    public String getExtendedTradeDate() {
        return extendedTradeDate;
    }

    public void setExtendedTradeDate(String extendedTradeDate) {
        this.extendedTradeDate = extendedTradeDate;
    }

    public String getExtendedTradeTime() {
        return extendedTradeTime;
    }

    public void setExtendedTradeTime(String extendedTradeTime) {
        this.extendedTradeTime = extendedTradeTime;
    }

    public int getExtendedTradeSize() {
        return extendedTradeSize;
    }

    public void setExtendedTradeSize(int extendedTradeSize) {
        this.extendedTradeSize = extendedTradeSize;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public String toString() {
        return "Level1Summary{" +
                "symbol='" + symbol + '\'' +
                ", bid=" + bid +
                ", bidSize=" + bidSize +
                ", bidTime='" + bidTime + '\'' +
                ", ask=" + ask +
                ", askSize=" + askSize +
                ", askTime='" + askTime + '\'' +
                ", last=" + last +
                ", lastSize=" + lastSize +
                ", lastTime='" + lastTime + '\'' +
                ", extendedTrade=" + extendedTrade +
                ", extendedTradeDate='" + extendedTradeDate + '\'' +
                ", extendedTradeTime='" + extendedTradeTime + '\'' +
                ", extendedTradeSize=" + extendedTradeSize +
                ", totalVolume=" + totalVolume +
                ", low=" + low +
                ", high=" + high +
                ", open=" + open +
                ", messageContent='" + messageContent + '\'' +
                ", exchangeId='" + exchangeId + '\'' +
                ", delay=" + delay +
                ", localDateTime='" + localDateTime + '\'' +
                '}';
    }
}
