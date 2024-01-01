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
import java.util.Objects;

/**
 * @description: 缓存测试
 * @author: jia_b
 * @date: 2024/1/1 19:34
 * @since: 1.0
 */
public class CacheTest {

    private static UserMapper mapper;
    private static SqlSessionFactory build;
    @Before
    public void before() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        build = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = build.openSession(true);
        mapper = sqlSession.getMapper(UserMapper.class);
    }

    @Test
    public void firstLevelCache() {
        User user = new User();
        user.setId(1);
        User user1 = mapper.findByCondition(user);

        user.setUsername("zhangsan");
        mapper.updateUser(user);

        User user2 = mapper.findByCondition(user);
        System.out.println(Objects.equals(user1, user2));
    }

    @Test
    public void secondLevelCache() {
        SqlSession sqlSession1 = build.openSession();
        SqlSession sqlSession2 = build.openSession();
        SqlSession sqlSession3 = build.openSession();

        UserMapper mapper1 = sqlSession1.getMapper(UserMapper.class);
        UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);
        UserMapper mapper3 = sqlSession3.getMapper(UserMapper.class);

        User user = new User();
        user.setId(1);
        User user1 = mapper1.findByCondition(user);
        sqlSession1.close();

//        user.setUsername("zhangsan1");
//        mapper3.updateUser(user);
//        sqlSession3.commit();

        User user2 = mapper2.findByCondition(user);
        System.out.println(Objects.equals(user1, user2));
        System.out.println(user1 == user2);
    }

}