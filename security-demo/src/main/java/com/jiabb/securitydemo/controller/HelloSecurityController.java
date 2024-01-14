package com.jiabb.securitydemo.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: security 入门
 * @author: jia_b
 * @date: 2024/1/14 0:05
 * @since: 1.0
 */
@RestController
public class HelloSecurityController {


    @RequestMapping("/helloSecurity")
    public String hello() {
        return "hello security";
    }

}