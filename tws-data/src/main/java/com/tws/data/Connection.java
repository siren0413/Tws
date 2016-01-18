package com.tws.data;

import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.client.EWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chris on 1/17/16.
 */
public class Connection implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(Connection.class);

    private EClientSocket client;
    private boolean asyncConnection;
    private String host = "localhost";
    private int port = 7496;
    private int clientId = 1;

    private EWrapper wrapper;
    private EReader reader;
    private EReaderSignal signal;

    private ExecutorService executorService;
    private int poolSize;


    public void connect() {
        if (client.isConnected()) {
            return;
        }
        client.eConnect(host, port, clientId);
        if (client.isConnected()) {
            logger.info("connected to TWS server, version [{}], time [{}]", client.serverVersion(), client.TwsConnectionTime());
        }
        reader = new EReader(client, signal);
        reader.start();
        executorService.execute(new MessageWorker());
        if (client.isAsyncEConnect()) {
            client.startAPI();
        }
    }

    public void disconnect() {
        if (client.isConnected()) {
            client.eDisconnect();
            logger.info("disconnected from TWS server.");
        }
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    class MessageWorker implements Runnable {
        @Override
        public void run() {
            while (client.isConnected()) {
                signal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (IOException e) {
                    logger.error("Error process messages.", e);
                }
            }

        }
    }

    public void afterPropertiesSet() throws Exception {
        logger.info("connection initialized as following:");
        logger.info("host = {}", host);
        logger.info("port = {}", port);
        logger.info("client_id = {}", clientId);
        logger.info("connection_pool_size = {}", poolSize);
        client = new EClientSocket(wrapper, signal);
        client.setAsyncEConnect(asyncConnection);
        executorService = Executors.newFixedThreadPool(poolSize);
    }

    @Required
    public void setWrapper(EWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Required
    public void setSignal(EReaderSignal signal) {
        this.signal = signal;
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
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Required
    public void setAsyncConnection(boolean asyncConnection) {
        this.asyncConnection = asyncConnection;
    }

    @Required
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public EClientSocket getClient() {
        return client;
    }
}
