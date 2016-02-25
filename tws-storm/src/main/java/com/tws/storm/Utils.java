package com.tws.storm;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by chris on 2/24/16.
 */
public class Utils {
    public static ZonedDateTime currentZonedDateTime = ZonedDateTime.now(ZoneId.of("America/New_York"));

    public static ZonedDateTime getCurrentZonedDateTime(boolean mock){
        if(mock){
            return currentZonedDateTime;
        }else{
            return ZonedDateTime.now(ZoneId.of("America/New_York"));
        }
    }
}
