package com.tws.iqfeed.handler;

import com.google.common.base.Splitter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
public class MessageSplitterHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(MessageSplitterHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if (msg != null && !msg.isEmpty()) {
            Splitter splitter = Splitter.on(",").trimResults();
            List<String> list = splitter.splitToList(msg.trim());
            ctx.fireChannelRead(list);
        } else {
            logger.error("Message body is empty: [{}]", msg);
        }
    }
}
