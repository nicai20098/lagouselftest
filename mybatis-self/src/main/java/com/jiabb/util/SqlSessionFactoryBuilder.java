package com.jiabb.util;

import com.jiabb.config.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2023/5/6 23:03
 * @since: 1.0
 */
public class SqlSessionFactoryBuilder {

    private Configuration configuration;

    public SqlSessionFactoryBuilder() {
        this.configuration = new Configuration();
    }

    public SqlSessionFactory build(InputStream inputStream) throws
            DocumentException, PropertyVetoException, ClassNotFoundException {
//1.解析配置文件，封装Configuration
        XMLConfigerBuilder xmlConfigerBuilder = new
                XMLConfigerBuilder(configuration);
        Configuration configuration =
                xmlConfigerBuilder.parseConfiguration(inputStream);
//2.创建 sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return sqlSessionFactory;
    }

}
