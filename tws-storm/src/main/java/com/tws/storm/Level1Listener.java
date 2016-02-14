package com.tws.storm;

import java.util.List;

/**
 * Created by admin on 2/13/2016.
 */
public class Level1Listener {

    public void onMessageReceived(List<String> msg){
        System.out.println(msg);
    }

}
