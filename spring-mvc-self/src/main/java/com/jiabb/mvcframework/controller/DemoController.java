package com.jiabb.mvcframework.controller;

import com.jiabb.mvcframework.annotations.LocalAutowired;
import com.jiabb.mvcframework.annotations.LocalController;
import com.jiabb.mvcframework.annotations.LocalRequestMapping;
import com.jiabb.mvcframework.service.DemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Retention;

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


    public String query(HttpServletRequest request, HttpServletResponse response, String name) {
        return demoService.get(name);
    }

}