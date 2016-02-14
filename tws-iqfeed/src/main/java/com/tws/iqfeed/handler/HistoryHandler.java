package com.tws.iqfeed.handler;

import com.tws.activemq.ActivemqPublisher;
import com.tws.rabbitmq.RabbitmqPublisher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by admin on 1/31/2016.
 */
@ChannelHandler.Sharable
public class HistoryHandler extends SimpleChannelInboundHandler<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(HistoryHandler.class);

    @Autowired
    private RabbitmqPublisher publisher;
    private final static String HISTORY_CHANNEL = "HISTORY_DATA";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> msg) throws Exception {

//        MessageTypeFilter.TYPE type = MessageTypeFilter.filterHistoryMsg(msg);
//        if (type == MessageTypeFilter.TYPE.ERROR) {
//            logger.error("received error message: " + msg);
//            return;
//        }

//        publisher.publish(HISTORY_CHANNEL, msg);
        System.out.println(msg);
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
}
