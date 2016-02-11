package com.tws.iqfeed.netty;

/**
 * Created by admin on 2/9/2016.
 */
public class MessageTypeFilter {

    enum TYPE {
        ERROR,
        SYMBOL_NOT_FOUND,
        NORMAL;
    }

    public static TYPE filterHistoryMsg(String msg) {
        String[] parts = msg.split(",");
        if (parts.length > 0 && parts[0].equals("E")) {
            return TYPE.ERROR;
        }
        if (parts.length > 1 && parts[1].equals("E")) {
            return TYPE.ERROR;
        }
        return TYPE.NORMAL;
    }

    public static TYPE filterLevel1Msg(String msg) {
        String[] parts = msg.split(",");
        if (parts.length > 0 && parts[0].equals("E")) {
            return TYPE.ERROR;
        }
        if (parts.length > 1 && parts[1].equals("E")) {
            return TYPE.ERROR;
        }
        if (parts.length > 0 && parts[0].equals("n")) {
            return TYPE.SYMBOL_NOT_FOUND;
        }
        return TYPE.NORMAL;
    }
}
