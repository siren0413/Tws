package com.tws.iqfeed.handler.history;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
@ChannelHandler.Sharable
public class HistoryErrorMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(HistoryErrorMessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        if ("E".equals(list.get(0)) || (list.size() > 0 && "E".equals(list.get(1)))) {
            logger.error("Error message received: {}", list);
        } else {
            ctx.fireChannelRead(list);
        }
    }
}
