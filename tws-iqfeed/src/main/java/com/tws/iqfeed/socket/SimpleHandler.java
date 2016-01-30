package com.tws.iqfeed.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 1/29/2016.
 */
public class SimpleHandler extends SimpleChannelInboundHandler {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHandler.class);

    private byte[] buffer = new byte[102400];

    private SocketConnection socketConnection;
    private boolean reconnect;

    public SimpleHandler(SocketConnection socketConnection, boolean reconnect) {
        this.socketConnection = socketConnection;
        this.reconnect = reconnect;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if(reconnect) {
            final EventLoop eventLoop = ctx.channel().eventLoop();
            eventLoop.schedule(new Runnable() {
                @Override
                public void run() {
                    socketConnection.createBootstrap(new Bootstrap(), eventLoop, reconnect);
                }
            }, 1L, TimeUnit.SECONDS);
        }
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
            Files.write(Paths.get("haha.txt"), new String(buffer,0,len).getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
//            System.out.println(new String(buffer, 0, len));
        }
    }
}
