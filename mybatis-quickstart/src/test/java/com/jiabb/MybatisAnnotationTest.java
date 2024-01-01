package com.jiabb;

import com.jiabb.dao.UserMapper;
import com.jiabb.entity.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @description: mybatis 注解测试
 * @author: jia_b
 * @date: 2024/1/1 14:12
 * @since: 1.0
 */
public class MybatisAnnotationTest {

    private static UserMapper mapper;
    @Before
    public void before() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession(true);
        mapper = sqlSession.getMapper(UserMapper.class);
    }

    @Test
    public void test001() {
        List<User> res = mapper.selectUserList();
        System.out.println(res);
    }

    @Test
    public void test002() {
        User user = new User();
        user.setId(6);
        user.setUsername("xiaoxiao");
        Integer res = mapper.addUser(user);
        System.out.println(res);
    }

    @Test
    public void test003() {
        User user = new User();
        user.setId(6);
        user.setUsername("xiaoxiao1");
        Integer res = mapper.updateUser(user);
        System.out.println(res);
    }

    @Test
    public void test004() {
        Integer res = mapper.deleteUser(6);
        System.out.println(res);
    }


}