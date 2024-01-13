package com.jiabb.springbootdemo01;

import com.jiabb.pojo.SimpleBean;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest  //标记该类为Spring boot单元测试类 并加载项目的applicationContext上下文
class SpringbootDemo01ApplicationTests {

    @Autowired
    private SimpleBean simpleBean;
    @Test
    void contextLoads() {
        System.out.println(simpleBean);
    }

}
