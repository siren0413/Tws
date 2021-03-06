package com.tws.iqfeed.handler.level1;

import com.tws.activemq.ActivemqPublisher;
import com.tws.shared.common.TimeUtils;
import com.tws.shared.common.Utils;
import com.tws.shared.iqfeed.model.Level1Summary;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tws.shared.Constants.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
@ChannelHandler.Sharable
public class Level1SummaryMessageHandler extends SimpleChannelInboundHandler<List<String>> {

    @Autowired
    private ActivemqPublisher publisher;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<String> list) throws Exception {
        if ("P".equals(list.get(0))) {
            Level1Summary level1Summary = new Level1Summary();
            int i = 1;
            try {
                level1Summary.setSymbol(list.get(i++));
                level1Summary.setBid(NumberUtils.toFloat(list.get(i++)));
                level1Summary.setBidSize(NumberUtils.toInt(list.get(i++)));
                level1Summary.setBidTime(list.get(i++));
                level1Summary.setAsk(NumberUtils.toFloat(list.get(i++)));
                level1Summary.setAskSize(NumberUtils.toInt(list.get(i++)));
                level1Summary.setAskTime(list.get(i++));
                level1Summary.setLast(NumberUtils.toFloat(list.get(i++)));
                level1Summary.setLastSize(NumberUtils.toInt(list.get(i++)));
                level1Summary.setLastTime(list.get(i++));
                level1Summary.setExtendedTrade(NumberUtils.toFloat(list.get(i++)));
                level1Summary.setExtendedTradeDate(list.get(i++));
                level1Summary.setExtendedTradeTime(list.get(i++));
                level1Summary.setExtendedTradeSize(NumberUtils.toInt(list.get(i++)));
                level1Summary.setTotalVolume(NumberUtils.toInt(list.get(i++)));
                level1Summary.setLow(NumberUtils.toFloat(list.get(i++)));
                level1Summary.setHigh(NumberUtils.toFloat(list.get(i++)));
                level1Summary.setOpen(NumberUtils.toFloat(list.get(i++)));
                level1Summary.setMessageContent(list.get(i++));
                level1Summary.setExchangeId(list.get(i++));
                level1Summary.setDelay(NumberUtils.toInt(list.get(i++)));
                ZonedDateTime zonedDateTime = ZonedDateTime.now(TimeUtils.ZONE_EST);
                String localDateTime = zonedDateTime.format(TimeUtils.dateTimeMilliSecFormatter);
                level1Summary.setLocalDateTime(localDateTime);
            }catch (Exception e){
                ctx.fireChannelRead(list);
                return;
            }
            publisher.publish(LEVEL1_SUMMARY_ROUTEKEY_PREFIX, Utils.getGson().toJson(level1Summary));
        } else {
            ctx.fireChannelRead(list);
        }
    }
}
