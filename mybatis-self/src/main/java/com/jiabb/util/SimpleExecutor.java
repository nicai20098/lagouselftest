package com.jiabb.util;

import com.jiabb.config.Configuration;
import com.jiabb.mapping.MappedStatement;
import com.jiabb.mapping.ParameterMapping;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2023/5/6 23:25
 * @since: 1.0
 */
public class SimpleExecutor implements Executor {

    private Connection connection = null;

    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object[] param) throws SQLException, NoSuchFieldException, IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException {
        //1.注册驱动，获取连接
        connection = configuration.getDataSource().getConnection();
        // select * from user where id = #{id} and username = #{username}
        String sql = mappedStatement.getSql();
        //对sql进行处理
        BoundSql boundsql = getBoundSql(sql);
        // select * from where id = ? and username = ?
        String finalSql = boundsql.getSqlText();
        //获取传入参数类型
        Class<?> paramterType = mappedStatement.getParamterType();
        //获取预编译preparedStatement对象
        PreparedStatement preparedStatement = connection.prepareStatement(finalSql);
        List<ParameterMapping> parameterMappingList = boundsql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String name = parameterMapping.getContent();
            //反射
            Field declaredField = paramterType.getDeclaredField(name);
            declaredField.setAccessible(true);
            //参数的值
            Object o = declaredField.get(param[0]);

            //给占位符赋值
            preparedStatement.setObject(i + 1, o);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        Class<?> resultType = mappedStatement.getResultType();
        List<E> results = new ArrayList<>();
        while (resultSet.next()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            E o = (E) resultType.newInstance();
            for (int i = 1; i <= columnCount; i++) {
                //属性名
                String columnName = metaData.getColumnName(i);
                //属性值
                Object value = resultSet.getObject(columnName);
                //创建属性描述器，为属性生成读写方法
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultType);
                //获取写方法
                Method writeMethod = propertyDescriptor.getWriteMethod();
                //向类中写入值
                writeMethod.invoke(o, value);
            }
            results.add(o);
        }
        return results;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    private BoundSql getBoundSql(String sql) {
        //标记处理类：主要是配合通用标记解析器GenericTokenParser类完成对配置文件等的解析工作，其中TokenHandler主要完成处理
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        //GenericTokenParser :通用的标记解析器，完成了代码片段中的占位符的解析，然后再根据给定的标记处理器(TokenHandler) 来进行表达式的处理
        //三个参数：分别为openToken (开始标记)、closeToken (结束标记)、handler (标记处 理器)
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String parse = genericTokenParser.parse(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        return new BoundSql(parse, parameterMappings);
    }
}