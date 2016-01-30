package com.tws.iqfeed.conn;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 1/28/2016.
 */
public class SocketConnection implements Connection {

    private static final Logger logger = LoggerFactory.getLogger(SocketConnection.class);

    private String host;
    private int port;
    private EventLoopGroup eventLoop = new NioEventLoopGroup();
    private ChannelFuture channelFuture;


    public SocketConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void connect(boolean reconnect) {
        channelFuture = createBootstrap(new Bootstrap(), eventLoop, reconnect);
    }

    public ChannelFuture createBootstrap(Bootstrap bootstrap, EventLoopGroup eventLoop, boolean reconnect) {
        bootstrap.group(eventLoop);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new SimpleHandler(SocketConnection.this, reconnect));
            }
        });
        bootstrap.remoteAddress(host, port);
        channelFuture = bootstrap.connect();
        channelFuture.addListener(new ConnectionListener(this, reconnect));
        return channelFuture;
    }

    public void send(String message) {
        if (channelFuture == null) {
            logger.error("Failed to send message due to channelFuture not initialized.");
        } else {
            Channel channel = channelFuture.awaitUninterruptibly().channel();
            channel.writeAndFlush(Unpooled.wrappedBuffer(message.getBytes()));
        }
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

}
