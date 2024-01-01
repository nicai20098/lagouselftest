package com.jiabb.sqlSession;

import com.jiabb.config.Configuration;
import com.jiabb.io.Resources;
import com.jiabb.mapping.MappedStatement;
import com.jiabb.sqlSession.SqlSession;
import com.jiabb.util.Executor;
import com.jiabb.util.SimpleExecutor;

import java.io.InputStream;
import java.lang.reflect.*;
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
        return simpleExcutor.query(configuration, mappedStatement, param);
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

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        //使用动态代理为dao生成代理对象
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //findAll
                String methodName = method.getName();
                //className = com.jiabb.dao.UserDao
                String className = method.getDeclaringClass().getName();
                String statementId = className + "." + methodName;
                //返回值类型
                Type genericReturnType = method.getGenericReturnType();
                //判断是否进行了 泛化类型参数化
                if (genericReturnType instanceof ParameterizedType) {
                    return selectList(statementId, args);
                } else {
                    return selectOne(statementId, args);
                }
            }
        });
        return (T) proxyInstance;
    }
}
