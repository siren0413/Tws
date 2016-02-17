package com.tws.repository.service;


import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2/16/2016.
 */

public class HistoryCommandService {

    private static final String HISTORY_INTERVAL_TIMEFRAME = "HIT";
    private static final String HISTORY_DAY_TIMEFRAME = "HDT";

    public String getCommand(String symbol, int interval, long start) {
        if (interval < 86400) {
            return getTickIntervalCmd(symbol, interval, start);
        } else {
            return getDayIntervalCmd(symbol, interval, start);
        }
    }

    private String getTickIntervalCmd(String symbol, int interval, long start) {
        Date date = new Date(start);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HHmmss");
        String startTime = format.format(date);
        String endTime = "";
        String maxDataPoints = "";
        String beginTimeFilter = "";
        String endTimeFilter = "";
        String dataDirection = "";
        String requestId = String.join(".", symbol, String.valueOf(interval));
        String datapointsPerSend = "";
        String intervalType = "s";
        return String.join(",", HISTORY_INTERVAL_TIMEFRAME, symbol, String.valueOf(interval), startTime, endTime, maxDataPoints, beginTimeFilter, endTimeFilter, dataDirection, requestId, datapointsPerSend, intervalType);
    }

    private String getDayIntervalCmd(String symbol, int interval, long start) {
        Date date = new Date(start);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String startTime = format.format(date);
        String endTime = "";
        String maxDataPoints = "";
        String dataDirection = "";
        String requestId = String.join(".", symbol, String.valueOf(interval));
        String datapointsPerSend = "";
        return String.join(",", HISTORY_DAY_TIMEFRAME, symbol, startTime, endTime, maxDataPoints, dataDirection, requestId, datapointsPerSend);
    }


}
