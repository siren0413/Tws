package com.tws.iqfeed.listener;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tws.iqfeed.common.SequenceAction;
import com.tws.iqfeed.common.SequenceContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by yijunmao on 2/19/16.
 */
public class HistoryCmdThresholdAction implements SequenceAction<String>, InitializingBean {

    private Cache<String,Long> cache;
    private int expireSecond;

    @Override
    public boolean execute(SequenceContext ctx, String item) {
        Map<String,Long> map = cache.asMap();
        if(map.get(item)==null){
            map.put(item, System.currentTimeMillis());
            return true;
        }
        return false;
    }

    @Required
    public void setExpireSecond(int expireSecond) {
        this.expireSecond = expireSecond;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cache = CacheBuilder.newBuilder().refreshAfterWrite(expireSecond,TimeUnit.SECONDS).expireAfterWrite(expireSecond, TimeUnit.SECONDS).build();
    }
}
