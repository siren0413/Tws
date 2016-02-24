package com.tws.iqfeed.handler.level1;

import com.tws.activemq.ActivemqPublisher;
import com.tws.rabbitmq.RabbitmqPublisher;
import com.tws.shared.iqfeed.model.Level1Update;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tws.shared.Constants.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
@ChannelHandler.Sharable
public class Level1UpdateMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    @Autowired
    private ActivemqPublisher publisher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        if ("Q".equals(list.get(0))) {
            Level1Update level1Update = new Level1Update();
            int i = 1;
            try {
                level1Update.setSymbol(list.get(i++));
                level1Update.setBid(NumberUtils.toFloat(list.get(i++)));
                level1Update.setBidSize(NumberUtils.toInt(list.get(i++)));
                level1Update.setBidTime(list.get(i++));
                level1Update.setAsk(NumberUtils.toFloat(list.get(i++)));
                level1Update.setAskSize(NumberUtils.toInt(list.get(i++)));
                level1Update.setAskTime(list.get(i++));
                level1Update.setLast(NumberUtils.toFloat(list.get(i++)));
                level1Update.setLastSize(NumberUtils.toInt(list.get(i++)));
                level1Update.setLastTime(list.get(i++));
                level1Update.setExtendedTrade(NumberUtils.toFloat(list.get(i++)));
                level1Update.setExtendedTradeDate(list.get(i++));
                level1Update.setExtendedTradeTime(list.get(i++));
                level1Update.setExtendedTradeSize(NumberUtils.toInt(list.get(i++)));
                level1Update.setTotalVolume(NumberUtils.toInt(list.get(i++)));
                level1Update.setLow(NumberUtils.toFloat(list.get(i++)));
                level1Update.setHigh(NumberUtils.toFloat(list.get(i++)));
                level1Update.setOpen(NumberUtils.toFloat(list.get(i++)));
                level1Update.setMessageContent(list.get(i++));
                level1Update.setExchangeId(list.get(i++));
                level1Update.setDelay(NumberUtils.toInt(list.get(i++)));
                ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
                String localDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
                level1Update.setLocalDateTime(localDateTime);
            }catch (Exception e){
                ctx.fireChannelRead(list);
                return;
            }
            publisher.publish(LEVEL1_UPDATE_ROUTEKEY_PREFIX, level1Update);
        } else {
            ctx.fireChannelRead(list);
        }
    }
}
