package com.tws.zeromq;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by admin on 1/31/2016.
 */
public class MessageListenerImpl implements ZeromqMessageListener{
    @Override
    public void onMessageReceived(String msg) {

//        String[] split = msg.split(" ");
        int start = msg.indexOf(" ");
//        System.out.println(msg.substring(start));
        try {
            Files.write(Paths.get("haha.txt"),msg.substring(start).getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
