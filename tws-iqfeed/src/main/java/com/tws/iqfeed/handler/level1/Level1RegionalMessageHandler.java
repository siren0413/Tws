package com.tws.iqfeed.handler.level1;

import com.tws.rabbitmq.RabbitmqPublisher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tws.iqfeed.common.Constants.*;

import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */

@ChannelHandler.Sharable
public class Level1RegionalMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    @Autowired
    private RabbitmqPublisher publisher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        if ("R".equals(list.get(0))) {
            publisher.publish(LEVEL1_EXCHANGE, String.join(ROUTEKEY_DELIMETER, LEVEL1_REGIONAL_ROUTEKEY_PREFIX, list.get(1)), list);
        } else {
            ctx.fireChannelRead(list);
        }
    }
}
