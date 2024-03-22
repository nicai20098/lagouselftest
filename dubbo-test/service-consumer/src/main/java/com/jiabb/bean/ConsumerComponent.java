package com.jiabb.bean;

import com.jiabb.dubbo.service.HelloService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * @author jia_b
 * @description: TODO
 * @date: 2024/3/3 12:10
 * @since: 1.0
 */
@Component
public class ConsumerComponent {

    @Reference
    private HelloService helloService;

    public String sayHelle(String name) {
        return helloService.sayHello(name);
    }

}