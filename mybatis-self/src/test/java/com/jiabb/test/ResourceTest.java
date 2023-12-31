package com.jiabb.test;

import com.jiabb.io.Resources;
import com.jiabb.sqlSession.SqlSessionFactory;
import com.jiabb.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class ResourceTest {

    @Test
    public void test001() throws PropertyVetoException, DocumentException, ClassNotFoundException {
        //加载配置文件
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");

        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsSteam);


    }

    @Test
    public void test002() throws ClassNotFoundException {
        Class<?> aClass = Class.forName("com.jiabb.entity.User");
        System.out.println(aClass);
    }
}
