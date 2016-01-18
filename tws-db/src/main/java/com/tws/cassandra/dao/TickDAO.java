package com.tws.cassandra.dao;

import com.datastax.driver.core.Session;
import com.tws.cassandra.model.Tick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * Created by yijunmao on 1/17/16.
 */

@Repository
public class TickDAO extends CassandraBaseDAO<Tick> {

    @Autowired
    @Qualifier("stockSession")
    private Session session;

    public TickDAO(Session session) {
        super(session, Tick.class);
    }
}
