package com.tws.iqfeed.socket;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
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
    private ChannelPool channelPool;


    public SocketConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void connect(boolean reconnect) {
        channelPool = createBootstrap(new Bootstrap(), eventLoop, reconnect);
    }

    public ChannelPool createBootstrap(Bootstrap bootstrap, EventLoopGroup eventLoop, boolean reconnect) {
        bootstrap.group(eventLoop);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.channel(NioSocketChannel.class);
//        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
//            @Override
//            protected void initChannel(SocketChannel socketChannel) throws Exception {
//                socketChannel.pipeline().addLast(new SimpleHandler(SocketConnection.this, reconnect));
//            }
//        });
        bootstrap.remoteAddress(host, port);

        ChannelPool channelPool = new SimpleChannelPool(bootstrap, new ChannelPoolHandler() {
            @Override
            public void channelReleased(Channel ch) throws Exception {
                System.out.println("channelReleased");
            }

            @Override
            public void channelAcquired(Channel ch) throws Exception {
                System.out.println("channelAcquired");
            }

            @Override
            public void channelCreated(Channel ch) throws Exception {
                System.out.println("channelCreated");
            }
        });

//        channelFuture = bootstrap.connect();
//        channelFuture.addListener(new ConnectionListener(this, reconnect));
        return channelPool;
    }

    public void send(String message) {
        if (channelFuture == null) {
            logger.error("Failed to send message due to channelFuture not initialized.");
        } else {
            Future<Channel> future = channelPool.acquire();
            future.addListener(new FutureListener<Channel>(){
                @Override
                public void operationComplete(Future<Channel> future) throws Exception {
                    if(future.isSuccess()){
                        Channel channel = future.getNow();
                        channel.writeAndFlush(Unpooled.wrappedBuffer(message.getBytes()));
                        channelPool.release(channel);
                    }
                }
            });

//
//            Channel channel = channelFuture.awaitUninterruptibly().channel();
//            channel.writeAndFlush(Unpooled.wrappedBuffer(message.getBytes()));
        }
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

}
