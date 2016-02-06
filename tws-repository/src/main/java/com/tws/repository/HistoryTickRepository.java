package com.tws.repository;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Repository;


/**
 * Created by admin on 2/2/2016.
 */

@Repository
public class HistoryTickRepository {

    @Autowired
    private HbaseTemplate template;

    private static final String tableName = "HistoryTick";
    private static final byte[] CF = Bytes.toBytes("cf");

    private static final byte[] ID  = Bytes.toBytes("id");
    private static final byte[] TIMESTAMP  = Bytes.toBytes("ts");
    private static final byte[] LAST = Bytes.toBytes("l");
    private static final byte[] LAST_SIZE  = Bytes.toBytes("ls");
    private static final byte[] TOTAL_VOLUME  = Bytes.toBytes("tv");
    private static final byte[] BID  = Bytes.toBytes("b");
    private static final byte[] ASK  = Bytes.toBytes("a");
    private static final byte[] TICK_ID  = Bytes.toBytes("tid");
    private static final byte[] BASIS_FOR_LAST  = Bytes.toBytes("basis");
    private static final byte[] MARKET_CENTER  = Bytes.toBytes("mkt");
    private static final byte[] TRADE_CONDITION  = Bytes.toBytes("cnd");


    public HistoryTick save(final HistoryTick historyTick){
        return template.execute(tableName, new TableCallback<HistoryTick>() {
            @Override
            public HistoryTick doInTable(HTableInterface hTableInterface) throws Throwable {
                Put put = new Put(Bytes.toBytes(historyTick.getTimestamp()));
                put.addColumn(CF, ID, Bytes.toBytes(historyTick.getId()));
                put.addColumn(CF, TIMESTAMP, Bytes.toBytes(historyTick.getTimestamp()));
                put.addColumn(CF, LAST, Bytes.toBytes(historyTick.getLast()));
                put.addColumn(CF, LAST_SIZE, Bytes.toBytes(historyTick.getLastSize()));
                put.addColumn(CF, TOTAL_VOLUME, Bytes.toBytes(historyTick.getTotalVolume()));
                put.addColumn(CF, BID, Bytes.toBytes(historyTick.getBid()));
                put.addColumn(CF, ASK, Bytes.toBytes(historyTick.getAsk()));
                put.addColumn(CF, TICK_ID, Bytes.toBytes(historyTick.getTickId()));
                put.addColumn(CF, BASIS_FOR_LAST, Bytes.toBytes(historyTick.getBasis()));
                put.addColumn(CF, MARKET_CENTER, Bytes.toBytes(historyTick.getMarket()));
                put.addColumn(CF, TRADE_CONDITION, Bytes.toBytes(historyTick.getCondition()));
                hTableInterface.put(put);
                return historyTick;
            }
        });
    }

}
