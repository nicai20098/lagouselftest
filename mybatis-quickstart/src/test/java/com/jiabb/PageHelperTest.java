package com.jiabb;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
 * @description: 分页测试
 * @author: jia_b
 * @date: 2024/1/1 21:10
 * @since: 1.0
 */
public class PageHelperTest {

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
        PageHelper.startPage(1, 3);
        List<User> res = mapper.selectUserList();
        System.out.println(res);
        PageInfo<User> userPageInfo = new PageInfo<>(res);
        System.out.println(userPageInfo);
    }

}