package com.tws.iqfeed.netty;

import com.tws.activemq.ActivemqPublisher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.Set;

/**
 * Created by admin on 1/31/2016.
 */
@ChannelHandler.Sharable
public class Level1Handler extends SimpleChannelInboundHandler {

    private static final Logger logger = LoggerFactory.getLogger(Level1Handler.class);

    private byte[] buffer = new byte[102400];
    private ActivemqPublisher publisher;
    private static final String LEVEL1_TOPIC = "LEVEL1_FEED";

    @Required
    public void setPublisher(ActivemqPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        if (in.isReadable()) {
            int len = in.readableBytes();
            in.getBytes(0, buffer, 0, len);
            String message = new String(buffer, 0, len).trim();
            MessageTypeFilter.TYPE type = MessageTypeFilter.filterLevel1Msg(message);
            switch (type){
                case ERROR:
                    logger.error("received error message: " + message);
                    break;
                case SYMBOL_NOT_FOUND:
                    logger.error("symbol not found: " + message);
                    break;
            }
            publisher.publish(LEVEL1_TOPIC, message);
        }
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
