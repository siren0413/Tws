package com.tws.iqfeed.handler.history;

import com.tws.activemq.ActivemqPublisher;
import com.tws.shared.Constants;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
@ChannelHandler.Sharable
public class HistoryCmdResponseMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(HistoryCmdResponseMessageHandler.class);

    @Autowired
    private ActivemqPublisher publisher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        if (list.size() > 0 && ("E".equals(list.get(1)) || list.get(1).contains("ENDMSG"))) {
            publisher.publish(Constants.HISTORY_INTERVAL_RESPONSE_ROUTEKEY_PREFIX, (Serializable) list);
        } else {
            ctx.fireChannelRead(list);
        }
    }
}
