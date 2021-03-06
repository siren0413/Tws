package com.tws.iqfeed.handler.level1;

import com.tws.activemq.ActivemqPublisher;
import com.tws.shared.common.Utils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.tws.shared.Constants.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
@ChannelHandler.Sharable
public class Level1FundamentalMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(Level1FundamentalMessageHandler.class);

    @Autowired
    private ActivemqPublisher publisher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        if ("F".equals(list.get(0))) {
            publisher.publish(LEVEL1_FUNDAMENTAL_ROUTEKEY_PREFIX, Utils.getGson().toJson(list));
        } else {
            ctx.fireChannelRead(list);
        }
    }
}
