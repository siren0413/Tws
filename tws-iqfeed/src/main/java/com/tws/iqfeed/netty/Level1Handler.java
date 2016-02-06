package com.tws.iqfeed.netty;

import com.tws.activemq.ActivemqPublisher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.Set;

/**
 * Created by admin on 1/31/2016.
 */
@ChannelHandler.Sharable
public class Level1Handler extends SimpleChannelInboundHandler {
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
            publisher.publish(LEVEL1_TOPIC, new String(buffer, 0, len));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive " + new Date());
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
        System.out.println("channelActive " + new Date());
        super.channelActive(ctx);
    }
}
