package com.tws.iqfeed.handler.level1;

import com.tws.activemq.ActivemqPublisher;
import com.tws.shared.common.Utils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.tws.shared.Constants.*;

/**
 * Created by admin on 2/13/2016.
 */
@ChannelHandler.Sharable
public class Level1NewsMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    @Autowired
    private ActivemqPublisher publisher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        if ("N".equals(list.get(0))) {
            publisher.publish(LEVEL1_NEWS_ROUTEKEY_PREFIX, Utils.getGson().toJson(list));
        }else{
            ctx.fireChannelRead(list);
        }
    }
}
