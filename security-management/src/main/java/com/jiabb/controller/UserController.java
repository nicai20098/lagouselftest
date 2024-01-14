package com.jiabb.controller;

import com.jiabb.domain.User;
import com.jiabb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户处理类
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 获取当前登录用户
     */
    @RequestMapping("/loginUser1")
    @ResponseBody
    public UserDetails getCurrentUser() {
        return (UserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取当前登录用户 *
     *
     * @return
     */
    @RequestMapping("/loginUser2")
    @ResponseBody
    public UserDetails getCurrentUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails;
    }

    /**
     * 获取当前登录用户 *
     *
     * @return
     */
    @RequestMapping("/loginUser3")
    @ResponseBody
    public UserDetails getCurrentUser(@AuthenticationPrincipal UserDetails
                                              userDetails) {
        return userDetails;
    }

    /**
     * 查询所有用户
     *
     * @return
     */
    @RequestMapping("/findAll")
    public String findAll(Model model) {
        List<User> userList = userService.list();
        model.addAttribute("userList", userList);
        return "user_list";
    }

    /**
     * 查询所有用户-返回json数据
     *
     * @return
     */
    @RequestMapping("/findAllTOJson")
    @ResponseBody
    public List<User> findAllTOJson() {
        List<User> userList = userService.list();
        return userList;
    }

    /**
     * 用户修改页面跳转
     *
     * @return
     */
    @RequestMapping("/update/{id}")
    public String update(@PathVariable Integer id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "user_update";
    }

    /**
     * 用户添加或修改
     *
     * @return
     */
    @PostMapping("/saveOrUpdate")
    public String saveOrUpdate(User user) {
        userService.saveOrUpdate(user);
        return "redirect:/user/findAll";
    }

    /**
     * 用户添加页面跳转
     *
     * @return
     */
    @RequestMapping("/add")
    public String add() {
        return "user_add";
    }

    /**
     * 用户删除
     *
     * @return
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        userService.removeById(id);
        return "redirect:/user/findAll";
    }

    /**
     * 用户删除-多选删除
     *
     * @return
     */
    @GetMapping("/delByIds")
    public String delByIds(@RequestParam(value = "id") List<Integer> ids) {
        for (Integer id : ids) {
            System.out.println(id);
        }
        return "redirect:/user/findAll";
    }


    /**
     * 根据用户ID查询用户
     *
     * @return
     */
    @GetMapping("/{id}")
    @ResponseBody
    public User getById(@PathVariable Integer id) {
        //获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 判断认证信息是否来源于RememberMe
        if (RememberMeAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            throw new RememberMeAuthenticationException("认证信息来源于 RememberMe,请重新登录");
        }
        return userService.getById(id);
    }

}
