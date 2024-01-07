package com.jiabb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/7 16:52
 * @since: 1.0
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/handle01")
    public ModelAndView handle01() {
        Date date = new Date();
        ModelAndView modelAndView = new ModelAndView();
        // 向请求域 添加属性
        modelAndView.addObject("date", date);
        modelAndView.setViewName("success");
        return modelAndView;
    }

    @RequestMapping("/handle02")
    public String handle02(ModelMap modelMap) {
        Date date = new Date();
        modelMap.addAttribute("date", date);
        return "success";
    }

    @RequestMapping("/handle03")
    public String handle03(Model model) {
        Date date = new Date();
        model.addAttribute("date", date);
        return "success";
    }

    @RequestMapping("/handle04")
    public String handle04(Map<String, Object> map) {
        Date date = new Date();
        map.put("date", date);
        return "success";
    }

}