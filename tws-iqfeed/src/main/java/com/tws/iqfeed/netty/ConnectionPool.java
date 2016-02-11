package com.tws.iqfeed.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by admin on 1/31/2016.
 */

public class ConnectionPool implements FactoryBean<ChannelPool> {

    private String host;
    private int port;
    private ChannelHandler handler;
    private int poolSize = 1;
    private boolean fixed;

    @Override
    public ChannelPool getObject() throws Exception {
        EventLoopGroup eventLoop = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoop);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.remoteAddress(host, port);
        bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(204800));

        if(fixed) {
            FixedChannelPool fixedChannelPool = new FixedChannelPool(bootstrap, new AbstractChannelPoolHandler() {
                @Override
                public void channelCreated(Channel ch) throws Exception {
                    ch.pipeline().addLast(handler);
                }
            }, poolSize);

            return fixedChannelPool;
        }else{
            SimpleChannelPool simpleChannelPool = new SimpleChannelPool(bootstrap, new AbstractChannelPoolHandler() {
                @Override
                public void channelCreated(Channel ch) throws Exception {
                    ch.pipeline().addLast(handler);
                }
            });

            return simpleChannelPool;
        }
    }

    @Override
    public Class<?> getObjectType() {
        return FixedChannelPool.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Required
    public void setHost(String host) {
        this.host = host;
    }

    @Required
    public void setPort(int port) {
        this.port = port;
    }

    @Required
    public void setHandler(ChannelHandler handler) {
        this.handler = handler;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    @Required
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
}
