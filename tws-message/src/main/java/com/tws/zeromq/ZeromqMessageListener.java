package com.tws.zeromq;

/**
 * Created by admin on 1/31/2016.
 */
public interface ZeromqMessageListener {
    void onMessageReceived(String msg);
}
