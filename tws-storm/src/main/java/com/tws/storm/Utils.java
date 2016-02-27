package com.tws.storm;

import com.tws.shared.common.TimeUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by chris on 2/24/16.
 */
public class Utils {
    public static ZonedDateTime currentZonedDateTime = ZonedDateTime.now(TimeUtils.ZONE_EST);

    public static ZonedDateTime getCurrentZonedDateTime(boolean mock){
        if(mock){
            return currentZonedDateTime;
        }else{
            return ZonedDateTime.now(TimeUtils.ZONE_EST);
        }
    }
}
