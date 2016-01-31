package com.tws;

import com.tws.message.MessageListener;

/**
 * Created by admin on 1/30/2016.
 */
public class TestMessageListener implements MessageListener {
    @Override
    public void messageReceived(String msg) {
        System.out.println(msg);
    }
}
