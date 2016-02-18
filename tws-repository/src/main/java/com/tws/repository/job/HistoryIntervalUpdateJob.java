package com.tws.repository.job;

import com.tws.repository.GlobalQueues;
import com.tws.repository.service.HistoryIntervalUpdateService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by admin on 2/16/2016.
 */

public class HistoryIntervalUpdateJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(HistoryIntervalUpdateJob.class);

    private List<String> intervals;
    private Date secondStartDate;
    private Date minuteStartDate;
    private Date dayStartDate;

    @Autowired
    private HistoryIntervalUpdateService dbUpdateService;

    private void init(JobDataMap map) {
        String intervalString = map.getString("intervalList");
        String secondStart = map.getString("secondStartDate");
        String minuteStart = map.getString("minuteStartDate");
        String dayStart = map.getString("dayStartDate");
        intervals = new LinkedList<>();
        StringTokenizer tokenizer = new StringTokenizer(intervalString, ",");
        while (tokenizer.hasMoreTokens()) {
            intervals.add(tokenizer.nextToken());
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            secondStartDate = format.parse(secondStart);
            minuteStartDate = format.parse(minuteStart);
            dayStartDate = format.parse(dayStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        logger.info("History interval update job init: ");
        logger.info("interval list: {}", intervalString);
        logger.info("second start: {}", secondStart);
        logger.info("minute start: {}", minuteStart);
        logger.info("day start: {}", dayStart);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        logger.info("start job: {}", jobKey);

        JobDataMap map = context.getJobDetail().getJobDataMap();
        init(map);

        GlobalQueues.symbolQueue.offer("AAPL");
        List<String> symbols = new LinkedList<>();
        while (GlobalQueues.symbolQueue.peek() != null) {
            String symbol = GlobalQueues.symbolQueue.poll();
            if (symbol != null) {
                symbols.add(symbol);
            }
        }

        logger.info("job: {}, load symbol: {}", jobKey, symbols);

    }


}
