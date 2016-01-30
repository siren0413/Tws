package com.tws.iqfeed.conn;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 1/29/2016.
 */
public class ConnectionListener implements ChannelFutureListener {
    private SocketConnection socketConnection;
    public ConnectionListener(SocketConnection socketConnection) {
        this.socketConnection = socketConnection;
    }
    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (!channelFuture.isSuccess()) {
            System.out.println("Reconnect............");
            final EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
                    ChannelFuture newChannelFuture = socketConnection.createBootstrap(new Bootstrap(), loop);
                    socketConnection.setChannelFuture(newChannelFuture);
                }
            }, 1L, TimeUnit.SECONDS);
        }
    }
}