package com.tws.iqfeed.handler.history;

import com.tws.rabbitmq.RabbitmqPublisher;
import com.tws.shared.iqfeed.model.HistoryInterval;
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
public class HistoryIntervalMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    @Autowired
    private RabbitmqPublisher publisher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        HistoryInterval historyInterval = new HistoryInterval();
        int i = 0;
        try {
            historyInterval.setRequestId(list.get(i++));
            historyInterval.setTimestamp(list.get(i++));
            historyInterval.setHigh(Float.parseFloat(list.get(i++)));
            historyInterval.setLow(Float.parseFloat(list.get(i++)));
            historyInterval.setOpen(Float.parseFloat(list.get(i++)));
            historyInterval.setClose(Float.parseFloat(list.get(i++)));
            historyInterval.setTotalVolume(Integer.parseInt(list.get(i++)));
            historyInterval.setPeriodVolume(Integer.parseInt(list.get(i++)));
            historyInterval.setNumTrades(Integer.parseInt(list.get(i++)));
        } catch (Exception e) {
            ctx.fireChannelRead(list);
            return;
        }
        publisher.publish(HISTORY_EXCHANGE, String.join(ROUTEKEY_DELIMETER, HISTORY_INTERVAL_ROUTEKEY_PREFIX, historyInterval.getRequestId()), historyInterval);
    }
}
