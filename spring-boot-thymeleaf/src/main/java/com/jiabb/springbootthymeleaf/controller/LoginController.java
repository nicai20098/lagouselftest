package com.jiabb.springbootthymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/13 21:07
 * @since: 1.0
 */
@Controller
public class LoginController {

    @RequestMapping("toLoginPage")
    public String toLoginPage(Model model) {
        model.addAttribute("currentYear", Calendar.getInstance().get(Calendar.YEAR));
        return "login";
    }
}