package com.tws.iqfeed.conn;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by admin on 1/28/2016.
 */
public class SocketConnection implements Connection {

    private static final Logger logger = LoggerFactory.getLogger(SocketConnection.class);

    private String host;
    private int port;
    private ChannelFuture channelFuture;
    private ClientBootstrap bootstrap;

    public SocketConnection(String host, int port) {
        this.host = host;
        this.port = port;
        bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));
        bootstrap.setOption("sendBufferSize", 1048576);
        bootstrap.setOption("receiveBufferSize", 1048576);
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new SimpleHandler(SocketConnection.this));
            }
        });
    }

    @Override
    public SocketClient connect() {
        while (true) {
            channelFuture = bootstrap.connect(new InetSocketAddress(host, port));
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    logger.info("1111111111111111111111111111111");
                }
                
            });
            channelFuture.awaitUninterruptibly();
            if (channelFuture.isSuccess()) {
                break;
            }
            logger.warn("Failed to connect to remote [{}], retry in 5 sec...", channelFuture.getChannel().getRemoteAddress());
        }
        return new SocketClientImpl(channelFuture);
    }


}
