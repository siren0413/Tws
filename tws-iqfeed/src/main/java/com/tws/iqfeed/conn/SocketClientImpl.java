package com.tws.iqfeed.conn;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 1/29/2016.
 */
public class SocketClientImpl implements SocketClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClientImpl.class);

    private ChannelFuture channelFuture;

    public SocketClientImpl(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    @Override
    public void send(String message) {
        if (channelFuture == null) {
            logger.error("Failed to send message due to channelFuture not initialized.");
        } else {
            Channel channel = channelFuture.getChannel();
            channel.write(ChannelBuffers.wrappedBuffer(message.getBytes()));
        }
    }

}
