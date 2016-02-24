package com.tws.iqfeed.common;

/**
 * Created by admin on 1/30/2016.
 */
public class Command {

    // ***************** COMMON *******************
    public static class COMMON{
        public static String CONNECT(){
            return "S,CONNECT\r\n";
        }
        public static String SET_PROTOCOL(){
            return "S,SET PROTOCOL,5.2\r\n";
        }
    }

    // ***************** LEVEL 1 *******************
    public static class LEVEL1{
        public static String TIMESTAMP_ON(){
            return "S,TIMESTAMPSON\r\n";
        }
        public static String TIMESTAMP_OFF(){
            return "S,TIMESTAMPSOFF\r\n";
        }
        public static String NEWS_ON(){
            return "S,NEWSON\r\n";
        }
        public static String NEWS_OFF(){
            return "S,NEWSOFF\r\n";
        }
        public static String STATS(){
            return "S,REQUEST STATS\r\n";
        }
        public static String SELECT_UPDATE_FIELD(){
            return "S,SELECT UPDATE FIELDS,Bid,Bid Size,Bid Time,Ask,Ask Size,Ask Time,Last,Last Size,Last Time,Extended Trade,Extended Trade Date,Extended Trade Time,Extended Trade Size,Total Volume,Low,High,Open,Message Contents,Exchange ID,Delay\r\n";
        }

    }

}
