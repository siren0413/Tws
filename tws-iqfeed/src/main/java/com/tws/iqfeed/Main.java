package com.tws.iqfeed;

import com.tws.controller.HistorySocketController;
import com.tws.iqfeed.common.Command;
import com.tws.iqfeed.netty.HistorySocket;
import com.tws.iqfeed.socket.SocketConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by admin on 1/29/2016.
 */

//
public class Main {
    public static void main(String[] args) throws IOException {
//        Runtime.getRuntime().exec("IQConnect.exe -product YIJUN_MAO_11967 -version 1.0 ");
//        SocketConnection connection1 = new SocketConnection("127.0.0.1", 9300);
//        connection1.connect(true);
//        connection1.send("S,CONNECT\r\n");
//        connection1.send("S,CONNECT\r\n");
//        connection1.send("S,CONNECT\r\n");

//        String sCommand = String.format("HTX,%s,%s,%s,%s,%s\r\n", "AAPL", "", "", "", "");
//        connection1.send(sCommand);
//
//        SocketConnection connection2 = new SocketConnection("127.0.0.1", 9100);
//        connection2.connect(true);
//        String sCommand2 = String.format("HTX,%s,%s,%s,%s,%s\r\n", "FB", "", "", "", "");
//        connection2.send(sCommand2);
//
//        Connection connection2 = new SocketConnection("127.0.0.1", 9300);
//        connection2.connect();
//        connection2.send("S,CONNECT\r\n");
//
//        Connection connection3 = new SocketConnection("127.0.0.1", 9300);
//        connection3.connect();
//        connection3.send("S,CONNECT\r\n");
//        SpringApplication.run(Main.class, args);
        ApplicationContext ctx = new ClassPathXmlApplicationContext("iqfeed-spring.xml");
        ctx.getBean("level1Socket");
        HistorySocket socket = (HistorySocket) ctx.getBean("historySocket");
//        socket.send(Command.Level1.SET_PROTOCOL());
//        socket.send("HTX,AAPL,,,AAPL,\r\n");
//        socket.send("HTX,FB,,,FB,\r\n");

//        ApplicationContext context = SpringApplication.run(Main.class, args);
    }
}
