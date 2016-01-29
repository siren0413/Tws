package com.tws.iqfeed;

import java.io.IOException;

/**
 * Created by admin on 1/29/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().exec("IQConnect.exe -product YIJUN_MAO_11967 -version 1.0 ");
        Connection connection1 = new SocketConnection("127.0.0.1", 9300);
        connection1.connect();
        connection1.send("S,CONNECT\r\n");


        Connection connection2 = new SocketConnection("127.0.0.1", 9300);
        connection2.connect();
        connection2.send("S,CONNECT\r\n");

        Connection connection3 = new SocketConnection("127.0.0.1", 9300);
        connection3.connect();
        connection3.send("S,CONNECT\r\n");

    }
}
