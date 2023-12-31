package com.jiabb.sqlSession;

import com.jiabb.sqlSession.SqlSession;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2023/5/6 23:11
 * @since: 1.0
 */
public interface SqlSessionFactory {

    public SqlSession openSession();

}

