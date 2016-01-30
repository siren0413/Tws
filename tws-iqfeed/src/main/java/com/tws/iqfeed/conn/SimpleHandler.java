package com.tws.iqfeed.conn;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 1/29/2016.
 */
public class SimpleHandler extends SimpleChannelHandler {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHandler.class);

    private byte[] buffer = new byte[10240];

    private SocketConnection socketConnection;

    public SimpleHandler(SocketConnection socketConnection) {
        this.socketConnection = socketConnection;
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.info("Connected to remote [{}]", ctx.getChannel().getRemoteAddress());
        super.channelConnected(ctx, e);
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.info("Disconnected to remote [{}]", ctx.getChannel().getRemoteAddress());
        super.channelDisconnected(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ChannelBuffer channelBuffer = (ChannelBuffer) e.getMessage();
        if (channelBuffer.readable()) {
            int len = channelBuffer.readableBytes();
            channelBuffer.getBytes(0, buffer, 0, len);
            System.out.println(new String(buffer, 0, len));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        logger.error("Exception:", e.getCause());
    }
}
