package com.tws.iqfeed.netty;

import com.tws.activemq.ActivemqPublisher;
import com.tws.zeromq.ZeromqPublisher;
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

import java.util.Set;

/**
 * Created by admin on 1/31/2016.
 */
@ChannelHandler.Sharable
public class HistoryHandler extends SimpleChannelInboundHandler {

    private static final Logger logger = LoggerFactory.getLogger(HistoryHandler.class);

    private byte[] buffer = new byte[81960];
    private ActivemqPublisher publisher;
    private final static String HISTORY_CHANNEL = "HISTORY_DATA";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        if (in.isReadable()) {
            int len = in.readableBytes();
            in.getBytes(0, buffer, 0, len);
            String message = new String(buffer, 0, len).trim();
            MessageTypeFilter.TYPE type = MessageTypeFilter.filterHistoryMsg(message);
            if(type == MessageTypeFilter.TYPE.ERROR){
                logger.error("received error message: " + message);
            }
            publisher.publish(HISTORY_CHANNEL, message);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.error("Channel inactive.");
        super.channelInactive(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Channel active.");
        super.channelActive(ctx);
    }

    @Required
    public void setPublisher(ActivemqPublisher publisher) {
        this.publisher = publisher;
    }
}
