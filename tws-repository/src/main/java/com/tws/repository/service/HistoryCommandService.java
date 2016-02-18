package com.tws.repository.service;


/**
 * Created by admin on 2/16/2016.
 */

public class HistoryCommandService {

    private static final String HISTORY_INTERVAL_TIMEFRAME = "HIT";
    private static final String HISTORY_DAY_TIMEFRAME = "HDT";

    public String getTickIntervalCmd(String symbol, int interval, String startTime,String endTime) {
        String beginTimeFilter = "";
        String endTimeFilter = "";
        String dataDirection = "1";
        String maxDataPoints = "";
        String requestId = String.join(".", symbol, String.valueOf(interval));
        String datapointsPerSend = "";
        String intervalType = "s";
        return String.join(",", HISTORY_INTERVAL_TIMEFRAME, symbol, String.valueOf(interval), startTime, endTime, maxDataPoints, beginTimeFilter, endTimeFilter, dataDirection, requestId, datapointsPerSend, intervalType);
    }

    public String getDayIntervalCmd(String symbol, int interval, String startTime,String endTime) {
        String dataDirection = "1";
        String maxDataPoints = "";
        String requestId = String.join(".", symbol, String.valueOf(interval));
        String datapointsPerSend = "";
        return String.join(",", HISTORY_DAY_TIMEFRAME, symbol, startTime, endTime, maxDataPoints, dataDirection, requestId, datapointsPerSend);
    }

}
