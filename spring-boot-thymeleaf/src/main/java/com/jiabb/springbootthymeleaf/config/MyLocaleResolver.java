package com.jiabb.springbootthymeleaf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @description: 自定义区域化配置
 * @author: jia_b
 * @date: 2024/1/13 22:02
 * @since: 1.0
 */
@Configuration
public class MyLocaleResolver implements LocaleResolver {
    // 自定义 区域解析方式
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        String l = httpServletRequest.getParameter("l");
        Locale res = null;
        if (!StringUtils.isEmpty(l)) {
            String[] s = l.split("_");
            res = new Locale(s[0], s[1]);
        } else {
            String header = httpServletRequest.getHeader("Accept-Language");
            String[] split = header.split(",");
            String[] locale = split[0].split("-");
            res = new Locale(locale[0], locale[1]);
        }

        return res;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }

    @Bean
    public LocaleResolver localeResolver() {
        return new MyLocaleResolver();
    }


}
