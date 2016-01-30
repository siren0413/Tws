package com.tws.iqfeed.conn;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 1/29/2016.
 */
public class SimpleHandler extends SimpleChannelInboundHandler {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHandler.class);

    private byte[] buffer = new byte[10240];

    private SocketConnection socketConnection;

    public SimpleHandler(SocketConnection socketConnection) {
        this.socketConnection = socketConnection;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                socketConnection.createBootstrap(new Bootstrap(), eventLoop);
            }
        }, 1L, TimeUnit.SECONDS);
        logger.info("channel inactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel active");
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("error", cause);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        if (in.isReadable()) {
            int len = in.readableBytes();
            in.getBytes(0, buffer, 0, len);
            System.out.println(new String(buffer, 0, len));
        }
    }
}
