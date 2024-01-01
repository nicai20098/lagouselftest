package com.jiabb;

import com.jiabb.dao.UserMapper;
import com.jiabb.entity.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @description: 通用mapper测试
 * @author: jia_b
 * @date: 2024/1/1 21:30
 * @since: 1.0
 */
public class CommonMapperTest {

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
        User user = new User();
        user.setId(1);
        User user1 = mapper.selectOne(user);
        System.out.println(user1);
    }

    /**
     * example 方法
     */
    @Test
    public void test002() {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("id", 1);
        List<User> res = mapper.selectByExample(example);
        System.out.println(res);
    }

}