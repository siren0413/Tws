package com.tws.shared.common;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by chris on 2/25/16.
 */
public class TimeUtils {

    public static DateTimeFormatter dateTimeMicroSecFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    public static DateTimeFormatter dateTimeMilliSecFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static DateTimeFormatter dateTimeSecFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static ZoneId ZONE_EST = ZoneId.of("America/New_York");
}
