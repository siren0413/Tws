package com.tws.shared;

import com.tws.shared.marshall.TickerMarshaller;

/**
 * Created by chris on 1/18/16.
 */
public enum MsgType {

    TICK(0, TickerMarshaller.class, new TickerMarshaller()),
    UNKNOWN(Integer.MAX_VALUE, null, null);

    private int index;
    private Class clazz;
    private Object marshaller;

    private MsgType(int index, Class clazz, Object marshaller) {
        this.index = index;
        this.clazz = clazz;
        this.marshaller = marshaller;
    }

    public int index() {
        return index;
    }

    public Class clazz() {
        return clazz;
    }

    public Object marshaller(){
        return marshaller;
    }

    public static MsgType get(int index) {
        for (MsgType type : values()) {
            if (type.index == index) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
