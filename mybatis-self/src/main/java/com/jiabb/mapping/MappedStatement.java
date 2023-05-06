package com.jiabb.mapping;

/**
 * @description: mapping解析类
 * @author: jia_b
 * @date: 2023/5/6 22:59
 * @since: 1.0
 */
public class MappedStatement {

    //id
    private String id;
    //sql语句
    private String sql;
    //输入参数
    private Class<?> paramterType;
    //输出参数
    private Class<?> resultType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Class<?> getParamterType() {
        return paramterType;
    }

    public void setParamterType(Class<?> paramterType) {
        this.paramterType = paramterType;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }
}
