package com.tws.cassandra.dao;

import com.datastax.driver.core.Session;
import com.tws.shared.ib.model.Tick;

/**
 * Created by yijunmao on 1/17/16.
 */

public class TickDAO extends CassandraBaseDAO<Tick> {

    private Session session;

    public TickDAO(Session session) {
        super(session, Tick.class);
    }
}
