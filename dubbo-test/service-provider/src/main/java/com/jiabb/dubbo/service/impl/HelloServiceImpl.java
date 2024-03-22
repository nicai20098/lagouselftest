package com.jiabb.dubbo.service.impl;

import com.jiabb.dubbo.service.HelloService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author jia_b
 * @description: TODO
 * @date: 2024/3/3 11:28
 * @since: 1.0
 */
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello:" + name;
    }
}