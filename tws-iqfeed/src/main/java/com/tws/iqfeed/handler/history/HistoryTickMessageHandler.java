package com.tws.iqfeed.handler.history;

import com.tws.rabbitmq.RabbitmqPublisher;
import com.tws.shared.iqfeed.model.HistoryTick;
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
public class HistoryTickMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(HistoryTickMessageHandler.class);

    @Autowired
    private RabbitmqPublisher publisher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        HistoryTick historyTick = new HistoryTick();
        int i = 0;
        try {
            historyTick.setRequestId(list.get(i++));
            historyTick.setTimestamp(list.get(i++));
            historyTick.setLast(NumberUtils.toInt(list.get(i++)));
            historyTick.setLastSize(NumberUtils.toInt(list.get(i++)));
            historyTick.setTotalVolume(NumberUtils.toInt(list.get(i++)));
            historyTick.setBid(NumberUtils.toFloat(list.get(i++)));
            historyTick.setAsk(NumberUtils.toFloat(list.get(i++)));
            historyTick.setTickId(NumberUtils.toInt(list.get(i++)));
            historyTick.setBasisForLast(list.get(i++));
            historyTick.setMarketCenter(NumberUtils.toInt(list.get(i++)));
            historyTick.setTradeCondition(list.get(i++));
        } catch (Exception e) {
            ctx.fireChannelRead(list);
            return;
        }
        String requestId = historyTick.getRequestId();
        String[] parts = requestId.split("\\.");
        if (parts.length != 2) {
            logger.error("Unsupported requestId: [{}]", requestId);
            return;
        }
        publisher.publish(HISTORY_EXCHANGE, String.join(ROUTEKEY_DELIMETER, HISTORY_TICK_ROUTEKEY_PREFIX, historyTick.getRequestId()), historyTick);
    }
}
