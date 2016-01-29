package com.tws.iqfeed;

import org.jboss.netty.channel.ChannelFuture;

/**
 * Created by admin on 1/28/2016.
 */
public interface Connection {
    ChannelFuture connect();
    boolean isConnected();
    void send(String message);
}
