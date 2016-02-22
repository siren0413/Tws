package com.tws.cassandra.repo;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Param;
import com.google.common.util.concurrent.ListenableFuture;
import com.tws.cassandra.accessor.HistoryIntervalAccessor;
import com.tws.cassandra.model.HistoryIntervalDB;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by admin on 2/14/2016.
 */
public class HistoryIntervalRepository implements InitializingBean, HistoryIntervalAccessor {

    @Autowired
    @Qualifier("historySession")
    private Session session;

    private Mapper<HistoryIntervalDB> mapper;
    private HistoryIntervalAccessor accessor;

    @Override
    public void afterPropertiesSet() throws Exception {
        MappingManager manager = new MappingManager(session);
        mapper = manager.mapper(HistoryIntervalDB.class);
        mapper.setDefaultGetOptions(Mapper.Option.consistencyLevel(ConsistencyLevel.ANY));
        mapper.setDefaultSaveOptions(Mapper.Option.consistencyLevel(ConsistencyLevel.ANY));
        accessor = manager.createAccessor(HistoryIntervalAccessor.class);
    }

    @Override
    public ListenableFuture<Result<HistoryIntervalDB>> getIntervalInTime(@Param("symbol") String symbol, @Param("interval") int interval, @Param("start") long start, @Param("end") long end) {
        return accessor.getIntervalInTime(symbol, interval, start, end);
    }

    @Override
    public ListenableFuture<Result<HistoryIntervalDB>> getIntervalInTime(@Param("symbol") String symbol, @Param("interval") int interval, @Param("start") long start) {
        return accessor.getIntervalInTime(symbol, interval, start);
    }

    @Override
    public ListenableFuture<Result<HistoryIntervalDB>> getIntervalInTime(@Param("symbol") String symbol, @Param("interval") int interval, @Param("start") long start, @Param("dataPoints") int dataPoints) {
        return accessor.getIntervalInTime(symbol, interval, start, dataPoints);
    }

    @Override
    public HistoryIntervalDB getMostRecentRecordInTime(@Param("symbol") String symbol, @Param("interval") int interval) {
        return accessor.getMostRecentRecordInTime(symbol, interval);
    }

    public void saveIntervalInTimeAsync(HistoryIntervalDB historyIntervalDB){
        mapper.saveAsync(historyIntervalDB);
    }

    public void saveIntervalInTime(HistoryIntervalDB historyIntervalDB){
        mapper.save(historyIntervalDB);
    }

}
