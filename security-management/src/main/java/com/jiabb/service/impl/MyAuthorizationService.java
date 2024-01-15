package com.jiabb.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Objects;

/**
 * @description: 自定义bean授权
 * @author: jia_b
 * @date: 2024/1/14 16:23
 * @since: 1.0
 */
@Component
public class MyAuthorizationService {

    /**
     * 检查用户是否有权限
     */
    public boolean check(Authentication authentication, HttpServletRequest request) {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        String username = userDetails.getUsername();
        if (Objects.equals("user", username)) {
            return true;
        } else {
            // 获取请求路径
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            String requestURI = request.getRequestURI();
            if (requestURI.contains("/user")) {
                //循环判断用户权限集合是否包含ROLE_AMDIN
                for (GrantedAuthority authority : authorities) {
                    if (Objects.equals("ROLE_ADMIN", authority.getAuthority())) {
                        return true;
                    }
                }
            }
            if (requestURI.contains("/product")) {
                //循环判断用户权限集合是否包含ROLE_AMDIN
                for (GrantedAuthority authority : authorities) {
                    if (Objects.equals("ROLE_PRODUCT", authority.getAuthority())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}