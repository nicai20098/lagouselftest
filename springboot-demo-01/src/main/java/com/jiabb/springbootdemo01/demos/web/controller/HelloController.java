package com.jiabb.springbootdemo01.demos.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/11 20:56
 * @since: 1.0
 */
@RestController
public class HelloController {


    @RequestMapping("/demo")
    public String demo() {
        return "你好 springboot";
    }
}