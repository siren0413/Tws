package com.tws.iqfeed.common;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yijunmao on 2/19/16.
 */
public class SequenceContext {

    private ConcurrentHashMap<String, Object> map = new ConcurrentHashMap();

    public Object get(String key){
        return map.get(key);
    }

    public void set(String key, Object obj){
        map.put(key, obj);
    }

}
