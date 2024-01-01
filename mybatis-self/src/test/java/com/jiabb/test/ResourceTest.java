package com.jiabb.test;

import com.jiabb.dao.UserMapper;
import com.jiabb.entity.User;
import com.jiabb.io.Resources;
import com.jiabb.sqlSession.SqlSession;
import com.jiabb.sqlSession.SqlSessionFactory;
import com.jiabb.sqlSession.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class ResourceTest {

    @Test
    public void test001() throws Exception {
        //加载配置文件
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");

        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsSteam);

        SqlSession sqlSession = build.openSession();
        List<Object> zhangSan = sqlSession.selectList("user.selectList");
        System.out.println(zhangSan);
    }

    @Test
    public void test002() throws ClassNotFoundException {
        Class<?> aClass = Class.forName("com.jiabb.entity.User");
        System.out.println(aClass);
    }

    @Test
    public void test003() throws Exception {
        //加载配置文件
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");

        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsSteam);

        SqlSession sqlSession = build.openSession();
        User user = new User();
        user.setId(1);
        user.setUsername("张三");
        User zhangSan = sqlSession.selectOne("user.selectOne", user);
        System.out.println(zhangSan);
    }

    @Test
    public void test004() throws Exception {
        //加载配置文件
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = build.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> all = mapper.selectList();
        System.out.println(all);

    }

    @Test
    public void test005() throws Exception {
        //加载配置文件
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = build.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setId(1);
        user.setUsername("张三");
        User user1 = mapper.selectOne(user);
        System.out.println(user1);

    }
}
