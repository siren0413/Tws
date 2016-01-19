package com.tws.data;

import com.ib.client.EClientSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by chris on 1/18/16.
 */
public class ClientFactory implements FactoryBean<EClientSocket>{

    private static final Logger logger = LoggerFactory.getLogger(ClientFactory.class);

    private Connection connection;

    public EClientSocket getObject() throws Exception {
        if(!connection.isConnected()){
            connection.connect();
        }
        while(!connection.isConnected()){
            logger.error("not connected to tws, wait 5 sec and try again...");
            Thread.sleep(5000);
            if(!connection.isConnected()) {
                connection.connect();
            }
        }
        return connection.getClient();
    }

    public Class<?> getObjectType() {
        return EClientSocket.class;
    }

    public boolean isSingleton() {
        return true;
    }

    @Required
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
