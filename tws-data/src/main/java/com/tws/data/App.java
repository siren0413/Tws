package com.tws.data;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public Connection connection;
    final static String ALL_GENERIC_TICK_TAGS = "100,101,104,105,106,107,165,221,225,233,236,258,293,294,295,318";

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("data-spring.xml");
        App app = (App) context.getBean("app");
        app.connection.connect();
        EClientSocket client = app.connection.getClient();
        client.reqCurrentTime();
        Contract contract = new Contract();
        contract.secType(Types.SecType.STK);
        contract.currency("USD");
        contract.symbol("DJX");
        contract.exchange("SMART");
        contract.primaryExch("ARCA");
        client.reqMktData(1, contract, App.ALL_GENERIC_TICK_TAGS, false, null);
        //client.reqMktDepth(1, contract, 50, null);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
