package com.tws.iqfeed.netty;

import com.tws.activemq.ActivemqPublisher;
import com.tws.zeromq.ZeromqPublisher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by admin on 1/31/2016.
 */
@ChannelHandler.Sharable
public class HistoryHandler extends SimpleChannelInboundHandler {

    private byte[] buffer = new byte[81960];
    private ActivemqPublisher publisher;
    private final static String HISTORY_CHANNEL = "HISTORY_TOPIC";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        if (in.isReadable()) {
            int len = in.readableBytes();
            in.getBytes(0, buffer, 0, len);
            publisher.publish(HISTORY_CHANNEL, new String(buffer, 0, len));
        }
    }

    @Required
    public void setPublisher(ActivemqPublisher publisher) {
        this.publisher = publisher;
    }
}
