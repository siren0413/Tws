package com.tws.iqfeed.handler.history;

import com.tws.rabbitmq.RabbitmqPublisher;
import com.tws.shared.iqfeed.model.HistoryTick;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tws.iqfeed.common.Constants.*;

import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
@ChannelHandler.Sharable
public class HistoryTickMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    @Autowired
    private RabbitmqPublisher publisher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        HistoryTick historyTick = new HistoryTick();
        int i = 0;
        try {
            historyTick.setRequestId(list.get(i++));
            historyTick.setTimestamp(list.get(i++));
            historyTick.setLast(Float.parseFloat(list.get(i++)));
            historyTick.setLastSize(Integer.parseInt(list.get(i++)));
            historyTick.setTotalVolume(Integer.parseInt(list.get(i++)));
            historyTick.setBid(Float.parseFloat(list.get(i++)));
            historyTick.setAsk(Float.parseFloat(list.get(i++)));
            historyTick.setTickId(Integer.parseInt(list.get(i++)));
            historyTick.setBasisForLast(list.get(i++));
            historyTick.setMarketCenter(Integer.parseInt(list.get(i++)));
            historyTick.setTradeCondition(list.get(i++));
        } catch (Exception e) {
            ctx.fireChannelRead(list);
            return;
        }
        publisher.publish(HISTORY_EXCHANGE, String.join(ROUTEKEY_DELIMETER, HISTORY_TICK_ROUTEKEY_PREFIX, historyTick.getRequestId()), historyTick);
    }
}
