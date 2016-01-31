package com.tws.iqfeed.grizzly;

import org.glassfish.grizzly.connectionpool.SingleEndpointPool;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.filterchain.TransportFilter;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.glassfish.grizzly.utils.StringFilter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 1/30/2016.
 */
public class ConnectionPool implements FactoryBean<SingleEndpointPool> {

    private String host;
    private int port;
    private int poolSize;


    @Override
    public SingleEndpointPool getObject() throws Exception {
        FilterChainBuilder filterChainBuilder = FilterChainBuilder.stateless();
        filterChainBuilder.add(new TransportFilter());
        filterChainBuilder.add(new StringFilter(Charset.forName("US-ASCII"), "\n"));
        filterChainBuilder.add(new ClientFilter());
        final TCPNIOTransport transport =
                TCPNIOTransportBuilder.newInstance().build();
        transport.setProcessor(filterChainBuilder.build());
        transport.start();
        SingleEndpointPool pool = SingleEndpointPool
                .builder(SocketAddress.class)
                .connectorHandler(transport)
                .endpointAddress(new InetSocketAddress(host, port))
                .maxPoolSize(poolSize)
                .reconnectDelay(5L, TimeUnit.SECONDS)
                .maxReconnectAttempts(Integer.MAX_VALUE)
                .keepAliveCheckInterval(5L, TimeUnit.SECONDS)
                .connectTimeout(5L, TimeUnit.SECONDS)
                .build();
        return pool;
    }

    @Override
    public Class<?> getObjectType() {
        return SingleEndpointPool.class;
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
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
