package com.tws.iqfeed;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
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

    private volatile boolean connected = false;
    private String host;
    private int port;
    private ChannelFuture channelFuture;

    public SocketConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public ChannelFuture connect() {
        ClientBootstrap bootstrap = new ClientBootstrap(
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
                return Channels.pipeline(new SimpleHandler());
            }
        });
        try {
            channelFuture = bootstrap.connect(new InetSocketAddress(host, port)).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to connect to host [" + host + "], port [" + port + "].", e);
        }
        return channelFuture;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void send(String message){
        if(channelFuture == null){
            logger.error("Failed to send message due to channelFuture not initialized.");
        }else{
            Channel channel = channelFuture.getChannel();
            channel.write(ChannelBuffers.wrappedBuffer(message.getBytes()));
        }
    }

    private class SimpleHandler extends SimpleChannelHandler {

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            SocketConnection.this.connected = true;
            logger.info("Connected to host [{}], port [{}].", host, port);
            super.channelConnected(ctx, e);
        }

        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            SocketConnection.this.connected = false;
            logger.info("DisConnected to host [{}], port [{}].", host, port);
            super.channelDisconnected(ctx, e);
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            ChannelBuffer buf = (ChannelBuffer) e.getMessage();
            if (buf.readable()) {
                int len = buf.readableBytes();
                byte[] b = new byte[len];
                buf.getBytes(0, b);
                System.out.println(new String(b));
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            logger.error("Exception:", e.getCause());
        }
    }

}
