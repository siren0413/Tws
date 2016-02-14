package com.tws.iqfeed.handler.level1;

import com.tws.iqfeed.netty.Level1Socket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * Created by admin on 1/31/2016.
 */
@ChannelHandler.Sharable
public class Level1UnsupportedMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(Level1UnsupportedMessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        logger.error("Unsupported message: {}", list);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.error("Channel inactive.");
        Channel channel = ctx.channel();
        Attribute<Set<String>> attr = channel.attr(AttributeKey.valueOf(Level1Socket.ATTRIBUTE_KEY_SYMBOL));
        Set<String> value = attr.get();
        if (value != null) {
            value.forEach(Level1Socket.symbolQueue::offer);
        }
        super.channelInactive(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Channel active.");
        super.channelActive(ctx);
    }
}
