package com.jiabb.demo.controller;

import com.jiabb.mvcframework.annotations.LocalAutowired;
import com.jiabb.mvcframework.annotations.LocalController;
import com.jiabb.mvcframework.annotations.LocalRequestMapping;
import com.jiabb.demo.service.DemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/7 23:29
 * @since: 1.0
 */
@LocalController
@LocalRequestMapping("/demo")
public class DemoController {

    @LocalAutowired
    private DemoService demoService;


    @LocalRequestMapping("/query")
    public String query(HttpServletRequest request, HttpServletResponse response, String name) {
        return demoService.get(name);
    }

}