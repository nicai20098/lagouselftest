package com.jiabb.config;

import com.jiabb.mapping.MappedStatement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 配置类解析
 * @author: jia_b
 * @date: 2023/5/6 22:57
 * @since: 1.0
 */
public class Configuration {

    //数据源
    private DataSource dataSource;
    //map集合： key:statementId value:MappedStatement
    private Map<String, MappedStatement> mappedStatementMap = new HashMap<>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }

    public void setMappedStatementMap(Map<String, MappedStatement>
                                              mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }

}
