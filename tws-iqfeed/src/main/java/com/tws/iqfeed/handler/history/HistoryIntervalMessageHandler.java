package com.tws.iqfeed.handler.history;

import com.tws.activemq.ActivemqPublisher;
import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.cassandra.repo.HistoryIntervalRepository;
import com.tws.rabbitmq.RabbitmqPublisher;
import com.tws.shared.iqfeed.model.HistoryInterval;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tws.shared.Constants.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
@ChannelHandler.Sharable
public class HistoryIntervalMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(HistoryIntervalMessageHandler.class);

    @Autowired
    private ActivemqPublisher publisher;

    @Autowired
    private HistoryIntervalRepository repository;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        HistoryInterval historyInterval = new HistoryInterval();
        int i = 0;
        try {
            historyInterval.setRequestId(list.get(i++));
            historyInterval.setTimestamp(list.get(i++));
            historyInterval.setHigh(NumberUtils.toFloat(list.get(i++)));
            historyInterval.setLow(NumberUtils.toFloat(list.get(i++)));
            historyInterval.setOpen(NumberUtils.toFloat(list.get(i++)));
            historyInterval.setClose(NumberUtils.toFloat(list.get(i++)));
            historyInterval.setTotalVolume(NumberUtils.toInt(list.get(i++)));
            historyInterval.setPeriodVolume(NumberUtils.toInt(list.get(i++)));
            historyInterval.setNumTrades(NumberUtils.toInt(list.get(i++)));
        } catch (Exception e) {
            ctx.fireChannelRead(list);
            return;
        }
        String requestId = historyInterval.getRequestId();
        String[] parts = requestId.split("\\.");
        if (parts.length != 2) {
            logger.error("Unsupported requestId: [{}]", requestId);
            return;
        }
        save(historyInterval);
//        publisher.publish(HISTORY_INTERVAL_ROUTEKEY_PREFIX, historyInterval);
    }

    private void save(HistoryInterval historyInterval) {
        HistoryIntervalDB historyIntervalDB = new HistoryIntervalDB();
        String[] parts = historyInterval.getRequestId().split("\\.");
        historyIntervalDB.setSymbol(parts[0]);
        historyIntervalDB.setInterval(NumberUtils.toInt(parts[1]));
        if (historyInterval.getTimestamp() == null) {
            return;
        }
        ZonedDateTime zonedDateTime;
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(historyInterval.getTimestamp().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            zonedDateTime = localDateTime.atZone(ZoneId.of("America/New_York"));
        } catch (DateTimeParseException e) {
            try {
                LocalDate localDate = LocalDate.parse(historyInterval.getTimestamp().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                zonedDateTime = localDate.atStartOfDay(ZoneId.of("America/New_York"));
            } catch (DateTimeParseException e1) {
                logger.error("unable to parse date: {}", historyInterval.getTimestamp(),e);
                return;
            }
        }
        historyIntervalDB.setTime(zonedDateTime.toInstant().toEpochMilli());
        historyIntervalDB.setTimestamp(historyInterval.getTimestamp());
        historyIntervalDB.setClose(historyInterval.getClose());
        historyIntervalDB.setHigh(historyInterval.getHigh());
        historyIntervalDB.setLow(historyInterval.getLow());
        historyIntervalDB.setNumTrades(historyInterval.getNumTrades());
        historyIntervalDB.setOpen(historyInterval.getOpen());
        historyIntervalDB.setPeriodVolume(historyInterval.getPeriodVolume());
        historyIntervalDB.setTotalVolume(historyInterval.getTotalVolume());
        repository.saveIntervalInTimeAsync(historyIntervalDB);
    }
}
