package com.jiabb.util;

import com.jiabb.config.Configuration;
import com.jiabb.mapping.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2023/5/6 23:16
 * @since: 1.0
 */
public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;

    private Executor simpleExcutor = new SimpleExecutor();

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    //处理器对象
    @Override
    public <E> List<E> selectList(String statementId, Object... param)
            throws Exception {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        List<E> query = simpleExcutor.query(configuration, mappedStatement, param);
        return query;
    }

    //selectOne 中调用 selectList
    @Override
    public <T> T selectOne(String statementId, Object... params) throws
            Exception {
        List<Object> objects = selectList(statementId, params);
        if (objects.size() == 1) {
            return (T) objects.get(0);
        } else {
            throw new RuntimeException("返回结果过多");
        }
    }

    @Override
    public void close() throws SQLException {
        simpleExcutor.close();
    }
}
