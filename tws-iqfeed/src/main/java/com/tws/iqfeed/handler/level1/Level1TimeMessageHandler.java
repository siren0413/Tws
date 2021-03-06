package com.tws.iqfeed.handler.level1;

import com.tws.activemq.ActivemqPublisher;
import com.tws.shared.common.Utils;
import com.tws.shared.iqfeed.model.Level1Timestamp;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tws.shared.Constants.*;

import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
@ChannelHandler.Sharable
public class Level1TimeMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    @Autowired
    private ActivemqPublisher publisher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        if ("T".equals(list.get(0))) {
            Level1Timestamp level1Timestamp = new Level1Timestamp();
            int i = 1;
            try{
                level1Timestamp.setTimestamp(list.get(i++));
            }catch (Exception e){
                ctx.fireChannelRead(list);
                return;
            }
            publisher.publish(LEVEL1_TIMESTAMP_ROUTEKEY_PREFIX, Utils.getGson().toJson(list));
        }else{
            ctx.fireChannelRead(list);
        }
    }
}
