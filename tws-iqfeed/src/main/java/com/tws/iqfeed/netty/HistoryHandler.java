package com.tws.iqfeed.netty;

import com.google.common.base.Splitter;
import com.tws.activemq.ActivemqPublisher;
import com.tws.zeromq.ZeromqPublisher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by admin on 1/31/2016.
 */
@ChannelHandler.Sharable
public class HistoryHandler extends SimpleChannelInboundHandler {

    private static final Logger logger = LoggerFactory.getLogger(HistoryHandler.class);

    private ZeromqPublisher publisher;
    private final static String HISTORY_CHANNEL = "HISTORY_DATA";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        String message = buf.toString(CharsetUtil.US_ASCII);

        MessageTypeFilter.TYPE type = MessageTypeFilter.filterHistoryMsg(message);
        if (type == MessageTypeFilter.TYPE.ERROR) {
            logger.error("received error message: " + message);
            return;
        }

        Splitter splitter = Splitter.on(System.getProperty("line.separator")).trimResults().omitEmptyStrings();
        for (String line : splitter.split(message)) {
            publisher.publish(HISTORY_CHANNEL, line);
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
    public void setPublisher(ZeromqPublisher publisher) {
        this.publisher = publisher;
    }
}
