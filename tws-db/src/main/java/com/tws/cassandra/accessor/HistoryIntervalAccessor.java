package com.tws.cassandra.accessor;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.datastax.driver.mapping.annotations.QueryParameters;
import com.google.common.util.concurrent.ListenableFuture;
import com.tws.cassandra.model.HistoryIntervalDB;


/**
 * Created by admin on 2/14/2016.
 */
@Accessor
public interface HistoryIntervalAccessor {

    @Query("SELECT * FROM history.time WHERE symbol = :symbol AND interval = :interval AND time >= :start AND time <= :end")
    @QueryParameters(consistency = "ONE")
    ListenableFuture<Result<HistoryIntervalDB>> getIntervalInTime(@Param("symbol") String symbol, @Param("interval") int interval, @Param("start") long start, @Param("end") long end);

    @Query("SELECT * FROM history.time WHERE symbol = :symbol AND interval = :interval AND time >= :start")
    @QueryParameters(consistency = "ONE")
    ListenableFuture<Result<HistoryIntervalDB>> getIntervalInTime(@Param("symbol") String symbol, @Param("interval") int interval, @Param("start") long start);

}
