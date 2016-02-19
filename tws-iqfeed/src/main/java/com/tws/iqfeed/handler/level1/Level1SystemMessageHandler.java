package com.tws.iqfeed.handler.level1;

import com.tws.activemq.ActivemqPublisher;
import com.tws.rabbitmq.RabbitmqPublisher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tws.shared.Constants.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
@ChannelHandler.Sharable
public class Level1SystemMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(Level1SystemMessageHandler.class);

    @Autowired
    private ActivemqPublisher publisher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        if ("S".equals(list.get(0))) {
            logger.info("System message: {}", list);
            String operation = list.get(1);
            switch (operation) {
                case "WATCHES":
                    publisher.publish(LEVEL1_SYSTEM_WATCH_KEY, (Serializable)list);
                    break;
                default:
                    publisher.publish(LEVEL1_SYSTEM_ROUTEKEY_PREFIX, (Serializable)list);
            }
        } else {
            ctx.fireChannelRead(list);
        }
    }
}
