package com.jiabb.spring;

import com.jiabb.entity.User;
import com.jiabb.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/2 23:57
 * @since: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MpTest {

    @Resource
    private UserMapper userMapper;



    @Test
    public void test001() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

}