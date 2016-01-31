package com.tws.iqfeed.socket;

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
    private boolean reconnect;

    public ConnectionListener(SocketConnection socketConnection, boolean reconnect) {
        this.socketConnection = socketConnection;
        this.reconnect = reconnect;
    }
    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        channelFuture.awaitUninterruptibly();
        if (!channelFuture.isSuccess()) {
            System.out.println("Reconnect............");
            Runtime.getRuntime().exec("IQConnect.exe -product YIJUN_MAO_11967 -version 1.0 ");
            final EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
//                    ChannelFuture newChannelFuture = socketConnection.createBootstrap(new Bootstrap(), loop, reconnect);
//                    socketConnection.setChannelFuture(newChannelFuture);
                }
            }, 1L, TimeUnit.SECONDS);
        }
    }
}