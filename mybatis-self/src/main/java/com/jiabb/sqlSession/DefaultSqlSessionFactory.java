package com.jiabb.sqlSession;

import com.jiabb.config.Configuration;
import com.jiabb.sqlSession.DefaultSqlSession;
import com.jiabb.sqlSession.SqlSession;
import com.jiabb.sqlSession.SqlSessionFactory;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2023/5/6 23:12
 * @since: 1.0
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
