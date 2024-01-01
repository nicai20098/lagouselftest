package com.jiabb;

import com.jiabb.dao.OrderMapper;
import com.jiabb.dao.UserMapper;
import com.jiabb.entity.Order;
import com.jiabb.entity.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/1 11:42
 * @since: 1.0
 */
public class MybatisTest {

    @Test
    public void test001() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        List<User> objects = sqlSession.selectList("user.findAll");
        System.out.println(objects);
        sqlSession.close();
    }

    @Test
    public void test002() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        User user = new User();
        user.setId(8);
        user.setUsername("小八");
        sqlSession.insert("user.add", user);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void test003() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        User user = new User();
        user.setId(8);
        user.setUsername("小八八");
        sqlSession.update("user.update", user);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void test004() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        sqlSession.delete("user.delete", 8);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void test005() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> res = mapper.findAll();
        System.out.println(res);
    }

    @Test
    public void test006() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setUsername("张三");
        User res = mapper.findByCondition(user);
        System.out.println(res);
    }

    @Test
    public void test007() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<Integer> list = Arrays.asList(1, 2);
        List<User> res = mapper.findByIds(list);
        System.out.println(res);
    }

    @Test
    public void test008() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
        List<Order> res = mapper.findOrderAndUser();
        System.out.println(res);
    }

    @Test
    public void test009() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> res = mapper.findUserAndOrder();
        System.out.println(res);
    }

    @Test
    public void test0010() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> res = mapper.findAllUserAndRole();
        System.out.println(res);
    }

}