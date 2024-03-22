package com.jiabb;

import com.jiabb.bean.ConsumerComponent;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

/**
 * @author jia_b
 * @description: TODO
 * @date: 2024/3/3 12:14
 * @since: 1.0
 */
public class AnnotationConsumerMain {


    public static void main(String[] args) throws IOException {

        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        annotationConfigApplicationContext.start();

        ConsumerComponent service = annotationConfigApplicationContext.getBean(ConsumerComponent.class);
        while (true) {
            System.in.read();
            String s = service.sayHelle("world");
            System.out.println("result:" + s);
        }

    }

    @Configuration
    @EnableDubbo
    @PropertySource("classpath:/dubbo-consumer.properties")
    @ComponentScan(basePackages = "com.jiabb.bean")
    static class ConsumerConfiguration {

    }

}