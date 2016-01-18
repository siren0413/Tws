package com.tws.cassandra.dao;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by yijunmao on 1/17/16.
 */

public abstract class CassandraBaseDAO<T> implements InitializingBean {

    private Mapper<T> mapper;
    private Session session;
    private Class<T> type;

    public void save(T entity) {
        mapper.save(entity);
    }

    public T get(T entity) {
        return mapper.get(entity);
    }

    public CassandraBaseDAO(Session session, Class<T> type) {
        this.session = session;
        this.type = type;
    }

    public void afterPropertiesSet() throws Exception {
        MappingManager manager = new MappingManager(session);
        mapper = manager.mapper(type);
        Mapper.Option consistencyLevel = Mapper.Option.consistencyLevel(ConsistencyLevel.ANY);
        mapper.setDefaultSaveOptions(consistencyLevel);
        mapper.setDefaultGetOptions(consistencyLevel);
        mapper.setDefaultDeleteOptions(consistencyLevel);
    }
}
