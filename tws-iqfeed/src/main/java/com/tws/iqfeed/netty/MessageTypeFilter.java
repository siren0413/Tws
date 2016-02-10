package com.tws.iqfeed.netty;

/**
 * Created by admin on 2/9/2016.
 */
public class MessageTypeFilter {

     enum TYPE{
        ERROR,
        SYMBOL_NOT_FOUND,
        NORMAL;
    }

    public static TYPE filterHistoryMsg(String msg){
        if(msg.startsWith("E,") || msg.contains(",E,")){
            return TYPE.ERROR;
        }
        return TYPE.NORMAL;
    }

    public static TYPE filterLevel1Msg(String msg){
        if(msg.startsWith("E,") || msg.contains(",E,")){
            return TYPE.ERROR;
        }
        if(msg.startsWith("n,")){
            return TYPE.SYMBOL_NOT_FOUND;
        }
        return TYPE.NORMAL;
    }
}
