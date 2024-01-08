package com.jiabb.demo.service.impl;

import com.jiabb.mvcframework.annotations.LocalService;
import com.jiabb.demo.service.DemoService;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/7 23:30
 * @since: 1.0
 */
@LocalService
public class DemoServiceImpl implements DemoService {
    @Override
    public String get(String name) {
        return name;
    }
}