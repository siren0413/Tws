package com.tws.iqfeed.handler.level1;

import com.tws.shared.common.Utils;
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
public class Level1SymbolNotFoundHandler extends SimpleChannelInboundHandler<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(Level1SymbolNotFoundHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        if ("n".equals(list.get(0))) {
            logger.error("Symbol not found: {}", Utils.getGson().toJson(list));
        }else{
            ctx.fireChannelRead(list);
        }
    }
}
