package com.jiabb.sqlSession;

import java.sql.SQLException;
import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2023/5/6 23:14
 * @since: 1.0
 */
public interface SqlSession {

    public <E> List<E> selectList(String statementId, Object... param) throws Exception;

    public <T> T selectOne(String statementId, Object... params) throws Exception;

    public void close() throws SQLException;

}
