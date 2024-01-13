package com.jiabb.config;

import com.jiabb.pojo.SimpleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/12 23:14
 * @since: 1.0
 */
@Configuration
@ConditionalOnClass //当类路径classpath下有指定的类的情况, 就会进行自动配置
public class MyAutoConfiguration {

    static {
        System.out.println("MyAutoConfiguration init...");
    }

    @Bean
    public SimpleBean simpleBean() {
        return new SimpleBean();
    }

}