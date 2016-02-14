package com.tws.iqfeed.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
public class HistoryErrorDetectHandler extends SimpleChannelInboundHandler<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(HistoryErrorDetectHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        if(list.size() >= 8 && list.size() <= 11 && !"E".equals(list.get(0)) && !"E".equals(list.get(1))){
            ctx.fireChannelRead(list);
        }else{
            logger.error("Error message detect: {}", list);
        }
    }
}
