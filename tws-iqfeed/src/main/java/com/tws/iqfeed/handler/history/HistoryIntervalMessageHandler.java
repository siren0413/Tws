package com.tws.iqfeed.handler.history;

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

import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
@ChannelHandler.Sharable
public class HistoryIntervalMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(HistoryIntervalMessageHandler.class);

    @Autowired
    private RabbitmqPublisher publisher;

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
        publisher.publish(HISTORY_EXCHANGE, String.join(ROUTEKEY_DELIMETER, HISTORY_INTERVAL_ROUTEKEY_PREFIX, historyInterval.getRequestId()), historyInterval);
    }
}
