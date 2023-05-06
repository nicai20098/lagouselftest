package com.jiabb.util;

import com.jiabb.config.Configuration;
import com.jiabb.mapping.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2023/5/6 23:23
 * @since: 1.0
 */
public interface Executor {

    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object[] param) throws Exception;
    void close() throws SQLException;

}
